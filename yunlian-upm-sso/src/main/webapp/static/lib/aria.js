/**
 * get state and props from arai attibutes
 */
var jquery = window.jQuery || require('jquery');
var stateAttrs = [
    'busy',
    'disabeld',
    'grabbed',
    'hidden',
    'invalid',
    'expanded',
    'pressed'
];
var propAttrs = [
    'atomic',
    'controls',
    'describedby',
    'dropeffect',
    'flowto',
    'haspopup',
    'label',
    'labelledby',
    'live',
    'owns',
    'relevant'
];
module.exports = function aria(el) {
    var $el = jquery(el);
    var role = $el.attr('role');
    if (role !== 'button') {
        throw new Error('Not Implemented');
    }
    var ctx = {
        state: {},
        props: {}
    };
    jquery.each(stateAttrs, function(i, key) {
        var val = $el.attr('aria-' + key);
        if (typeof val === 'undefined') {
            return;
        }
        if (val === 'true') {
            val = true;
        } else {
            val = false;
        }
        ctx.state[key] = val;
    });
    jquery.each(propAttrs, function(i, key) {
        var val = $el.attr('aria-' + key);
        if (typeof val === 'undefined') {
            return;
        }
        ctx.props[key] = val;
    });
    return ctx;
};

