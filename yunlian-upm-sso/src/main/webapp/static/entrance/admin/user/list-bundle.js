require=(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
var dispatcher = require('./dispatcher');

exports.toggleUserStatus = function(id) {
    dispatcher.dispatch({
        actionType: 'TOGGLE_USER_STATUS',
        id: id
    });
};

exports.toggleUserFreeze = function(id) {
    dispatcher.dispatch({
        actionType: 'TOGGLE_USER_FREEZE',
        id: id
    });
};

exports.initUserStatusStore = function(users) {
    dispatcher.dispatch({
        actionType: 'INIT_USER_STATUS',
        users: users
    });
};

},{"./dispatcher":2}],2:[function(require,module,exports){
var flux = require('flux');

module.exports = new flux.Dispatcher();

},{"flux":12}],3:[function(require,module,exports){
exports.querymobile = require('./querymobile');
exports.userstatus = require('./userstatus');
exports.userfreeze = require('./userfreeze');
exports.initStore = require('./initstore');
exports.resetpasswordbysys = require('./resetpasswordbysys');

},{"./initstore":4,"./querymobile":5,"./resetpasswordbysys":6,"./userfreeze":7,"./userstatus":8}],4:[function(require,module,exports){
var actions = require('./actions');

module.exports = function(users) {
    actions.initUserStatusStore(users);
};

},{"./actions":1}],5:[function(require,module,exports){
var jquery = window.jQuery || require('jquery');

module.exports = querymobile;

function querymobile() {
    var self = this;
    jquery.get('/admin/user/mobile', { id: this.id }, function(data/*, textStatus, jqXHR*/) {
        var msg = data.result.msg;
        if (msg === null) {
            msg = '还没有手机号';
        }
        jquery(self.el).replaceWith('<span>' + msg + '</span>');
    });
}


},{"jquery":"jquery"}],6:[function(require,module,exports){
var jquery = window.jQuery || require('jquery');

module.exports = function() {
    var uri = '/admin/user/resetPassBySys';
    if (!window.confirm('确定要重置密码?')) {
        return;
    }
    jquery.get(uri, { id: this.id })
        .done(function(data) {
            window.alert(data.result.msg);
        })
        .fail(function() {
            window.alert('发生错误，操作失败!');
        });
};

},{"jquery":"jquery"}],7:[function(require,module,exports){
var jquery = window.jQuery || require('jquery');

var userstatusstore = require('./userstatusstore');
var actions = require('./actions');

module.exports = function() {
    this.previousStatus = userstatusstore.getState(this.id);
    if (!window.confirm('确定要进行此操作？')) {
        return;
    }
    var ctx = this;
    var url = '/admin/user/' + (this.previousStatus === 'active' ? 'freeze' : 'recovery');
    jquery.get(url, { id: this.id }, function(/*data*/) {
        actions.toggleUserFreeze(ctx.id);
        updateView(ctx);
    });
};

function updateView(ctx) {
    var $el = jquery(ctx.el);
    var status = userstatusstore.getState(ctx.id);
    var previousStatus = ctx.previousStatus;

    var statusText = ctx.statusMap[status];
    $el.parents('tr').find('.userstatus-describe').text(statusText);
    var toggleText = ctx.statusMap[previousStatus];
    if (status === 'freeze') {
        toggleText = '解冻';
    }
    $el.text(toggleText);
}

},{"./actions":1,"./userstatusstore":9,"jquery":"jquery"}],8:[function(require,module,exports){
var jquery = window.jQuery || require('jquery');

var userstatusstore = require('./userstatusstore');
var actions = require('./actions');

module.exports = function() {
    if (!window.confirm('确定要进行此操作？')) {
        return;
    }
    this.previousStatus = userstatusstore.getState(this.id);
    var ctx = this;
    var url = '/admin/user/' + (this.previousStatus === 'active' ? 'disable' : 'enable');
    jquery.get(url, { id: this.id }, function(/*data*/) {
        actions.toggleUserStatus(ctx.id);
        updateView(ctx);
    });
};

function updateView(ctx) {
    var $el = jquery(ctx.el);
    var status = userstatusstore.getState(ctx.id);
    var statusText = ctx.statusMap[status];
    $el.parents('tr').find('.userstatus-describe').text(statusText);
    var previousStatus = ctx.previousStatus;
    var toggleText = ctx.statusMap[previousStatus];
    $el.text(toggleText);
}

},{"./actions":1,"./userstatusstore":9,"jquery":"jquery"}],9:[function(require,module,exports){
var dispatcher = require('./dispatcher');

var userstatusstore = {};

exports.token = dispatcher.register(function(payload) {
    if (payload.actionType === 'TOGGLE_USER_STATUS') {
        if (typeof payload.status === 'undefined') {
            payload.status = getStatus(payload.id);
        }
        userstatusstore[payload.id] = payload.status;
    }
    else if (payload.actionType === 'TOGGLE_USER_FREEZE') {
        userstatusstore[payload.id] = getFreeze(payload.id);
    }
    else if (payload.actionType === 'INIT_USER_STATUS') {
        payload.users.forEach(function(user) {
            userstatusstore[user.id] = user.status;
        });
    }
});

exports.getState = function(userid) {
    return userstatusstore[userid] || null;
};

function getStatus(id) {
    var prevVal = userstatusstore[id];
    if (typeof prevVal === 'undefined') {
        return null;
    }
    if (prevVal === 'active') {
        return 'delete';
    }
    if (prevVal === 'delete') {
        return 'active';
    }
    return null;
}

function getFreeze(id) {
    var prevVal = userstatusstore[id];
    if (typeof prevVal === 'undefined') {
        return null;
    }
    if (prevVal === 'active') {
        return 'freeze';
    }
    if (prevVal === 'freeze') {
        return 'active';
    }
    return null;
}


},{"./dispatcher":2}],10:[function(require,module,exports){
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


},{"jquery":"jquery"}],11:[function(require,module,exports){

exports.setState = function() {
};

exports.render = function() {
};

},{}],12:[function(require,module,exports){
/**
 * Copyright (c) 2014, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

module.exports.Dispatcher = require('./lib/Dispatcher')

},{"./lib/Dispatcher":13}],13:[function(require,module,exports){
/*
 * Copyright (c) 2014, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 *
 * @providesModule Dispatcher
 * @typechecks
 */

"use strict";

var invariant = require('./invariant');

var _lastID = 1;
var _prefix = 'ID_';

/**
 * Dispatcher is used to broadcast payloads to registered callbacks. This is
 * different from generic pub-sub systems in two ways:
 *
 *   1) Callbacks are not subscribed to particular events. Every payload is
 *      dispatched to every registered callback.
 *   2) Callbacks can be deferred in whole or part until other callbacks have
 *      been executed.
 *
 * For example, consider this hypothetical flight destination form, which
 * selects a default city when a country is selected:
 *
 *   var flightDispatcher = new Dispatcher();
 *
 *   // Keeps track of which country is selected
 *   var CountryStore = {country: null};
 *
 *   // Keeps track of which city is selected
 *   var CityStore = {city: null};
 *
 *   // Keeps track of the base flight price of the selected city
 *   var FlightPriceStore = {price: null}
 *
 * When a user changes the selected city, we dispatch the payload:
 *
 *   flightDispatcher.dispatch({
 *     actionType: 'city-update',
 *     selectedCity: 'paris'
 *   });
 *
 * This payload is digested by `CityStore`:
 *
 *   flightDispatcher.register(function(payload) {
 *     if (payload.actionType === 'city-update') {
 *       CityStore.city = payload.selectedCity;
 *     }
 *   });
 *
 * When the user selects a country, we dispatch the payload:
 *
 *   flightDispatcher.dispatch({
 *     actionType: 'country-update',
 *     selectedCountry: 'australia'
 *   });
 *
 * This payload is digested by both stores:
 *
 *    CountryStore.dispatchToken = flightDispatcher.register(function(payload) {
 *     if (payload.actionType === 'country-update') {
 *       CountryStore.country = payload.selectedCountry;
 *     }
 *   });
 *
 * When the callback to update `CountryStore` is registered, we save a reference
 * to the returned token. Using this token with `waitFor()`, we can guarantee
 * that `CountryStore` is updated before the callback that updates `CityStore`
 * needs to query its data.
 *
 *   CityStore.dispatchToken = flightDispatcher.register(function(payload) {
 *     if (payload.actionType === 'country-update') {
 *       // `CountryStore.country` may not be updated.
 *       flightDispatcher.waitFor([CountryStore.dispatchToken]);
 *       // `CountryStore.country` is now guaranteed to be updated.
 *
 *       // Select the default city for the new country
 *       CityStore.city = getDefaultCityForCountry(CountryStore.country);
 *     }
 *   });
 *
 * The usage of `waitFor()` can be chained, for example:
 *
 *   FlightPriceStore.dispatchToken =
 *     flightDispatcher.register(function(payload) {
 *       switch (payload.actionType) {
 *         case 'country-update':
 *           flightDispatcher.waitFor([CityStore.dispatchToken]);
 *           FlightPriceStore.price =
 *             getFlightPriceStore(CountryStore.country, CityStore.city);
 *           break;
 *
 *         case 'city-update':
 *           FlightPriceStore.price =
 *             FlightPriceStore(CountryStore.country, CityStore.city);
 *           break;
 *     }
 *   });
 *
 * The `country-update` payload will be guaranteed to invoke the stores'
 * registered callbacks in order: `CountryStore`, `CityStore`, then
 * `FlightPriceStore`.
 */

  function Dispatcher() {
    this.$Dispatcher_callbacks = {};
    this.$Dispatcher_isPending = {};
    this.$Dispatcher_isHandled = {};
    this.$Dispatcher_isDispatching = false;
    this.$Dispatcher_pendingPayload = null;
  }

  /**
   * Registers a callback to be invoked with every dispatched payload. Returns
   * a token that can be used with `waitFor()`.
   *
   * @param {function} callback
   * @return {string}
   */
  Dispatcher.prototype.register=function(callback) {
    var id = _prefix + _lastID++;
    this.$Dispatcher_callbacks[id] = callback;
    return id;
  };

  /**
   * Removes a callback based on its token.
   *
   * @param {string} id
   */
  Dispatcher.prototype.unregister=function(id) {
    invariant(
      this.$Dispatcher_callbacks[id],
      'Dispatcher.unregister(...): `%s` does not map to a registered callback.',
      id
    );
    delete this.$Dispatcher_callbacks[id];
  };

  /**
   * Waits for the callbacks specified to be invoked before continuing execution
   * of the current callback. This method should only be used by a callback in
   * response to a dispatched payload.
   *
   * @param {array<string>} ids
   */
  Dispatcher.prototype.waitFor=function(ids) {
    invariant(
      this.$Dispatcher_isDispatching,
      'Dispatcher.waitFor(...): Must be invoked while dispatching.'
    );
    for (var ii = 0; ii < ids.length; ii++) {
      var id = ids[ii];
      if (this.$Dispatcher_isPending[id]) {
        invariant(
          this.$Dispatcher_isHandled[id],
          'Dispatcher.waitFor(...): Circular dependency detected while ' +
          'waiting for `%s`.',
          id
        );
        continue;
      }
      invariant(
        this.$Dispatcher_callbacks[id],
        'Dispatcher.waitFor(...): `%s` does not map to a registered callback.',
        id
      );
      this.$Dispatcher_invokeCallback(id);
    }
  };

  /**
   * Dispatches a payload to all registered callbacks.
   *
   * @param {object} payload
   */
  Dispatcher.prototype.dispatch=function(payload) {
    invariant(
      !this.$Dispatcher_isDispatching,
      'Dispatch.dispatch(...): Cannot dispatch in the middle of a dispatch.'
    );
    this.$Dispatcher_startDispatching(payload);
    try {
      for (var id in this.$Dispatcher_callbacks) {
        if (this.$Dispatcher_isPending[id]) {
          continue;
        }
        this.$Dispatcher_invokeCallback(id);
      }
    } finally {
      this.$Dispatcher_stopDispatching();
    }
  };

  /**
   * Is this Dispatcher currently dispatching.
   *
   * @return {boolean}
   */
  Dispatcher.prototype.isDispatching=function() {
    return this.$Dispatcher_isDispatching;
  };

  /**
   * Call the callback stored with the given id. Also do some internal
   * bookkeeping.
   *
   * @param {string} id
   * @internal
   */
  Dispatcher.prototype.$Dispatcher_invokeCallback=function(id) {
    this.$Dispatcher_isPending[id] = true;
    this.$Dispatcher_callbacks[id](this.$Dispatcher_pendingPayload);
    this.$Dispatcher_isHandled[id] = true;
  };

  /**
   * Set up bookkeeping needed when dispatching.
   *
   * @param {object} payload
   * @internal
   */
  Dispatcher.prototype.$Dispatcher_startDispatching=function(payload) {
    for (var id in this.$Dispatcher_callbacks) {
      this.$Dispatcher_isPending[id] = false;
      this.$Dispatcher_isHandled[id] = false;
    }
    this.$Dispatcher_pendingPayload = payload;
    this.$Dispatcher_isDispatching = true;
  };

  /**
   * Clear bookkeeping used for dispatching.
   *
   * @internal
   */
  Dispatcher.prototype.$Dispatcher_stopDispatching=function() {
    this.$Dispatcher_pendingPayload = null;
    this.$Dispatcher_isDispatching = false;
  };


module.exports = Dispatcher;

},{"./invariant":14}],14:[function(require,module,exports){
/**
 * Copyright (c) 2014, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 *
 * @providesModule invariant
 */

"use strict";

/**
 * Use invariant() to assert state which your program assumes to be true.
 *
 * Provide sprintf-style format (only %s is supported) and arguments
 * to provide information about what broke and what you were
 * expecting.
 *
 * The invariant message will be stripped in production, but the invariant
 * will remain to ensure logic does not differ in production.
 */

var invariant = function(condition, format, a, b, c, d, e, f) {
  if (false) {
    if (format === undefined) {
      throw new Error('invariant requires an error message argument');
    }
  }

  if (!condition) {
    var error;
    if (format === undefined) {
      error = new Error(
        'Minified exception occurred; use the non-minified dev environment ' +
        'for the full error message and additional helpful warnings.'
      );
    } else {
      var args = [a, b, c, d, e, f];
      var argIndex = 0;
      error = new Error(
        'Invariant Violation: ' +
        format.replace(/%s/g, function() { return args[argIndex++]; })
      );
    }

    error.framesToPop = 1; // we don't care about invariant's own frame
    throw error;
  }
};

module.exports = invariant;

},{}],"main":[function(require,module,exports){
var jquery = window.jQuery || require('jquery');

var userlist = require('../../../lib/admin/user/list');
var aria = require('../../../lib/aria');
var sc = require('../../../lib/simplecomponent');
// ENUM
var statusMap = null;

module.exports = {
    init: init
};

function init(config) {
    userlist.initStore(config.users);
    statusMap = Object.keys(config.status).reduce(function(prev, index) {
        var item = config.status[index];
        prev[item.name] = item.text;
        return prev;
    }, {});
    scanRoleButton(config.container);
    batchResetPassword();
}

function scanRoleButton(container) {
    jquery(container).on('click', '[role="button"]', onclick);
}

function onclick(e) {
    var $el = $(e.currentTarget);
    var label = $el.attr('aria-label');
    var action = userlist[label];
    if (typeof action !== 'function') {
        return;
    }
    var ctx = aria(e.currentTarget);
    ctx.username = $el.parents('tr').find('td').eq(2).text();
    ctx.statusMap = statusMap;
    ctx.setState = sc.setState;
    ctx.id = $el.data('id');
    ctx.el = e.currentTarget;
    action.call(ctx);
}

function batchResetPassword(){

    jquery('#allids').on("click", function(){
        var flag = this.checked;
        jquery('input[name="checkboxids"]').each(function(){
            this.checked = flag;
        });
    });

    jquery('input[name="checkboxids"]').on("click",function(){
        jquery('#allids').attr("checked", jquery('input[name="checkboxids"]').length === jquery('input[name="checkboxids"]:checked').length ? true : false);
    });

    jquery('#batchresetpassword').on('click', function(){
        var uri = '/admin/user/resetPassBySys';
        if (!window.confirm('确定要批量重置密码?')) {
            return;
        }
        var failMsg = '';
        var failed = 0;
        jquery('input[name="checkboxids"]:checked').each(function(){
            jquery.get(uri, { id: jquery(this).val() })
                .fail(function() {
                    failMsg = failMsg + ',' + jquery(this).parents('tr').find('td').eq(2).text();
                });
        });

        if(failed >0){
            window.alert(failMsg + "重置密码失败");
        }else{
            window.alert("批量重置密码全部成功");
        }

    });
}
},{"../../../lib/admin/user/list":3,"../../../lib/aria":10,"../../../lib/simplecomponent":11,"jquery":"jquery"}]},{},[]);
