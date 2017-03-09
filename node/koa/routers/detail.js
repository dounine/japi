    var Router = require('koa-router');
    var path = require('path');
    var sendfile = require('koa-sendfile');
    var server = require(path.resolve('koa/server/' + path.basename(__filename)));
    var commonSer = require(path.resolve('koa/server/common.js'));
    var config = require(path.resolve('plugins/read-config.js'));
    var fetch = require('node-fetch');//url转发
    module.exports = function(config){
        var router = new Router();
        router.get('/detail', function*(){
            yield (sendfile(this, path.resolve('detail.html')));
            if(!this.status){
                this.throw(404);
            }
        }).post('/detail', function*(){  //渲染导航nav
            var nav = this.request.body;
            var token = this.cookies.get('token');
            var $self = this;
            yield (server().sso(nav, token)
                .then((parsedBody) =>{
                    var responseText = JSON.parse(parsedBody);
                    $self.body = responseText;
                }).catch((error) =>{
                    if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                        $self.body = {'msg' : '请求错误！', errno : 3};
                        $self.status = 408;
                    }
                }));
        }).post('/versions', function*(){  //版本
            var token = this.cookies.get('token');
            var ver = this.request.body;
            var $self = this;
            yield (server().version(ver, token)
                .then((parsedBody) =>{
                    var responseText = JSON.parse(parsedBody);
                    $self.body = responseText;
                }).catch((error) =>{
                    if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                        $self.body = {'msg' : '请求错误！', errno : 3};
                        $self.status = 408;
                    }
                }));
        }).post('/date', function*(){  //时间
            var token = this.cookies.get('token');
            var verDate = this.request.body;
            var $self = this;
            yield (server().verDates(verDate, token)
                .then((parsedBody) =>{
                    var responseText = JSON.parse(parsedBody);
                    $self.body = responseText;
                }).catch((error) =>{
                    if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                        $self.body = {'msg' : '请求错误！', errno : 3};
                        $self.status = 408;
                    }
                }));
        }).post('/action', function*(){  //具体请求和相应参数
            var token = this.cookies.get('token');
            var verAction = this.request.body;
            var $self = this;
            yield (server().verActions(verAction, token)
                .then((parsedBody) =>{
                    var responseText = JSON.parse(parsedBody);
                    $self.body = responseText;
                }).catch((error) =>{
                    if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                        $self.body = {'msg' : '请求错误！', errno : 3};
                        $self.status = 408;
                    }
                }));
        }).get('/userlogout', function *(next){  //登出
            var token = this.cookies.get('token');
            var $self = this;
            yield (commonSer().logout(token)
                .then((parsedBody) =>{
                    var responseText = JSON.parse(parsedBody);
                    $self.body = responseText;
                    $self.cookies.set('token', '');
                    $self.cookies.set('username', '');
                    $self.cookies.set('password', '');
                    $self.cookies.set('readmemberMe', '');
                }).catch((error) =>{
                    if(error.error && error.error.code && error.error.code == 'ETIMEDOUT'){//登录超时
                        $self.body = {'msg' : '请求错误！', errno : 3};
                        $self.status = 408;
                    }
                }));
        })

        return router;
    };
