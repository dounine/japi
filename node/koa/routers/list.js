const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/' + path.basename(__filename)));
const config = require(path.resolve('plugins/read-config.js'));
const fetch = require('node-fetch');
module.exports = function (config) {
    const router = new Router();
    router.get('/list', function *() {
        var token = this.cookies.get('token');
        if(!token){
            this.redirect('/login')
        }else {
            var stats = yield (sendfile(this,path.resolve('list.html')));
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
    }).post('/pages',function*(){
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
    })

    return router;
};