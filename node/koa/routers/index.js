const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/server.js'));
var config = require(path.resolve('plugins/read-config.js'));
const fetch = require('node-fetch');//url转发
module.exports = function (config) {
    const router = new Router();
    router.get('/index',function*(){
        var token = this.cookies.get('token');
        if(!token){
            this.redirect('/login')
        }else {
             yield (sendfile(this,path.resolve('index.html')));
        }
    }).post('/index',function*(){
        let nav = this.request.body;
        let token = this.cookies.get('token');
        let $self = this;
        yield (server().sso(nav,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).post('/versions',function*(){
        let token = this.cookies.get('token');
        let ver = this.request.body;
        console.info(ver);
        let $self = this;
        yield (server().version(ver,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).post('/date',function*(){
        let token = this.cookies.get('token');
        let verDate = this.request.body;
        let $self = this;
        yield (server().verDates(verDate,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).post('/action',function*(){
        let token = this.cookies.get('token');
        let verAction = this.request.body;
        let $self = this;
        yield (server().verActions(verAction,token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).get('/logout',function *(next){
        let token = this.cookies.get('token');
        console.info(token);
        var $self = this;
        yield (server().logout(token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
                console.info(responseText);
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    })

    return router;
};