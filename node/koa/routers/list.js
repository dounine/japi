const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const commonSer = require(path.resolve('koa/server/common.js'));
const server = require(path.resolve('koa/server/' + path.basename(__filename)));
const config = require(path.resolve('plugins/read-config.js'));
const fetch = require('node-fetch');
module.exports = function (config) {
    const router = new Router();
    //获取列表页面
    router.get('/list', function *() {
        yield (sendfile(this,path.resolve('list.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).get('/lists',function*(){
        let token = this.cookies.get('token');
        let $self = this;
        yield (server().pagesList(token)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).get('/logo/:projectName', function *(next){
        let token = this.cookies.get('token');
        var $self = this;
        yield (fetch('http://192.168.0.121:8080/project/'+this.params.projectName+'/logo',{
            method:'GET',
            headers: { 'token': token }
        })
            .then(function(res) {
                return res.buffer();
            }).then(function(buffer) {
                $self.set('content-type','image/png');
                $self.body = buffer;
            }));
    }).post('/pageSize',function*(){
        let token = this.cookies.get('token');
        let page = this.request.body.pageSize;
        let $self = this;
        yield (server().pageSizes(token,page)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    }).get('/sizes',function*(){
        let token = this.cookies.get('token');
        let $self = this;
        yield (server().sizes(token)
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
    });

    return router;
};