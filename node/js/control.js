var version = {};
//判断是否登录
function isLogin(){
    $.get("/islogin", function(data){
        if(data.data == true){
            return

        } else {
            location.href = "/login";
        }
    })
}

//刷新后回到页面
function refresh(){
    var refHashArr = location.hash.split("#")[1].split('/');
    if(refHashArr.length!=1){
        version.projectName=refHashArr[0];
        version.packageName = refHashArr[1];
        version.funName = refHashArr[2];
        version.actionName = refHashArr[3];
        version.versionName = refHashArr[4];
        version.dateName = refHashArr[5];

        $.ajax({
            type : "post",
            url : "/versions",
            data : version,
            success : function(data){

                var cont, vNum;

                cont = "<div><h3>基本信息</h3><div class='version'><p class='v-num'></p><p class='v-time'>更新时间：</p></div>";
                if(data.data.length == "1"){
                    vNum = "版本：<span class='version-list' data-value=%{version}>%{version}</span>".format({version : data.data})
                } else {
                    vNum = "版本：<select  class='version-list' onchange='verSel(version)'>";
                    $.each(data.data, function(index, version){
                        var hasStrs = location.hash.split("/");
                        var selectedStr = "";
                        if(hasStrs.length>1){
                            if(version == hasStrs[hasStrs.length-2]){
                                selectedStr = "selected='true'";
                            }
                        }
                        vNum += "<option value='%{version}' %{selected}>%{version}</option>".format({version : version,selected:selectedStr})
                    });
                    vNum += "</select>";
                }
                cont += "</div><section class='section info'></section><section class='section infodous'></section><section class='section reqtable'></section><section class='section restable'></section><section id='modal'>" +
                    "<div class='header'><h3>信息</h3><a href='javascript:void(0)' class='close' onclick='modalClose()'>关闭</a></div><div class='m-con'></div></section></div>"

                $("#content").html(cont);
                $(".v-num").html(vNum);
                version.dateName = "";
                verSel(version,true);
                $('.nav-list .rootName:contains('+ version.packageName +')').parent().siblings("ul").find(".subName:contains("+version.funName +")").parent().siblings(".nav-section").find("a:contains("+ version.actionName +")").parent().addClass('active');
                // $('.time option[value="'+ aa +'"]').attr("selected",true);
                $(".time option").each(function(){

                if($(this).text() === aa){
                    $(this).attr('selected', 'selected')
                }
            })
            }
        });

    }


}


$(document).ready(function(){
    isLogin()
    //请求导航，生成nav
    var navName = {"projectName" : location.hash.split("#")[1].split("/")[0]}
    $.ajax({
        type : "post",
        url : "/detail",
        data : navName,
        success : function(data){
            //遍历生成导航
            var nav = "<div>";
            $.each(data.data, function(index, item){
                for(var ind in item){
                    nav += "<div class='nav-section root'><span class='mainbav open'><a href='javascript:void(0)' class='rootName'>%{mainNav}</a></span><ul>".format({mainNav : item[ind].name});
                    for(var inx in item[ind].funs){
                        var twoNavs = item[ind].funs;
                        nav += "<li class='sub'><span class='mainbav open'><a href='javascript:void(0)' class='subName'>%{twoNav}</a></span><ul class='nav-section'>".format({twoNav : twoNavs[inx].name})
                        for(var i in twoNavs[inx].actions){
                            var listNavs = twoNavs[inx].actions
                            nav += "<li class='acLi'><a href='javascript:void(0)'>%{lastNav}</a></li>".format({lastNav : listNavs[i].name})
                        }
                        nav += "</ul></li>"
                    }
                    nav += "</div>"
                }
            });
            nav += "</div>"
            $("#nav .nav-list").append(nav);

        },
        error : function(request){
            console.info(request);
        }
    });

    userName()
    //关注列表
   followLists()

refresh()






    //导航点击
    $("#nav").on("click", ".nav-list .acLi a", function(){

        version.projectName = window.location.hash.split("#")[1].split("/")[0];
        version.packageName = $(this).parents('.root').find('.rootName').text();
        version.funName = $(this).parents('.sub').find('.subName').text();
        version.actionName = $(this).text();
        version.dateName = "";
        $.ajax({
            type : "post",
            url : "/versions",
            data : version,
            success : function(data){

                var cont, vNum;

                cont = "<div><h3>基本信息</h3><div class='version'><p class='v-num'></p><p class='v-time'>更新时间：</p></div>";
                if(data.data.length == "1"){
                    vNum = "版本：<span class='version-list' data-value=%{version}>%{version}</span>".format({version : data.data})
                } else {
                    vNum = "版本：<select  class='version-list' onchange='verSel(version)'>";
                    $.each(data.data, function(index, version){
                        var hasStrs = location.hash.split("/");
                        var selectedStr = "";
                        if(hasStrs.length>1){
                            if(version == hasStrs[hasStrs.length-2]){
                                selectedStr = "selected='true'";
                            }
                        }
                        vNum += "<option value='%{version}' %{selected}>%{version}</option>".format({version : version,selected:selectedStr})
                    });
                    vNum += "</select>";
                }
                cont += "</div><section class='section info'></section><section class='section infodous'></section><section class='section reqtable'></section><section class='section restable'></section><section id='modal'>" +
                    "<div class='header'><h3>信息</h3><a href='javascript:void(0)' class='close' onclick='modalClose()'>关闭</a></div><div class='m-con'></div></section></div>"

                $("#content").html(cont);
                $(".v-num").html(vNum)
                version.dateName = "";
                verSel(version);

            }
        })
    })
});


