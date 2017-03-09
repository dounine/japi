const app = require('koa')();//koa web应用
const path = require('path');//路径
const router = require("koa-router")();//路由中间件
const session = require('koa-session');//cookie
const koaBody = require('koa-body');
const json = require('koa-json');
const cors = require('koa-cors');
const staticCache = require('koa-static-cache');
const config = require(path.resolve('plugins/read-config.js'));
const routersPath = '/koa/routers/';

app.use(cors());//跨域请求,用于与browsesync调试
app.keys = ['feedback'];//session加密值
app.use(session(app));//使用cookie
app.use(koaBody());//必需要路由用之前使用,不然获取不到表单
router.get('/', function *(next) {//根路由
    this.redirect('/login');//重写向到登录页面
    this.status = 301;
});

//============路由===========
app.use(require(path.join(__dirname,routersPath,'login.js'))().routes());//登录路由
app.use(require(path.join(__dirname,routersPath,'detail.js'))().routes());//api路由
app.use(require(path.join(__dirname,routersPath,'index.js'))().routes());//列表路由
app.use(router.routes());

//============静态文件资源===========
app.use(staticCache(path.join(__dirname, './'), {
    maxAge: 365 * 24 * 60 * 60
}))
app.listen(config()['port'], function () {
    console.log('koa server listening on port ' + config()['port']);
});