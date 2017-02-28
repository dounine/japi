const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/server.js'));
var config = require(path.resolve('plugins/read-config.js'));
const fetch = require('node-fetch');//url转发
module.exports = function (config) {
    const router = new Router();
    router.get('/login', function *() {
        var stats = yield (sendfile(this,path.resolve('login.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).post('login',function*(){

        let user = this.request.body;
        let $self = this;
    }).get('/list', function *() {
        var stats = yield (sendfile(this,path.resolve('list.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).get('/lists',function*(){
        let token = this.cookies.get('token');
        let $self = this;
        yield (server().pagesList(token)
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
    }).get('/logo/:projectName', function *(next){

        console.info(token);
        var $self = this;
         yield (fetch('http://192.168.0.121:8080/project/'+this.params.projectName+'/logo')
            .then(function(res) {
                return res.buffer();
            }).then(function(buffer) {
                $self.set('content-type','image/png');
                $self.body = buffer;
            }));
    }).get('/index',function*(){
        yield (sendfile(this,path.resolve('index.html')));
        if (!this.status) {
            this.throw(404);
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
    })

    return router;
};