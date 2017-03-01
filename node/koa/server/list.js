var request = require('request-promise');
var path = require('path');
var config = require(path.resolve('plugins/read-config.js'));
var form = require(path.resolve('plugins/form.js'));
module.exports = function (a) {
    this.pagesList = function (token) {
        var options = {
            method: 'GET',
            timeout:3000,
            uri: config()['rurl']+'/project/lists/1/8',
            // form: argvs,
            headers: {
                token:token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    this.captcha = function (argvs) {
        var options = {
            method: 'get',
            timeout:3000,
            uri: config()['rurl']+'project/'+argvs+'/logo',
            headers: {
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    this.logout = function (token) {
        var options = {
            method: 'GET',
            timeout:3000,
            uri: config()['rurl']+'user/logout',
            // form: argvs,
            headers: {
                token:token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };

    return this;
}