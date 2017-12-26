/*jshint node:true */
'use strict';
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var rename = require('gulp-rename');
var cp = require('child_process');
var del = require('del');
var rev = require('@mtfe/gulp-rev');
var revReplace = require('@mtfe/gulp-rev-replace');
var meta = require('@mtfe/gulp-meta');

var appname = 'mtupm';
var destDir = 'static/';
var webInfDir = 'WEB-INF/';

var config = {
    appKey: 'mtupm',
    development: {
        baseUri: 'http://192.168.2.116:9999/',
        hosts: ['192.168.2.116'],
        dest: '/opt/fengjr/public/'
    },
    production: {
        baseUri: 'http://cs0.fengjr.net/',
        hosts:['cos-static02', 'cos-static03'],
        dest: '/opt/fengjr/static/'
    }
};
var env = process.env.NODE_ENV || 'development';
var deployConfig = config[env];
var deployDir = 'dest/' + appname + '/';
var manifestPath = deployDir + '/manifest.json';
var staticRoot = (env === 'production') ? ('http://cs0.fengjr.net/' + appname) : ('http://192.168.2.116:9999/' + appname);

function makeRevManifest(srcFile) {
    //必须保证任务是顺序执行的，否则dest/manifest/manifest.json会有写入错误
    return gulp.src(srcFile.src, {base: destDir})
        .pipe(gulp.dest(deployDir))
        .pipe(rev({
            setFileName: function (baseName, hash) {
                return baseName + '-min.v' + hash;
            },
            hashLength: 7,
            sep: '.v'
        }))
        .pipe(gulp.dest(deployDir))
        .pipe(rev.manifest({
            merge: true,
            path: manifestPath,
            revBase: srcFile.revBase || deployDir
        }))
        .pipe(gulp.dest("."));
}

gulp.task('resourceRev', function () {
    return makeRevManifest({
        //有些工程会把图片放入css文件夹内
        src: [ destDir +  "**/*", "!" + destDir + 'bower_components/**/*']
    });
});

gulp.task('rev-replace-template', ['resourceRev'], function () {
    var manifest = gulp.src(manifestPath);
    return gulp.src(webInfDir + "**/*")
        .pipe(revReplace({manifest: manifest, prefix: staticRoot}))
        .pipe(gulp.dest(webInfDir));
});

gulp.task('rev-replace-jscss', ['resourceRev'], function () {
    var manifest = gulp.src(manifestPath);
    return gulp.src(deployDir + "**/*.{css,js}")
        .pipe(revReplace({manifest: manifest, prefix: staticRoot}))
        .pipe(gulp.dest(deployDir));
});

gulp.task('rev-replace', ['rev-replace-template', 'rev-replace-jscss']);

gulp.task('copyResourceWithVersion', ['rev-replace'], function () {
    return gulp.src(deployDir + "**/*-min.v*")
        .pipe(rename(function (path) {
            path.basename = path.basename.replace("-min.v", ".v");
        }))
        .pipe(gulp.dest(deployDir));
});

gulp.task('meta', ['copyResourceWithVersion'], function () {
    var hashLength = 7;
    return gulp.src([deployDir + "**/*.v*.css", deployDir + "**/*.v*.js", "!" + deployDir + "bower_components{,/**}"])
        .pipe(meta({
            deployDir: deployDir,
            path: deployDir + 'meta.js',
            group: appname
        }))
        .pipe(gulp.dest("."))
        .pipe(rev({
            setFileName: function (baseName, hash) {
                return baseName + '-min.v' + hash;
            },
            hashLength: hashLength,
            sep: '.v'
        }))
        .pipe(gulp.dest("."))
        .pipe(rename(function (path) {
            path.basename = path.basename.replace("-min.v", ".v");
        }))
        .pipe(gulp.dest("."))
        .pipe(meta.renderenv({
            hashLength: hashLength,
            path: webInfDir + "decorators/"+ 'env.inc'
        }))
        .pipe(gulp.dest("."));
});

//找出变化了的JS和CSS，避免全局压缩
var changedJS = [];
var changedCSS = [];
var diffResources = [];