//版本选择
function verSel(version){
    var isRefresh = arguments[1];
    var verTag = document.getElementsByClassName('version-list')[0].tagName;
    if(verTag == "SPAN"){
        version.versionName = $('span.version-list').attr('data-value');
    } else if(verTag == "SELECT"){
        version.versionName = $('select.version-list option:selected').val();
    }
    $.ajax({
        type : "post",
        url : "/date",
        data : version,
        success : function(data){
            var verDate
            if(data.data.length == "1"){
                verDate = "<span class='time' data-index='%{verDates}' >%{verDates}</span>".format({verDates : data.data})

            } else {
                verDate = "<select onchange='action(version)' class='time'>";
                $.each(data.data, function(index, dateName){
                    var hasStrs = location.hash.split("/");
                    var selectedStr = "";
                    if(hasStrs.length>1&&isRefresh){
                        if(dateName == hasStrs[hasStrs.length-1]){
                            selectedStr = "selected='true'";
                        }
                    }
                    verDate += "<option value='%{dateName}' %{selected}>%{dateName}</option>".format({dateName : dateName,selected:selectedStr})
                });
                verDate += "</select>";
            }

            $('.v-time').html(verDate);
            action(version);
        }
    })
}


var rootShowMySelf = new Object();
var rootShowMySelfRes = new Object();
var responseFields1 = null;

//参数

