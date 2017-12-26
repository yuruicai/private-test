/* jshint node: true */
"use strict";

require('../config');

var path = require('path');

var buildSite = require('mtfe_build_site');
var buildSiteConfig = require('mtfe_build_site/config');

buildSiteConfig({
    site: 'mtupm',
    webappPath: path.resolve('../')
}, function(err, config) {
    if (err) {
        throw err;
    }
    buildSite(config, function(err) {
        if (err) {
            console.error(err.filepath);
            console.error(err.moduleContent);
            throw err;
        }
        console.log('ALL TASKS ARE DONE!');
    });
});

