const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/server.js'));
const commonSer = require(path.resolve('koa/server/common.js'));
var config = require(path.resolve('plugins/read-config.js'));
const fetch = require('node-fetch');//url转发
module.exports = function (config) {
    const router = new Router();
    router.get('/index',function*(){
         yield (sendfile(this,path.resolve('index.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).post('/index',function*(){  //渲染导航nav
        let nav = this.request.body;
        let token = this.cookies.get('token');
        let $self = this;
        yield (server().sso(nav,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).post('/versions',function*(){  //版本
        let token = this.cookies.get('token');
        let ver = this.request.body;
        let $self = this;
        yield (server().version(ver,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).post('/date',function*(){  //时间
        let token = this.cookies.get('token');
        let verDate = this.request.body;
        let $self = this;
        yield (server().verDates(verDate,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).post('/action',function*(){  //具体请求和相应参数
        let token = this.cookies.get('token');
        let verAction = this.request.body;
        let $self = this;
        yield (server().verActions(verAction,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).get('/logout',function *(next){  //登出
        let token = this.cookies.get('token');
        var $self = this;
        yield (commonSer().logout(token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
                $self.cookies.set('token',"");
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).get("/islogin",function*(){
        let token = this.cookies.get('token');
        let isToken={"token":token};
        let $self = this;
        yield (commonSer().islogin(isToken)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }))
    })

    return router;
};