gulp.task('syncDiff', ["meta", "rev-replace"], function (cb) {
    var exec = cp.exec;
    var cmd = "rsync -n -avrc --exclude 'bower_components' " + deployDir + "* " + deployConfig.hosts[0] + ":" + deployConfig.dest + appname + "/";
    console.log(cmd);
    exec(cmd, null, function (err, out) {
        if (out) {
            diffResources = out.split("\n");
        }
        diffResources = diffResources.filter(function(resource) {
            if (resource.indexOf("-min.v") !== -1) { //只比较未压缩文件
                return;
            }
            if (/\.v\w+(\.js$|\.css$)/.test(resource)) {
                return resource;
            }
        });
        changedJS = diffResources.filter(function(resource) {
            if (/\.js$/.test(resource)) {
                return resource;
            }
        });
        changedCSS = diffResources.filter(function(resource) {
            if (/\.css$/.test(resource)) {
                return resource;
            }
        });
        console.log("diffResources", diffResources);
        cb(err);
    });
});

gulp.task('cssmin', ["syncDiff"], function () {
    changedCSS = changedCSS.map(function (resource) {
        resource = resource.replace(/(.+)(\.v\w+\.css)$/, function (match, $1, $2) {
            return deployDir + $1 + "-min" + $2;
        });
        return resource;
    });
    console.log("changedCSS:\n", changedCSS);
    var fileToMinify = changedCSS.length ? changedCSS.concat(["!" + deployDir + 'bower_components/**/*', "!" + deployDir + 'node_modules/**/*', "!" + deployDir + '**/*.min-min*']) : [];

    return gulp.src(fileToMinify, {base: deployDir})
        .pipe($.sourcemaps.init({loadMaps: true}))
        .pipe($.minifyCss())
        .pipe($.sourcemaps.write('./'))
        .pipe(gulp.dest(deployDir));
});

gulp.task('jsmin', ["syncDiff"], function () {
    changedJS = changedJS.map(function (resource) {
        resource = resource.replace(/(.+)(\.v\w+\.js)$/, function (match, $1, $2) {
            return deployDir + $1 + "-min" + $2;
        });
        return resource;
    });
    console.log("changedJS:\n", changedJS);
    var fileToMinify = changedJS.length ? changedJS.concat(["!" + deployDir + 'bower_components/**/*', "!" + deployDir + 'node_modules/**/*', "!" + deployDir + '**/*.min-min*']) : [];

    return gulp.src(fileToMinify, {base: deployDir})
        .pipe($.sourcemaps.init({loadMaps: true}))
        .pipe($.uglify())
        .pipe($.sourcemaps.write('./'))
        .pipe(gulp.dest(deployDir));
});

/*
 * 删除未变化的文件。这些文件没有被压缩处理，如果上传到静态服务器，会覆盖已有的已压缩文件
 **/

gulp.task('rmUnchanged', ['syncDiff'], function (cb) {
    var changedMap = {};
    diffResources.forEach(function (resource) {
        resource = resource.replace(/(.+)(\.v\w+\.(js|css))$/, function (match, $1, $2) {
            return deployDir + $1 + "-min" + $2;
        });
        changedMap[resource] = true;
    });
    var exec = cp.exec;
    var cmd = [
        "find dest -type f -name *-min.v*.js",
        "find dest -type f -name *-min.v*.css"
    ];
    exec(cmd.join("&&"), null, function (err, out, code) {
        if (err instanceof Error) {
            process.exit(code);
        }
        var allFile = out.split("\n");
        var unchangedFile = allFile.filter(function (minfile) {
            if (!changedMap[minfile]) {
                return minfile;
            }
        });
        console.log("unchangedFile", unchangedFile.length);
        del(unchangedFile, {}, function () {
            cb(err);
        });
    });
});

gulp.task('deploy', ['jsmin', 'cssmin', 'rmUnchanged'], function (cb) {
    var exec = cp.exec;
    var hosts = deployConfig.hosts;
    var cmd = [];
    if (hosts.length === 0) {
        return;
    }
    hosts.forEach(function(host) {
        cmd.push("rsync -ap --exclude 'bower_components'  dest/* " + host + ":" + deployConfig.dest);
    });
    console.log("this rsync info:", cmd);
    exec(cmd.join('&&'), null, function (err, out, code) {
        console.log(err, out);
        if (err instanceof Error) {
            process.exit(code);
        }
        cb(err);
    });
});
