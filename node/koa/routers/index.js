
var Router = require('koa-router');
var path = require('path');
var sendfile = require('koa-sendfile');
var commonSer = require(path.resolve('koa/server/common.js'));
var server = require(path.resolve('koa/server/' + path.basename(__filename)));
var config = require(path.resolve('plugins/read-config.js'));
var fetch = require('node-fetch');
module.exports = function(){
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
        var pages = this.request.body.pageSize;
        var searchKey = this.request.body.searchKey || '';
        yield (server().pagesList(pages,searchKey, token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;

            }).catch((error) =>{
                $self.body = {'msg' : error.error, errno : 3};
            }));
    }).get('/logo/:projectName', function *(next){
        var token = this.cookies.get('token');
        var $self = this;
        yield (fetch(config()['rurl']+'/project/' + this.params.projectName + '/logo', {
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
        var page = this.request.body.limit;
        var searchKey = this.request.body.searchKey || '';
        var $self = this;
        yield (server().pageSizes(token,searchKey, page)
            .then((parsedBody) =>{
            var responseText = JSON.parse(parsedBody);
        $self.body = responseText;
    }).catch((error) =>{
            $self.body = {'msg' : error.error, errno : 3};
    }));
    }).get('/sizes', function*(){
        var token = this.cookies.get('token');
        var searchKey = this.request.query.searchKey || ''
        var $self = this;
        yield (server().sizes(searchKey,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                $self.body = {'msg' : error.error, errno : 3};
            }));
    }).post("/addFollow", function*(){
        var token = this.cookies.get('token');
        var adFollow = this.request.body;
        var $self = this;
        yield (server().addFollow(adFollow,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                $self.body = {'msg' : error.error, errno : 3};
            }))
    }).post("/delFollow", function*(){
        var token = this.cookies.get('token');
        var delFollow = this.request.body;
        var $self = this;
yield (server().deleteFollow(delFollow,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                $self.body = {'msg' : error.error, errno : 3};
            }));
    }).get("/followList", function*(){
        var token = this.cookies.get('token');

        var $self = this;
        yield (server().followList(token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                $self.body = {'msg' : error.error, errno : 3};
            }));
    }).post("/sortList", function*(){
        var token = this.cookies.get('token');
        var sortName = this.request.body;
        var $self = this;
        yield (server().sortList(sortName,token)
            .then((parsedBody) =>{
                var responseText = JSON.parse(parsedBody);
                $self.body = responseText;
            }).catch((error) =>{
                $self.body = {'msg' : error.error, errno : 3};
            }));
    });

    return router;
};
