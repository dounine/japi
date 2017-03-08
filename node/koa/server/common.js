

var request = require('request-promise');
var path = require('path');
var config = require(path.resolve('plugins/read-config.js'));
var form = require(path.resolve('plugins/form.js'));
module.exports = function(argvs, token){

    //登出
    this.logout = function(token){
        var options = {
            method : 'GET',
            timeout : 3000,
            uri : config()['rurl'] + '/user/logout',
            // form: argvs,
            headers : {
                token : token
                /* 'content-type': 'application/x-www-form-urlencoded' */ // Set automatically
            }
        };
        return request(options);
    };
    //是否登录
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