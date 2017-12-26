require=(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
var inserted = {};

module.exports = function (css, options) {
    if (inserted[css]) return;
    inserted[css] = true;
    
    var elem = document.createElement('style');
    elem.setAttribute('type', 'text/css');

    if ('textContent' in elem) {
      elem.textContent = css;
    } else {
      elem.styleSheet.cssText = css;
    }
    
    var head = document.getElementsByTagName('head')[0];
    if (options && options.prepend) {
        head.insertBefore(elem, head.childNodes[0]);
    } else {
        head.appendChild(elem);
    }
};

},{}],2:[function(require,module,exports){
/**
 * Copyright 2013-2014 Facebook, Inc.
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
 *
 * @providesModule copyProperties
 */

/**
 * Copy properties from one or more objects (up to 5) into the first object.
 * This is a shallow copy. It mutates the first object and also returns it.
 *
 * NOTE: `arguments` has a very significant performance penalty, which is why
 * we don't support unlimited arguments.
 */
function copyProperties(obj, a, b, c, d, e, f) {
  obj = obj || {};

  if ("production" !== "production") {
    if (f) {
      throw new Error('Too many arguments passed to copyProperties');
    }
  }

  var args = [a, b, c, d, e];
  var ii = 0, v;
  while (args[ii]) {
    v = args[ii++];
    for (var k in v) {
      obj[k] = v[k];
    }

    // IE ignores toString in object iteration.. See:
    // webreflection.blogspot.com/2007/07/quick-fix-internet-explorer-and.html
    if (v.hasOwnProperty && v.hasOwnProperty('toString') &&
        (typeof v.toString != 'undefined') && (obj.toString !== v.toString)) {
      obj.toString = v.toString;
    }
  }

  return obj;
}

module.exports = copyProperties;

},{}],3:[function(require,module,exports){
(function (global){
/**
 * @jsx React.DOM
 */
"use strict";

var React = global.React || require('react');
var has = require('has');
var copyProperties = require('react/lib/copyProperties');

var Menu = React.createClass({displayName: 'Menu',
    render: function() {
        var MenuItem = require('./MenuItem');
        if (!has(this.props, 'menus')) {
            return null;
        }
        if (this.props.menus.length === 0) {
            return null;
        }
        var list = this.props.menus.map(function(item) {
            item = copyProperties({
                key: item.id
            }, item);
            return MenuItem(item);
        });
        return (
            React.DOM.ul(null, 
                list
            )
        );
    }
});

module.exports = Menu;


}).call(this,typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {})
},{"./MenuItem":4,"has":7,"react":"react","react/lib/copyProperties":2}],4:[function(require,module,exports){
(function (global){
/**
 * @jsx React.DOM
 */
"use strict";

var React = global.React || require('react');
var Menu = require('./Menu');

var MenuItem = React.createClass({displayName: 'MenuItem',
    render: function() {
        var submenu = Menu(this.props);
        return (
            React.DOM.li({className: this.props.isActive ? 'active' : ''}, 
                React.DOM.a({'data-id': this.props.id}, this.props.title), 
                submenu
            )
        );
    }
});

module.exports = MenuItem;

}).call(this,typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {})
},{"./Menu":3,"react":"react"}],5:[function(require,module,exports){
(function (global){
/**
 * @jsx React.DOM
 */
"use strict";



var insertCss = require('insert-css');
var React = global.React || require('react');

var Menu = require('./components/Menu');
var forEachMenus = require('./lib/forEachMenus');

//
// 符合 cosui 的侧边栏，使用 React 撰写
//
var Sidebar = React.createClass({

    displayName: 'sidebar',

    getInitialState: function() {
        var activeIds = [];
        forEachMenus(this.props.menus, function(item) {
            if (item.isActive) {
                activeIds.push(item.id);
            }
        });

        return {
            activeIds: activeIds
        };
    },

    componentDidMount: function() {
        insertCss("#l-sidebar {\n  float: left;\n  width: 180px;\n  min-height: 400px;\n  background-color: #f9f9f9;\n  border-right: 1px solid #e9e9e9; }\n  #l-sidebar ul {\n    list-style: none;\n    padding-left: 0;\n    margin-top: 0;\n    margin-bottom: 0; }\n  #l-sidebar a {\n    display: block;\n    height: 40px;\n    line-height: 40px;\n    padding: 0 10px;\n    cursor: pointer; }\n  #l-sidebar li.active > a {\n    color: white;\n    background-color: #ff9a2f;\n    border-right: 3px solid #ff8b10; }\n\n/*# sourceMappingURL=style.css.map */\n");
    },

    handleMouseEnter: function(e) {
        console.log(e);
    },

    handleMouseLeave: function(e) {
        console.log(e);
    },

    render: function() {
        if ("production" === 'development') {
            console.info(this.props);
            console.info(this.state);
        }
        return (
            React.DOM.div({className: "l-sidebar-content", 
                onMouseEnter: this.handleMouseEnter, 
                onMouseLeave: this.handleMouseLeave
            }, 
                Menu({ menus: this.props.menus })
            )
        );
    }
});

module.exports = Sidebar;


}).call(this,typeof global !== "undefined" ? global : typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : {})
},{"./components/Menu":3,"./lib/forEachMenus":6,"insert-css":1,"react":"react"}],6:[function(require,module,exports){
var has = require('has');

function forEachMenuItems(menus, fn) {
    if (menus.length === 0) {
        return;
    }
    menus.forEach(function(menu) {
        fn(menu);
        if (has(menu, 'menus')) {
            forEachMenuItems(menu.menus, fn);
        }
    });
}

module.exports = forEachMenuItems;

},{"has":7}],7:[function(require,module,exports){
var hasOwn = Object.prototype.hasOwnProperty;


module.exports = function has(obj, property) {
  return hasOwn.call(obj, property);
};

},{}],"app/sidebar":[function(require,module,exports){
var Sidebar = require('sidebar');

var AppSidebar = Sidebar;

module.exports = AppSidebar;

},{"sidebar":5}]},{},[]);
