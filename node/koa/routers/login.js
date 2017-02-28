const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/' + path.basename(__filename)));
const config = require(path.resolve('plugins/read-config.js'));
module.exports = function (config) {
    const router = new Router();
    router.get('/login', function *() {
        var stats = yield (sendfile(this,path.resolve('login.html')));
        if (!this.status) {
            this.throw(404);
        }
    }).post('/login',function*(){
        let user = this.request.body;
        let $self = this;
        yield (server().login(user)
            .then( (parsedBody)=> {
                let responseText = JSON.parse(parsedBody);
                if(responseText.code == 0){
                    var token = responseText.data;
                    $self.body = responseText;
                    $self.cookies.set('token', token,{maxAge: 24*60*1000});
                    $self.redirect('/list')
                }
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
    })

    return router;
};