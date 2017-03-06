

var Router = require('koa-router');
var path = require('path');
var sendfile = require('koa-sendfile');
var commonSer = require(path.resolve('koa/server/common.js'));
var server = require(path.resolve('koa/server/' + path.basename(__filename)));
var config = require(path.resolve('plugins/read-config.js'));
var fetch = require('node-fetch');
module.exports = function(config){
    var router = new Router();
    //获取列表页面
    router.get('/index', function *(){
        yield (sendfile(this, path.resolve('index.html')));
        if(!this.status){
            this.throw(404);
        }
    }).post('/lists', function*(){
        var token = this.cookies.get('token');
        var $self = this;
        var pages = this.request.body.pageSize
        yield (server().pagesList(pages, token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;

            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    }).get('/logo/:projectName', function *(next){
        var token = this.cookies.get('token');
        var $self = this;
        yield (fetch('http://192.168.0.121:8080/project/' + this.params.projectName + '/logo', {
            method : 'GET',
            headers : {'token' : token}
        })
            .then(function(res){
                return res.buffer();
            }).then(function(buffer){
                $self.set('content-type', 'image/png');
                $self.body = buffer;
            }));
    }).post('/pageSize', function*(){
        var token = this.cookies.get('token');
        var page = this.request.body.pageSize;
        var $self = this;
        yield (server().pageSizes(token, page)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    }).get('/sizes', function*(){
        var token = this.cookies.get('token');
        var $self = this;
        yield (server().sizes(token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    }).get('/logout', function *(next){  //登出
        var token = this.cookies.get('token');
        var $self = this;
        yield (commonSer().logout(token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
                $self.cookies.set('token', "");
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    }).get("/islogin", function*(){
        var token = this.cookies.get('token');
        var isToken = {"token" : token};
        var $self = this;
        yield (commonSer().islogin(isToken)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }))
    }).post("/addFollow", function*(){
        var token = this.cookies.get('token');
        var adFollow = this.request.body;
        var $self = this;
        yield (server().addFollow(adFollow,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }))
    }).post("/delFollow", function*(){
        var token = this.cookies.get('token');
        var delFollow = this.request.body.projectName;
        var $self = this;
        yield (server().deleteFollow(delFollow,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    }).get("/followList", function*(){
        var token = this.cookies.get('token');

        var $self = this;
        yield (server().followList(token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    }).post("/sortList", function*(){
        var token = this.cookies.get('token');
        var sortName = this.request.body;
        console.info(sortName);
        var $self = this;
        yield (server().sortList(sortName,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                    $self.body = {'msg' : '请求错误！', errno : 3};
                    $self.status = 408;
                }
            }));
    });

    return router;
};