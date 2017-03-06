"use strict";
const Router = require('koa-router');
const path = require('path');
const sendfile = require('koa-sendfile');
const server = require(path.resolve('koa/server/server_ejs.js'));
const render = require('koa-ejs');
module.exports = function (config) {
    const router = new Router();
    router.get('/ejs/list',function*(next){
        let user = this.request.body;
        let $self = this;
        let responseText
        yield (server().sso(user)
            .then( (parsedBody)=> {
                responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {//登录超时
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
        yield this.render('list',{lists:responseText.list,layout:"template"});

    }).get('/ejs/index',function*(next){
        let user = this.request.body;
        let $self = this;
        let responseText,nav
        yield (server().sso(user)
            .then( (parsedBody)=> {
                responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                $self.body = responseText;
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));
        yield this.render('navs',{navs:responseText.nav,layout:"index"});
    }).post('/ejs/index',function*(next){
        let navInd = this.request.body;
        let $self = this;
        let responseText
        yield (server().version(navInd)
            .then( (parsedBody)=> {
                responseText = JSON.parse(parsedBody);
                // console.info("后台数据：",responseText);
                responseText.contDatas.forEach(function(item){
                    if(item.key==navInd.navInd){
                        responseText=item;
                        $self.body = responseText;
                        $self.render('version',{datas:responseText});
                        console.info(responseText);
                    }
                })
            }).catch((error)=> {
                if (error.error && error.error.code && error.error.code =='ETIMEDOUT') {
                    $self.body = {'msg':'请求错误！',errno:3};
                    $self.status = 408;
                }
            }));

    })
    return router;
};