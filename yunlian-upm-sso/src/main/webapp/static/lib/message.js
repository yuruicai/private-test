/*jshint node:true, expr:true, camelcase:false*/
/*global $*/
/**
 * info, warn, error
 */
module.exports = message;

function message(opts) {
    return new Message(opts);
}

function Message(opts) {
    opts || (opts = {});
    this.css_prefix = opts.css_prefix;
    this.parentEl = opts.parentEl;
    if (typeof this.parentEl === 'string') {
        this.parentEl = $(this.parentEl);
    }
    this.container = null;
}

Message.prototype.css_prefix = 'message';

Message.prototype.show = function(msg, type) {
    if (!this.container) {
        this.container = $('<div class="' + this.css_prefix + '"></div>');
        this.container.appendTo(this.parentEl);
    }
    this.container.removeClass();
    this.container.addClass(this.css_prefix + ' ' + this.css_prefix + '-' + type);
    this.container.html(msg);
    return this;
};

Message.prototype.warn = function(msg) {
    return this.show(msg, 'warn');
};

Message.prototype.error = function(msg) {
    return this.show(msg, 'error');
};

Message.prototype.info = function(msg) {
    return this.show(msg, 'info');
};

