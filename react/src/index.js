import React from 'react';
import ReactDOM from "react-dom";
import "./sass/index.scss";

let App = React.createClass({
    render:function(){
        return <div>
            <header>
                <div className="logo"><a href="/list"> <img src="./images/logo.png" alt=""/></a></div>
                <section>

                    <p className="head-l">
                        <a href="javascript:void(0)">接口测试</a>
                        <a href="javascript:void(0)">第三方接口测试</a>
                    </p>
                    <div className="head-r">
                        <div className="search">
                            <input type="text" />
                                <i></i>
                                <div className="secrchList">
                                </div>
                        </div>
                    </div>
                </section>

            </header>
            <div id="containal">
                <nav id="nav">
                    <div className="nav-head active">
                        <a href="javascript:void(0)" className="text">文档说明</a>
                        <i className="icon ac"></i>
                    </div>
                    <div className="nav-list">
                    </div>
                </nav>
                <div id="content">
                    <section className="section">
                        <div className="sec-head">
                            <h1>演示demo</h1>
                            <p>更新时间：<span className="time">2017-2-9</span></p>
                        </div>
                        <div className="basic-info">
                            <p>1.支持http、websocket测试</p>
                            <p>2.支持json，xml，txt，jsonp等测试</p>
                            <p>3.支持form-data，x-www-form-urlencoded ，raw，binary 上传格式</p>
                            <p>4.支持rest地址，http://www.test.com/test/.json 这样的地址会自动替换id</p>
                            <p>5.由于浏览器跨域访问限制，为了更好的体验服务，请下载安装扩展
                                <a href="javascript:void(0)">https://chrome.google.com/webstore/detail/%E5%B0%8F%E5%B9%BA%E9%B8%A1/omohfhadnbkakganodaofplinheljnbd</a>

                            </p>
                            <p>6.如果在使用过程中发现界面排版错误，请切换至chrome最新版本浏览器。其他浏览器，其他浏览器正在适配中。</p>
                            <p>7.如果配置了地址前缀，则该模块下所有url访问时会自动带上前缀。</p>
                            <p>8.如果有任何建议或意见都可以在这儿留言 http://www.xiaoyaoji.com.cn/help.html</p>
                            <p>9.有任何bug 都可以在这儿提出来 http://git.oschina.net/zhoujingjie/apiManager/issues</p>
                            <p>10.支持markdown编辑器</p>
                            <p>11.支持mock</p>
                            <p>12.支持变量</p>
                        </div>
                    </section>
                    <section className="section">
                        <div className="sec-head">
                            <h4> 请求参数</h4>
                        </div>
                        <div className="sec-table">
                            <div>
                                <ul className="sec-table-head">
                                    <li className="col-2">参数名称</li>
                                    <li className="col-1">是否必须</li>
                                    <li className="col-1">类型</li>
                                    <li className="col-6">描述</li>
                                </ul>
                                <div className="req">
                                    <div>
                                        <div className="sec-table-list">
                                            <ul>
                                                <li className="col-2">name</li>
                                                <li className="col-1">true</li>
                                                <li className="col-1">string</li>
                                                <li className=" col-6">测试1</li>
                                            </ul>
                                        </div>
                                        <div className="sec-table-list">
                                            <ul>
                                                <li className="col-2">key</li>
                                                <li className="col-1">true</li>
                                                <li className="col-1">int</li>
                                                <li className=" col-6">测试2</li>
                                            </ul>
                                        </div>
                                        <div className="sec-table-list">
                                            <ul>
                                                <li className="col-2">other</li>
                                                <li className="col-1">false</li>
                                                <li className="col-1">function</li>
                                                <li className=" col-6">测试3</li>
                                            </ul>
                                        </div>
                                        <div className="sec-table-list">
                                            <ul>
                                                <li className="col-2"><i className="icon-sub"></i> test</li>
                                                <li className="col-1">true</li>
                                                <li className="col-1">string</li>
                                                <li className=" col-6">测试4</li>
                                            </ul>
                                            <div className="sub">
                                                <ul>
                                                    <li className="col-2">test</li>
                                                    <li className="col-1">true</li>
                                                    <li className="col-1">string</li>
                                                    <li className=" col-6">测试4</li>
                                                </ul>
                                                <ul>
                                                    <li className="col-2">test</li>
                                                    <li className="col-1">true</li>
                                                    <li className="col-1">string</li>
                                                    <li className=" col-6">测试4</li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>

                </div>
            </div>
        </div>
    }
})
ReactDOM.render(<App/>,document.getElementById('app'))
