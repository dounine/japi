const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/server.js'));

module.exports = function (config) {
    const router = new Router();
    router.get('/list', function *() {
        var stats = yield (sendfile(this,path.resolve('list.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).post('/list',function*(){
        let user = this.request.body;
        let $self = this;
        yield (server().sso(user)
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
    }).get('/index',function*(){
        yield (sendfile(this,path.resolve('index.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).post('/index',function*(){
        let user = this.request.body;
        let $self = this;
        yield (server().sso(user)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                console.info("后台数据：",responseText);
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