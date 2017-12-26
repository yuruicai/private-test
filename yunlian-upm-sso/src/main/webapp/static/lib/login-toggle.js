exports.init = function init(config) {
    this.config = config;
    this.type = this.config.type;
    this.$toggle = $(config.toggle);
    this.bindUI();
};

exports.bindUI = bindUI;
exports.onToggle = onToggle;

function bindUI() {
    var self = this;
    this.$toggle.click(function(e) {
        self.onToggle(e);
    });
}

function onToggle(e) {
    var $el = $(e.currentTarget);
    var type = $el.data('type');

    var othertypes = [];
    $.each(this.config.types, function(i, item) {
        if (item !== type) {
            othertypes.push(item);
        }
    });

    var $section = $el.parents('.login-section');
    var $tabs = $section.find('.login-tabs');

    $tabs.find('.login-toggle').removeClass('is-active');
    $tabs.find('.login-toggle-' + type).addClass('is-active');

    $tabs.removeClass('login-type-' + othertypes.join('login-type-'));
    $tabs.addClass('login-type-' + type);

    $section.find('.login-container').hide();
    $section.find('.login-' + type + '-container').show();
    if (type === 'form') {
        $section.find('input[type="text"]').focus();
    }
}

