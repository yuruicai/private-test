/*jshint node:true */

if (typeof global._gaq === 'undefined') {
    global._gaq = [];
}

var config = require('../config');

if (M.GA_ACCOUNT) {
    if (global.addEventListener) {
        global.addEventListener('load', loadGA, false);
    } else if (window.attachEvent) {
        global.attachEvent('onload', loadGA);
    } else {
        loadGA();
    }
}

function loadGA() {
    // ga参数初始化
    global._gaq.push(
        [ '_setAccount', config.GA.account ],
        [ '_trackPageview' ]
    );
    var ga = document.createElement('script');
    ga.type = 'text/javascript';
    ga.async = true;
    ga.src = ('https:' === document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('body')[0];
    s.appendChild(ga, s);
}
