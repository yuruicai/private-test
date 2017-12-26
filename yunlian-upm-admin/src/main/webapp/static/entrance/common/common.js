/*jshint node:true*/
/*global $*/
require('../../lib/bootstrap-dropdown');

$(function() {
    $('#menus .dropdown-toggle').hover(function() {
        $(this).parent('.dropdown').addClass('open');
    }, function(e) {
        var $rt = $(e.relatedTarget);
        var $this = $(this);
        var $menu = $this.next();
        if ($rt.is('.dropdown-menu') || $.contains($menu, $rt)) {
            return;
        }
        $this.parent('.dropdown').removeClass('open');
    });

    $('#menus .dropdown-menu').on('mouseleave', function() {
        var $this = $(this);
        var $parent = $this.parent('.dropdown');
        if ($parent.hasClass('open')) {
            $parent.removeClass('open');
        }
    });

    $('.navigation > ul > li > a').click(function(e) {
        var $a = $(e.currentTarget);
        var $li = $a.parent();
        var $subnav = $li.find('ul');
        if ($subnav.length <= 0) {
            return;
        }
        $subnav.toggleClass('in');
        var $angle = $a.find('.angle-down');
        if ($subnav.hasClass('in')) {
            $angle.removeClass('fa-angle-up').addClass('fa-angle-down');
        } else {
            $angle.removeClass('fa-angle-down').addClass('fa-angle-up');
        }
    });
});
