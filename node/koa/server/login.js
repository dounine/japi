var request = require('request-promise');
var path = require('path');
var config = require(path.resolve('plugins/read-config.js'));
var form = require(path.resolve('plugins/form.js'));
module.exports = function(a){
    this.login = function(a){
        var rep = null;
        var options = {
            method : 'POST',
            timeout : 3000,
            uri : config()['rurl'] + '/user/login',
            form : a,
            headers : {
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };

    return this;
}