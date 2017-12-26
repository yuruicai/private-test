#! /bin/bash
set -e
# 保证cwd永远在当前文件所在目录
pushd ${0%/*}

APPKEY="mtupm";

if [ "$NODE_ENV" = "production" ] || [ "$1" = "production" ]; then
    CACHEMACHINE='dx-fe-nodemodules01';
else
    CACHEMACHINE='192.168.2.116';
fi

echo "scp $CACHEMACHINE:/opt/fengjr/public/$APPKEY/node_modules.tar.gz ./ || true"

scp $CACHEMACHINE:/opt/fengjr/public/$APPKEY/node_modules.tar.gz ./ || true
if [ -f ./node_modules.tar.gz ]; then
    echo "tar -zxf ./node_modules.tar.gz";
    tar -zxf ./node_modules.tar.gz
    rm ./node_modules.tar.gz
else
    echo "没有缓存的node_modules，直接下载";
fi
# sass
export SASS_BINARY_SITE="http://fe-engine02.lf.sankuai.com:8080/node-sass";

echo "npm --registry=http://r.npm.sankuai.com install";
npm --registry=http://r.npm.sankuai.com install

#echo "./node_modules/.bin/bower build || true";
# ./node_modules/.bin/bower install || true

# 打包node_modules
echo "tar -zcf ./$APPKEY/node_modules.tar.gz ./node_modules";
if [ ! -d "$APPKEY" ]; then
    mkdir $APPKEY
fi
tar -zcf ./$APPKEY/node_modules.tar.gz ./node_modules

# 上传文件到缓存服务器
echo "rsync -ap ./$APPKEY $CACHEMACHINE:/opt/fengjr/public/ || true"
rsync -ap ./$APPKEY $CACHEMACHINE:/opt/fengjr/public/ || true
# 删除tar包
rm -rf ./$APPKEY || true

if [ x"$1" = x ]; then
    echo 参数parm不存在或者为空值
    exit 2
fi

echo "NODE_ENV=$1 ./node_modules/.bin/gulp build"
NODE_ENV=$1 ./node_modules/.bin/gulp build

echo "NODE_ENV=$1 ./node_modules/.bin/gulp deploy"
NODE_ENV=$1 ./node_modules/.bin/gulp deploy

echo "rm -rf ./node_modules"
rm -rf ./node_modules || true

cat WEB-INF/decorators/env.inc

if [ -d ./dest ]; then
    echo "rm -rf ./dest";
    rm -rf ./dest
else
    echo "没有./dest目录";
fi

popd
