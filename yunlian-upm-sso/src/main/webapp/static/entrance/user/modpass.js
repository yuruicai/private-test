
var forEach = require('lodash.foreach');

exports.init = function init(config) {
    this.config = config;

    if (this.config.needsSendSms) {
        require('../../lib/sendsms');
    }

    this.username = this.config.username;
    this.$form = $(this.config.form || 'form');
    this.fields = this.config.fields.concat();
    this.bindUI();
};

exports.bindUI = function bindUI() {
    var names = [];
    this.fieldsByName = {};
    forEach(this.fields, function(field) {
        field.$form = this.$form;
        field.$el = this.$form.find('[name="' + field.name + '"]');
        names.push(field.name);
        this.fieldsByName[field.name] = field;
    }, this);

    var self = this;
    var filter = '[name="' + names.join('"], [name="') + '"]';
    this.$form.on('blur', filter, function(e) {
        self.onblur(e.target);
    });
    this.$form.on('submit', function(e) {
        self.onsubmit(e);
    });

    $('input[type=radio][name=verifyType]').change(function() {
        if (this.value === 'otp') {
            $("#div_smsCode").hide();
            $("#div_optCode").show();
        }
        else if (this.value === 'mobile') {
            $("#div_smsCode").show();
            $("#div_optCode").hide();
        }
    });
};

exports.onblur = function onblur(el) {
    var self = this;
    var $el = $(el);
    var name = $el.attr('name');
    var field = this.fieldsByName[name];

    field.value = $el.val();

    this.validate(field, function(err, result) {
        onvalidate(field, err, result);
        self.uiSetValidate(field);
    });
};

exports.onsubmit = function(e) {
    e.preventDefault();
    this.validate(function(err) {
        if (err) {
            return;
        }
        e.target.submit();
    });
};

function onvalidate(field, err, result) {
    if (err) {
        field.validateResult = err;
    } else {
        field.validateResult = result;
    }
    field.validateState = 'validated';
}

function validateFields(mod, done) {
    var pending = mod.fields.length;
    var hasErr = null;
    forEach(mod.fields, function(field) {
        field.value = field.$el.val();
        var self = this;
        this.validate(field, function(err, result) {
            pending--;
            if (err && hasErr === null) {
                hasErr = err;
            }
            onvalidate(field, err, result);
            self.uiSetValidate(field);
            if (pending === 0) {
                done(hasErr);
            }
        });
    }, mod);
}

exports.validate = function(field, done) {
    // all fields
    if (typeof field === 'function') {
        done = field;
        validateFields(this, done);
        return;
    }

    var err;
    if (field.needsSameAs) {
        if (field.value !== this.$form.find('[name="' + field.needsSameAs + '"]').val()) {
            err = new Error(field.label + ' 和 ' +
                    this.fieldsByName[field.needsSameAs].label + '不一致');
            return done(err);
        }
    }

    if (field.validate) {
        field.validate(field, field.value, function(err, result) {
            done(err, result);
        });
        return;
    }

    if(!field.validateUrl) {
        return done();
    }

    field.validateState = 'validating';
    field.validateResult = null;
    this.uiSetValidate(field);

    var url = field.validateUrl;
    $.post(url, { username: this.username, password: field.value }, function(response) {
        var err = null;
        if (response.status === 500) {
            err = new Error(response.errorMsg);
        }
        done(err, response);
    });
};

exports.types = [ 'warning', 'error', 'success' ];

exports.uiSetValidate = function(field) {
    var $el = field.$el;
    var $help = $el.next('.help-inline');
    var msg;
    var type;
    switch (field.validateState) {
        case 'validating':
            msg = '正在验证...';
            break;
        case 'validated':
            msg = '';
            if (field.validateResult instanceof Error) {
                msg = field.validateResult.message;
                type = 'error';
                break;
            }
            msg = '验证 OK';
            type = 'success';
            break;
        default:
            break;
    }

    var otherTypes = [];
    forEach(this.types, function(type_) {
        if (type_ !== type) {
            otherTypes.push(type_);
        }
    });

    var $controlGroup = $el.parents('.control-group');

    forEach(otherTypes, function(type_) {
        $help.removeClass('text-' + type_);
        $controlGroup.removeClass(type_);
    });

    if (!type) {
        $help.hide();
        return;
    }

    $controlGroup.addClass(type);
    if ($help.length > 0) {
        $help.text(msg);
        $help.addClass('text-' + type);
    } else {
        $el.after('<span class="help-inline text-">' + msg + '</span>');
    }
    $help.show();
};


