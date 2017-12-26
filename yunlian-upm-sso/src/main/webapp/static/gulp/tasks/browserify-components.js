
var path = require('path');

var gulp = require('gulp');
var gutil = require('gulp-util');
var browserify = require('browserify');
var watchify = require('watchify');
var glob = require('glob');
var source = require('vinyl-source-stream');
var regrevision = require('../util/reg_revision');

function browserifyComponents(done) {
    var files = glob.sync('entrance/components/*.js');
    files = files.filter(function(file) {
        if (regrevision.test(file)) {
            return false;
        }
        var extname = path.extname(file);
        var re = new RegExp('-bundle' + extname + '$');
        if (re.test(file)) {
            return false;
        }
        return true;
    });
    var pending = files.length;
    if (pending === 0) {
        return done();
    }
    function testEnd(err) {
        if (err) {
            return done(err);
        }
        pending--;
        if (pending === 0) {
            done();
        }
    }
    function brIt(file) {
        var isDebug = process.env.NODE_ENV === 'development' || process.env.NODE_ENV === undefined;
        var br = browserify({
            cache: {},
            packageCache: {},
            fullPaths: isDebug,
            entries: [ './' + file ],
            transform: [ 'brfs' ],
            debug: isDebug
        });
        var extname = path.extname(file);
        var basename = path.basename(file, extname);
        var expose = 'app/components/' + basename;
        function brset() {
            br.external('react');
            br.require('./' + file, { expose: expose });
        }
        brset();
        function bundle() {
            return br.bundle()
                .pipe(source(basename + '-bundle' + extname))
                .on('error', gutil.log.bind(gutil, 'Browserify Error'))
                .pipe(gulp.dest('entrance/components'))
                .on('error', testEnd)
                .on('finish', gutil.log.bind(gutil, 'Browserify finish ' + expose))
                .on('finish', testEnd);
        }
        if (global.isWatching) {
            br.on('reset', brset);
            var wbr = watchify(br);
            wbr.on('update', bundle);
            wbr.on('log', gutil.log.bind(gutil, 'Browserify log'));
        }
        return bundle();
    }
    files.forEach(brIt);
}

module.exports = browserifyComponents;

