var request = require('request-promise');
var path = require('path');
var config = require(path.resolve('plugins/read-config.js'));
var form = require(path.resolve('plugins/form.js'));
module.exports = function (argvs,token) {
    this.version = function (argvs,token) {
        var rep = null;
        var options = {
            method: 'POST',
            timeout:3000,
            uri: config()['rurl']+'/project/versions',
            form: argvs,
            headers: {
                token:token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    }
    this.verDates = function (argvs,token) {
        var rep = null;
        var options = {
            method: 'POST',
            timeout:3000,
            uri: config()['rurl']+'/project/dates',
            form: argvs,
            headers: {
                token:token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    }
    this.verActions = function (argvs,token) {
        var options = {
            method: 'POST',
            timeout:3000,
            uri: config()['rurl']+'/project/action',
            form: argvs,
            headers: {
                token:token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    }
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