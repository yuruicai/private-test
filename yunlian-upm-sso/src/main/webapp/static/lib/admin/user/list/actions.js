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
