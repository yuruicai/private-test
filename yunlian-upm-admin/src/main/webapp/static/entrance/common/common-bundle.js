require=(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
/* ============================================================
 * bootstrap-dropdown.js v2.3.1
 * http://twitter.github.com/bootstrap/javascript.html#dropdowns
 * ============================================================
 * Copyright 2012 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============================================================ */


/*jshint indent:2,unused:false,laxcomma:true,asi:true,eqeqeq:false,bitwise:false,expr:true*/
!function ($) {

  "use strict";


 /* DROPDOWN CLASS DEFINITION
  * ========================= */

  var toggle = '[data-toggle=dropdown]'
    , Dropdown = function (element) {
        var $el = $(element).on('click.dropdown.data-api', this.toggle)
        $('html').on('click.dropdown.data-api', function () {
          $el.parent().removeClass('open')
        })
      }

  Dropdown.prototype = {

    constructor: Dropdown

  , toggle: function (e) {
      var $this = $(this)
        , $parent
        , isActive

      if ($this.is('.disabled, :disabled')) return

      $parent = getParent($this)

      isActive = $parent.hasClass('open')

      clearMenus()

      if (!isActive) {
        $parent.toggleClass('open')
      }

      $this.focus()

      return false
    }

  , keydown: function (e) {
      var $this
        , $items
        , $active
        , $parent
        , isActive
        , index

      if (!/(38|40|27)/.test(e.keyCode)) return

      $this = $(this)

      e.preventDefault()
      e.stopPropagation()

      if ($this.is('.disabled, :disabled')) return

      $parent = getParent($this)

      isActive = $parent.hasClass('open')

      if (!isActive || (isActive && e.keyCode == 27)) {
        if (e.which == 27) $parent.find(toggle).focus()
        return $this.click()
      }

      $items = $('[role=menu] li:not(.divider):visible a', $parent)

      if (!$items.length) return

      index = $items.index($items.filter(':focus'))

      if (e.keyCode == 38 && index > 0) index--                                        // up
      if (e.keyCode == 40 && index < $items.length - 1) index++                        // down
      if (!~index) index = 0

      $items
        .eq(index)
        .focus()
    }

  }

  function clearMenus() {
    $(toggle).each(function () {
      getParent($(this)).removeClass('open')
    })
  }

  function getParent($this) {
    var selector = $this.attr('data-target')
      , $parent

    if (!selector) {
      selector = $this.attr('href')
      selector = selector && /#/.test(selector) && selector.replace(/.*(?=#[^\s]*$)/, '') //strip for ie7
    }

    $parent = selector && $(selector)

    if (!$parent || !$parent.length) $parent = $this.parent()

    return $parent
  }


  /* DROPDOWN PLUGIN DEFINITION
   * ========================== */

  var old = $.fn.dropdown

  $.fn.dropdown = function (option) {
    return this.each(function () {
      var $this = $(this)
        , data = $this.data('dropdown')
      if (!data) $this.data('dropdown', (data = new Dropdown(this)))
      if (typeof option == 'string') data[option].call($this)
    })
  }

  $.fn.dropdown.Constructor = Dropdown


 /* DROPDOWN NO CONFLICT
  * ==================== */

  $.fn.dropdown.noConflict = function () {
    $.fn.dropdown = old
    return this
  }


  /* APPLY TO STANDARD DROPDOWN ELEMENTS
   * =================================== */

  $(document)
    .on('click.dropdown.data-api', clearMenus)
    .on('click.dropdown.data-api', '.dropdown form', function (e) { e.stopPropagation() })
    .on('click.dropdown-menu', function (e) { e.stopPropagation() })
    .on('click.dropdown.data-api'  , toggle, Dropdown.prototype.toggle)
    .on('keydown.dropdown.data-api', toggle + ', [role=menu]' , Dropdown.prototype.keydown)

}(window.jQuery);

},{}],"common":[function(require,module,exports){
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

    $('#upm-menus li .dropdown-collapse').click(function(e) {
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

},{"../../lib/bootstrap-dropdown":1}]},{},[]);

function clickFirstMenu(firstMenu) {
    var $a = $(firstMenu);
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
}

function selectMenu(obj) {
    window.location.href = $(obj).data('url');
}
