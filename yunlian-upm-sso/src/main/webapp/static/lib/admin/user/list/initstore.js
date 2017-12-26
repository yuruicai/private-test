var actions = require('./actions');

module.exports = function(users) {
    actions.initUserStatusStore(users);
};
