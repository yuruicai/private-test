/*jshint node:true */
'use strict';
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var autoprefixer = require('autoprefixer-core');
var del = require('del');
var browserify = require('browserify');
var transform = require('vinyl-transform');

var requireDir = require('require-dir');
requireDir('./gulptasks', { recurse: true });

var srcDir = 'app/';
var destDir = 'static/';
var webInfDir = 'WEB-INF/';
var webappDir = './';
var env = process.env.NODE_ENV || 'development';

gulp.task('styles', function () {
    return gulp.src(srcDir + 'css/*.css')
        .pipe($.postcss([
            autoprefixer({browsers: ['last 1 version']})
        ]))
        .pipe(gulp.dest(destDir + 'css'));
});

gulp.task('scripts', function(){
    var browserified = transform(function (filename) {
        return browserify(filename, {debug: true})
            .add('es5-shim')
            .bundle();
    });
    browserified.on('error', function(e){
        // why?
        delete e.stream;
        console.error(e.message);
        this.emit('end');
    });

    return gulp.src(srcDir + 'js/**/index.page.js')
        .pipe(browserified)
        .pipe(gulp.dest(destDir + 'js'));
});

gulp.task('images', function () {
    return gulp.src(srcDir + 'images/**/*')
        .pipe($.imagemin({
            progressive: true,
            interlaced: true,
            // don't remove IDs from SVGs, they are often used
            // as hooks for embedding and styling
            svgoPlugins: [{cleanupIDs: false}]
        }))
        .pipe(gulp.dest(destDir + 'images'));
});

gulp.task('extras', function () {
    return gulp.src([
        srcDir + '*.*'
    ], {
        dot: true
    }).pipe(gulp.dest(destDir));
});



gulp.task('watch', ['build'], function(){
    gulp.watch(srcDir + 'css/**/*.css', ['styles']);
    gulp.watch(srcDir + 'js/**/*.js', ['scripts']);
    gulp.watch(srcDir + 'images/**/*', ['images']);
    gulp.watch(srcDir + '*.*', ['extras']);
});

var buildTasks = env === 'production' ? ['styles', 'scripts'] : ['extras', 'styles', 'scripts', 'images'];
gulp.task('build', buildTasks, function () {
    return gulp.src(destDir + '**/*').pipe($.size({title: 'build', gzip: true}));
});


gulp.task('default', function () {
    gulp.start('build');
});
