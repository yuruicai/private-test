/*jshint node:true*/

var gulp = require('gulp');
var gulpreact = require('gulp-react');
var browserifyEntrance = require('./gulp/tasks/browserify-entrance');
var browserifyComponents = require('./gulp/tasks/browserify-components');

gulp.task('copy-components-css', function() {
    gulp.src('src/components/*.css')
        .pipe(gulp.dest('entrance/components'));
});

gulp.task('watch-components-css', function() {
    gulp.watch('src/components/**.css', [ 'copy-components-css' ]);
});

gulp.task('watch-components-jsx', function() {
    gulp.watch('src/components/**.js', [ 'jsx' ]);
});

gulp.task('jsx', function() {
    gulp.src('src/components/*.js')
        .pipe(gulpreact())
        .pipe(gulp.dest('entrance/components'));
});

gulp.task('browserify-entrance', browserifyEntrance);

gulp.task('browserify-components', browserifyComponents);

gulp.task('setWatch', function() {
    global.isWatching = true;
    process.env.NODE_ENV = 'development';
});

gulp.task('default', [
    'browserify-components',
    'browserify-entrance'
]);

gulp.task('develop', [
    'setWatch',
    'watch-components-css',
    'watch-components-jsx',
    'browserify-components',
    'browserify-entrance'
]);

