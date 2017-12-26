# 开发环境

```sh
npm -r http://r.npm.sankuai.com install
```

# 开发 

```sh
npm start
```

# 规范

- jquery - global module
- react - global module
- require('common')
- require('main') - 需要 init()

build 出来的 bundle 的 expose:

entrance/common/common.js 为 common
其他的均为 main

entrance/components 为 React components
构建方法如下：

```js
jsx src/components entrance/components
```

# 测试

karma


# 发布

NODE_ENV=production gulp