function action(version){
    var actTag = document.getElementsByClassName('time')[0].tagName;
    if(actTag == "SPAN"){
        version.dateName = $('span.time').attr('data-index')
    } else if(actTag == "SELECT"){
        version.dateName = $('select.time option:selected').val();
    }
    $.ajax({
        type : "post",
        url : "/action",
        data : version,
        success : function(res){
            var resData = res.data;
            var info = "<div class='basic-info'><div class='item urls'>";
            var actInfoUrls = resData.actionInfoRequest.urls;
            var actInfoMethods = resData.actionInfoRequest.methods;
            var actionInfoDocs = resData.actionInfoDocs;
            console.info(resData);
            if(actInfoUrls.length == "1"){
                info += "<span class='urls-select' data-value='%{url}'>%{url}</span>".format({url : actInfoUrls})
            } else {
                info += "<select class='urls-select'>"
                $.each(actInfoUrls, function(i, t){
                    info += "<option>" + t + "</option>"
                });
                info += "</select>"
            }

            info += ("<span class='method'><strong></strong></span>" +
            "<a href='javascript:void(0)' class='copy' onclick='copy()'><img src='/images/copy.png' alt=''></a><span class='copysuc'>复制成功</span>");
            info += "</div><div class='item extype'></div></div>";


            var infoDocs="<ul>";
            $.each(actionInfoDocs,function(index,item){
                infoDocs+="<li>";
                infoDocs+="<p><span>%{tagName}</span>：<span>%{tagval}</span></p>".format({tagName:item.tagName,tagval:item.tagValue})
                infoDocs+="</li>"
            })
            infoDocs+="</ul>"

            /*******请求参数********/

            var requestFields = "<div class='sec-head'><h4>请求参数</h4></div><div class='sec-table'><div class='sec-table-wrap'>" +
                "<ul class='sec-table-head'><li class='col-2'>参数名称</li><li class='col-1'>是否必须</li><li class='col-1'>类型</li>" +
                "<li class='col-1'>默认值</li><li class='col-2'>约束</li><li class='col-6'>描述</li></ul><div id='req'>";

            var reqArr = [];
            $.each(resData.requestFields, function(index, item){
                if(item.fields){
                    for(var i in item.fields){
                        var obj = item.fields[i];
                        if(obj.type == '$this'){
                            obj._parent = item;
                            obj.ac = "ac"
                        }
                        reqArr.push(obj);
                    }
                } else {
                    reqArr.push(item);

                }
            })

            var level = new Object();
            level.indent = 4;
            level.count = 0;
            level.datas = new Object();
            subObj(reqArr, level, "");
            for(var subIndex in level.datas){
                var _parent = level.datas[subIndex]['_parent'];
                var _parentClickEventStr = "";
                if(_parent){
                    rootShowMySelf[subIndex] = _parent;
                    _parentClickEventStr = "onclick=_parent(this," + subIndex + ")";
                    level.datas[subIndex]['type'] = 'object';
                }
                level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"] + 2;
                requestFields += ("<div class='sec-table-list %{_open}' index='%{_index}'> <ul>" +
                "<li class='col-2'><i onclick='iconSubClick(this)' class='%{_sub}' style='left:%{_indent}px'></i> <span class='itemName' " + _parentClickEventStr + "  data-ac='%{ac}'  style='text-indent:%{_indent1}px'>%{name}</span></li>" +
                "<li class='col-1'>%{required}</li><li class='col-1'>%{type}</li>" +
                "<li class='col-1'>%{defaultValue}</li><li class='col-2'>%{constraint}</li><li class=' col-6'>%{description}</li></ul></div>").format(level.datas[subIndex])
            }

            requestFields += "</div></div></div>";

            /*****响应参数*****/

            var responseFields = "<div class='sec-head'><h4>响应参数</h4></div><div class='sec-table'><div class='sec-table-wrap'>" +
                "<ul class='sec-table-head'><li class='col-2'>参数名称</li><li class='col-1'>类型</li>" +
                "<li class='col-1'>默认值</li><li class='col-6'>描述</li></ul><div id='res'>";

            $.each(resData.responseFields, function(index, obj){
                if(obj.type == '$this'){
                    obj._parent = {'name' : 'not-null'};
                    obj.ac = "ac";
                }

            })

            var level = new Object();
            level.indent = 4;
            level.count = 0;
            level.datas = new Object();
            subObj(resData.responseFields, level, "");
            responseFields1 = resData.responseFields;
            for(var subIndex in level.datas){
                var _parent = level.datas[subIndex]['_parent'];
                var _parentClickEventStr = "";
                if(_parent){
                    rootShowMySelfRes[subIndex] = _parent;
                    _parentClickEventStr = "onclick=_parentRes(this," + subIndex + ")";
                    level.datas[subIndex]['type'] = 'object';
                }
                level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"] + 2;
                responseFields += ("<div class='sec-table-list %{_open}' index='%{_index}'> <ul>" +
                "<li class='col-2'><i onclick='iconSubClick(this)' class='%{_sub}' style='left:%{_indent}px'></i> <span class='itemName' " + _parentClickEventStr + "  data-ac='%{ac}'  style='text-indent:%{_indent1}px'>%{name}</span></li>" +
                "<li class='col-1'>%{type}</li>" +
                "<li class='col-1'>%{defaultValue}</li><li class=' col-6'>%{description}</li></ul></div>").format(level.datas[subIndex])
            }

            responseFields += "</div></div></div>";

            $('.section.info').html(info);
            var thod = actInfoMethods.join(" / ");
            $('.method strong').text(thod)
            $('.section.reqtable').html(requestFields);
            $('.section.restable').html(responseFields);
            $('.section.infodous').html(infoDocs);
            var hashArr=[];
            $.each(version,function(i,t){
                hashArr.push(t)
            });
            var newHash=hashArr.join('/');
            location.hash=newHash
        }
    })


}


function _parent(self, _parent){

    $('#modal').show();
    $('#modalbg').show()
    var modalCon = "<div class='sec-table'><div class='sec-table-wrap'>" +
        "<ul class='sec-table-head'><li class='col-2'>参数名称</li><li class='col-1'>是否必须</li><li class='col-1'>类型</li>" +
        "<li class='col-1'>默认值</li><li class='col-2'>约束</li><li class='col-6'>描述</li></ul>";


    var level = new Object();
    level.indent = 4;
    level.count = 0;
    level.datas = new Object();
    subObj(rootShowMySelf[_parent].fields, level, "");
    for(var subIndex in level.datas){
        level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"] + 2;
        modalCon += ("<div class='sec-table-list' index='%{_index}'> <ul>" +
        "<li class='col-2'><i class='%{_sub}' style='left:%{_indent}px'></i><span style='text-indent:%{_indent1}px'>%{name}</span></li>" +
        "<li class='col-1'>%{required}</li><li class='col-1'>%{type}</li>" +
        "<li class='col-1'>%{defaultValue}</li><li class='col-2'>%{constraint}</li><li class=' col-6'>%{description}</li></ul></div>").format(level.datas[subIndex])
    }

    modalCon += "</div></div>";

    $('.m-con').html(modalCon)
}

