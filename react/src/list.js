import React from "react";
import ReactDom from "react-dom";
import "./sass/list.scss";
const datas =  require("./data/data.json");
const App = React.createClass({
    getDefaultProps:function(){
        return {
            datas : datas
        }
    },
    render:function(){
        let listData = this.props.datas.list;
        console.info(listData);
        return <div>
            <header>
                <div className="logo"><a href="javascript:void(0)"> <img src="/src/images/logo.png" alt=""/></a></div>
                <section>
                    <div className="search">
                        <input type="text"/>
                            <i></i>
                    </div>
                </section>
            </header>
            <div id="containal">
                <div className="item-list">
                    {
                        listData.map((list)=>{
                            return <div className="item" key={list.name}>
                                <div className="icon">
                                    <a href="/index"><img src={list.icon}/></a>
                                </div>
                                <div className="item-title"><strong>{list.name}</strong></div>
                                <div className="item-info">
                                    <span className="version">{list.version}</span>
                                    <span className="time">{list.time}</span>
                                </div>
                                <div className="detailed">
                                    <a href={list.url}>文档</a></div>
                            </div>
                        })
                    }
                </div>
            </div>
            <div id="pages">
                <a href="javascript:void(0)">首页</a>
                <a href="javascript:void(0)">上一页</a>
                <a href="javascript:void(0)">下一页</a>
                <a href="javascript:void(0)">尾页</a>
            </div>
        </div>
    }
});
ReactDom.render(<App/>,document.getElementById('app'));