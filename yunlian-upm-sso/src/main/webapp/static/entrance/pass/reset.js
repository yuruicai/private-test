var modpass = require('../user/modpass');


module.exports = exports = create(modpass);


function F() {}
function create(proto) {
    F.prototype = proto;
    return new F();
}