function _parentRes(self, _parent){
    $('#modal').show();
    $('#modalbg').show();

    var modalCon = "<div class='sec-table'><div class='sec-table-wrap'>" +
        "<ul class='sec-table-head'><li class='col-2'>参数名称</li><li class='col-1'>类型</li>" +
        "<li class='col-1'>默认值</li><li class='col-6'>描述</li></ul>";

    var item = responseFields1;

    var level = new Object();
    level.indent = 4;
    level.count = 0;
    level.datas = new Object();
    subObj(item, level, "");
    for(var subIndex in level.datas){
        level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"] + 4;
        modalCon += ("<div class='sec-table-list' index='%{_index}'> <ul>" +
        "<li class='col-2'><i class='%{_sub}' style='left:%{_indent}px'></i><span style='text-indent:%{_indent1}px'>%{name}</span></li>" +
        "<li class='col-1'>%{type}</li>" +
        "<li class='col-1'>%{defaultValue}</li><li class=' col-6'>%{description}</li></ul></div>").format(level.datas[subIndex])
    }

    modalCon += "</div></div>";

    $('.m-con').html(modalCon)
}


//递归
function subObj(arr, level, tmpIndex){
    var indent = level.indent;

    for(var obj in arr){
        var _obj = arr[obj];
        level.count += 1;
        var _count = level.count;
        level.datas[level.count] = new Object();
        level.datas[level.count]["_indent"] = indent;
        level.datas[level.count]["_sub"] = "";
        if(tmpIndex == ""){
            level.datas[level.count]["_index"] = obj + "";
        } else {
            level.datas[level.count]["_index"] = tmpIndex + "-" + obj;
        }

        for(var name in _obj){
            if(is_array(_obj[name])){
                level.indent += 10;
                level.datas[level.count]["_sub"] = "icon-sub";
                level.datas[level.count]["_open"] = "isOpen";
                subObj(_obj[name], level, level.datas[level.count]["_index"]);
                level.str = 0;
                level.indent = 10;
            } else {
                level.datas[_count][name] = _obj[name];
            }
        }

    }
}
var is_array = function(value){
    return value &&
        typeof value === 'object' &&
        typeof value.length === 'number' &&
        typeof value.splice === 'function' && !(value.propertyIsEnumerable('length'));
};

function delFollow(_this){
    var data = {};
    data.projectName = $(_this).siblings('.followName').text();
    $.ajax({
        type : "POST",
        url : "/delFollow",
        data : data,
        success : function(data){
            var proName = $(_this).parent().children('.followName').text()
            $(_this).parent().remove();
        },
        error : function(data){
            console.info(data);
        }
    })
}

function folSort(self){
    var name = $(self).attr("class");
    var li = $(self).parents('li');
    var sortData={};
    if(name=="next"&&li.next()){
        li.next().after(li)
    }else if(name=="prev"&&li.prev()){
        li.prev().before(li);
    }
    var listSort=[];
    for(var i=0,len=$('.user-list li').length; i<len; i++){
        var listName = $('.user-list li').eq(i).children('.followName').text();
        listSort.push(listName)
    }
    sortData.projects = listSort.join(',');
    $.ajax({
        type:"post",
        url:"/sortList",
        async:false,
        data:sortData,
        success:function(data){
        },
        error:function(data){
        }
    })
}

function user(self){
    if(!$(self).hasClass('ac')){
        $(self).addClass('ac');
        $('.user-list').show();

    }else {
        $(self).removeClass('ac');
        $('.user-list').hide()
    }
}

function followLists(){
    $.ajax({
        type:"GET",
        async:false,
        url:"/followList",
        success:function(data){
            var followList="<ul>"
            $.each(data.data,function(index,item){
                followList+=("<li><span class='sort'><a href='javascript:void(0)' onclick='folSort(this)' class='prev'><img src='/images/up.png' alt=''></a>" +
                "<a href='javascript:void(0)' class='next' onclick='folSort(this)'><img src='/images/down.png' alt=''></a></span>" +
                "<a href='/detail#%{followName}' onclick='listJump(this)' class='followName'>%{followName}</a><a href='javascript:void(0)' class='del-fol' onclick='delFollow(this)'>" +
                "<img src='/images/pro_close.png' alt=''></a></li>").format({followName:item})
            });
            followList+="</ul>";
            $('.user-list').html(followList)

        },
        error:function(data){
            console.info(data);
        }
    })
}

function listJump(self){

    window.location.reload();

}

//读取用户名
function userName(){
    var userName = $.cookie("username");
    $('.user strong').text(userName)
}