var request = require('request-promise');
var path = require('path');
var config = require(path.resolve('plugins/read-config.js'));
var form = require(path.resolve('plugins/form.js'));
module.exports = function(argvs, token){
    this.pagesList = function(token){
        var options = {
            method : 'GET',
            timeout : 3000,
            uri : config()['rurl'] + '/project/lists/1/8',
            // form: argvs,
            headers : {
                token : token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    this.captcha = function(argvs){
        var options = {
            method : 'get',
            timeout : 3000,
            uri : config()['rurl'] + 'project/' + argvs + '/logo',
            headers : {
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    this.sso = function(argvs, token){
        var rep = null;
        var options = {
            method : 'POST',
            timeout : 3000,
            uri : config()['rurl'] + '/project/navs',
            form : argvs,
            headers : {
                token : token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    this.version = function(argvs, token){
        var rep = null;
        var options = {
            method : 'POST',
            timeout : 3000,
            uri : config()['rurl'] + '/project/versions',
            form : argvs,
            headers : {
                token : token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    }
    this.verDates = function(argvs, token){
        var rep = null;
        var options = {
            method : 'POST',
            timeout : 3000,
            uri : config()['rurl'] + '/project/dates',
            form : argvs,
            headers : {
                token : token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    }
    this.verActions = function(argvs, token){
        var options = {
            method : 'POST',
            timeout : 3000,
            uri : config()['rurl'] + '/project/action',
            form : argvs,
            headers : {
                token : token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    }
    this.islogin = function(token){
        var options = {
            method : 'POST',
            timeout : 3000,
            uri : config()['rurl'] + '/user/isLogin',
            form : token,
            headers : {
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    return this;
}