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

