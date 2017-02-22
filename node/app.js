const app = require('koa')();//koa web应用
const path = require('path');//路径
const router = require("koa-router")();//路由中间件
const serve = require('koa-static-server');
const session = require('koa-session');//cookie
const koaBody = require('koa-body');
const json = require('koa-json');
const cors = require('koa-cors');
const render = require('koa-ejs');
const EJS = require('ejs')
const port = 7777;
app.use(cors());//跨域请求,用于与browsesync调试
app.keys = ['feedback'];//session加密值
app.use(session(app));//使用cookie
app.use(koaBody());//必需要路由用之前使用,不然获取不到表单
router.get('/', function *(next) {//根路由
    this.redirect('/list');//重写向到列表页面
    this.status = 301;
});

render(app, {
    root: path.join(__dirname, 'views'),
    layout: '',
    viewExt: 'html',
    cache: false,
    debug: true
});

//============路由===========
app.use(require(path.join(__dirname,'koa/routers/routers.js'))().routes());
app.use(require(path.join(__dirname,'koa/routers/router_ejs.js'))().routes());
app.use(router.routes());




//============静态文件资源===========
app.use(serve({rootDir: './'}));

app.listen(port, function () {
    console.log('koa server listening on port ' + port);
});