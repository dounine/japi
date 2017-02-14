$(document).ready(function(){
    $.ajax({
        type:"get",
        url:"/data/data.json",
        success:function(data){
            //遍历生成导航

            $.each(data.nav,function(ind,item){
                var mainbav = "<div class='nav-section'><span class='mainbav open'><a href='javascript:void(0)'> " +
                    item.mainNav+"</a></span><ul>";
                for(var index in item.subNav){
                    mainbav+="<li><a href='javascript:void(0)' data-key='json'>%{navList}</a></li>".format({navList:item.subNav[index]})
                }
                mainbav+="</ul></div>";
                $("#nav .nav-list").append(mainbav);
            });

        },
        error: function(request) {
            console.info(request);
        }
    });

    //导航点击
    $("#nav").on("click",".nav-list li a",function(){
        var cont;
        var datakey = $(this).attr("data-key");
        $.get("/data/data.json",function(data){
            cont="<div><h4>基本信息</h4><div class='version'>";
            $.each(data.contDatas,function(ind,item){
                if(item.key==datakey){
                    newDatas = item.version;
                    cont+="<p>版本：<span class='verNo'></span><select class='version-list' onchange='verSel()'>";
                    urls = "接口地址：<select class='api-select'>"
                    $.each(newDatas,function(i,t){
                        newtime=t.datetime;
                        cont += "<option value=%{verVal}>%{verMsg}</option>".format({verVal:t.name,verMsg:t.msg});
                    });
                    cont+="</select></p><p>更新时间：<span class='time'>"+newtime+"</span></p>";

                }
            })
            cont+="</div><section class='section'><div class='basic-info'><div class='item urls'></div><div class='item extype'></div></div></section>" +
                "<section class='section'><div class='sec-head'><h4> 请求参数</h4></div> " +
                "<div class='sec-table'><div class='sec-table-wrap'><ul class='sec-table-head'>" +
                "<li class='col-2'>参数名称</li><li class='col-1'>是否必须</li><li class='col-1'>类型</li><li class='col-1'>默认值</li><li class='col-6'>描述</li>" +
                "</ul><div id='req'></div></div></div> </section>" +

                "<section class='section'><div class='sec-head'><h4> 响应参数</h4></div> " +
                "<div class='sec-table'><div class='sec-table-wrap'><ul class='sec-table-head'>" +
                "<li class='col-2'>参数名称</li><li class='col-1'>类型</li><li class='col-1'>默认值</li><li class='col-6'>描述</li>" +
                "</ul><div id='res'></div></div></div> </section>" +
                "</div>";
            $("#content").html(cont);
            verSel()
        })
    });

});
var version,newtime,urls,extype,reqParm,resParm,newDatas;
var msg = "";
var is_array = function(value) {
    return value &&
        typeof value === 'object' &&
        typeof value.length === 'number' &&
        typeof value.splice === 'function' &&
        !(value.propertyIsEnumerable('length'));
};
function verSel(){
    var versionSelect = $('.version-list option:selected').val();
    extype="<div>";
    reqParm="<div >";
    resParm="<div >";
    urls="<select class='urls-select' onchange='urlSel()'>"
    $.each(newDatas,function(i,t){
        if(t.name==versionSelect){
            $('.time').html(t.datetime);
            for(var idx in t.urls){
                urls+="<option value=%{urlVal} data-method=%{dataMethod}>%{urlVal}</option>".format({urlVal:t.urls[idx].url,dataMethod:t.urls[idx].method})
            }
            for(var idx in t.ex){
                extype+="<p>%{exName}:<span>%{exVal}</span></p>".format({exName:t.ex[idx].name,exVal:t.ex[idx].value})
            }
            var subReqData ,subResData;
            var level = new Object();
            level.indent = 0;
            level.count = 0;
            level.parent = "";
            level.datas = new Object();
            subObj(t.requestParms,level);

            for(var subIndex in level.datas){
                // console.info(level.datas[subIndex]);
                level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"]+2;
                reqParm+="<div class='sec-table-list %{_open}' parent='%{_parent}'> <ul><li class='col-2'><i class='%{_sub}' style='left:%{_indent}px'></i> <span style='text-indent:%{_indent}px'>%{requestName}</span></li><li class='col-1'>%{required}</li><li class='col-1'>%{datatype}</li><li class='col-1'>%{default}</li><li class=' col-6'>%{des}</li></ul></div>".format(level.datas[subIndex])
                // reqParm+="<div class='sec-table-list'> ";
                // if(level.datas[subIndex]._sub="icon-sub"){
                //     reqParm+= "<ul><li class='col-2'><i class='%{_sub}'></i> %{_str}%{requestName}</li><li class='col-1'>%{required}</li><li class='col-1'>%{datatype}</li><li class=' col-6'>%{des}</li></ul>".format(level.datas[subIndex])
                // }
                // reqParm+="</div>"
            }

            var level = new Object();
            level.indent = 0;
            level.count = 0;
            level.parent = "";
            level.datas = new Object();
            subObj(t.responseParms,level);
            for(var subIndex in level.datas){
                // console.info(level.datas[subIndex]);
                level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"]+2;
                resParm+="<div class='sec-table-list %{_open}' parent='%{_parent}'> <ul><li class='col-2'><i class='%{_sub}' style='left:%{_indent}px'></i> <span style='text-indent:%{_indent}px'>%{responseName}</span></li><li class='col-1'>%{datatype}</li><li class='col-1'>%{default}</li><li class=' col-6'>%{des}</li></ul></div>".format(level.datas[subIndex])
                // reqParm+="<div class='sec-table-list'> ";
                // if(level.datas[subIndex]._sub="icon-sub"){
                //     reqParm+= "<ul><li class='col-2'><i class='%{_sub}'></i> %{_str}%{requestName}</li><li class='col-1'>%{required}</li><li class='col-1'>%{datatype}</li><li class=' col-6'>%{des}</li></ul>".format(level.datas[subIndex])
                // }
                // reqParm+="</div>"
            }

            // for(var idx in t.requestParms){
            //     var obj = t.requestParms[idx];
            //     msg = "";
            //     for(var j in obj){//[{},{}]
            //         if(is_array(obj[j])){//child
            //             for(var i in obj[j]){//arr
            //                 if(is_array(obj[j][i])){
            //                     subObj(obj[j][i]);
            //                 }else if(typeof obj[j][i] == "object"){
            //                     for(var name in obj[j][i]){
            //                         console.info("2:"+name);
            //                     }
            //                     //msg += obj[j][i];
            //
            //                 }else{
            //                     console.info("2:"+obj[j][i]);
            //                 }
            //             }
            //         }else{
            //             console.error("1:"+j);
            //             //msg += obj[j];
            //         }
            //
            //     }
                //console.info(msg);


                // for(var objKey in t.requestParms[idx]){
                //     if(typeof t.requestParms[idx][objKey]=="object"){
                //         console.info(t.requestParms[idx][objKey]);
                //     }
                // }
                var arrKeys=[];
                // for(var ke in t.requestParms[idx]){
                //     if (t.requestParms[idx].hasOwnProperty(ke))
                //         arrKeys.push(ke);
                // }

            //     reqParm+="<div class='sec-table-list'> <ul><li class='col-2'>%{requestName}</li><li class='col-1'>%{reqParmreq}</li><li class='col-1'>%{reqParmType}</li><li class=' col-6'>%{reqParmDes}</li></ul></div>".format({
            //         reqParmName:t.requestParms[idx].requestName,
            //         reqParmreq:t.requestParms[idx].required,
            //         reqParmType:t.requestParms[idx].datatype,
            //         reqParmDes:t.requestParms[idx].des
            //     });
            //
            //     if(t.requestParms[idx].sub){
            //         subReqData = t.requestParms[idx].sub;
            //         reqParm+="<div class='sec-table-list'><ul><li class='col-2'><i class='icon-sub open'></i>%{reqParmName}</li><li class='col-1'>%{reqParmreq}</li><li class='col-1'>%{reqParmType}</li><li class=' col-6'>%{reqParmDes}</li></ul><div class='sub'>".format({
            //             reqParmName:t.requestParms[idx].requestName,
            //             reqParmreq:t.requestParms[idx].required,
            //             reqParmType:t.requestParms[idx].datatype,
            //             reqParmDes:t.requestParms[idx].des
            //         });
            //         for(ind in subReqData){
            //             reqParm+="<ul><li class='col-2'>%{subReqParm}</li><li class='col-1'>%{subReqReq}</li><li class='col-1'>%{subReqType}</li><li class='col-6'>%{subReqDes}</li></ul>".format({
            //                     subReqParm:subReqData[ind].requestName,
            //                     subReqReq:subReqData[ind].required,
            //                     subReqType:subReqData[ind].datatype,
            //                     subReqDes:subReqData[ind].des
            //             });
            //         }
            //
            //         reqParm+="</div></div";
            //     }
            // }

            // for(var idx in t.responseParms){
            //     resParm+="<div class='sec-table-list'><ul><li class='col-2'>%{resParmName}</li><li class='col-1'>%{resParmType}</li><li class=' col-6'>%{resParmDes}</li></ul></div>".format({
            //         resParmName:t.responseParms[idx].responseName,
            //         resParmType:t.responseParms[idx].datatype,
            //         resParmDes:t.responseParms[idx].des
            //     });
            //
            //
            // }
        }
    });
    extype+="</div>";
    reqParm+="</div>";
    resParm+="</div>";
    urls+="</select><span class='method'>请求方式：<strong></strong></span>" +
        "<a href='javascript:void(0)' class='copy' onclick='copy()'>复制</a><span class='copysuc'>复制成功</span> ";
    $('.urls').html(urls);
    $('.extype').html(extype);
    $('#req').html(reqParm);
    $('#res').html(resParm);
    var urlMethod = $('.urls-select option:selected').attr('data-method');
    $('.method strong').html(urlMethod)
}

function urlSel(){
    var urlSelect = $('.urls-select option:selected').attr('data-method');
    $('.method strong').html(urlSelect)
}
function subObj(arr,level){
    var indent = level.indent;
    var parent = level.parent;
    var c = 0;
    for(var obj in arr){
        var _obj = arr[obj];
        level.count += 1;
        level.datas[level.count] = new Object();
        level.datas[level.count]["_indent"] = indent;
        level.datas[level.count]["_sub"] = "";
        level.datas[level.count]["_parent"] = parent;


        for(var name in _obj){
            if(is_array(_obj[name])){
                level.indent += 10;
                level.datas[level.count]["_sub"] = "icon-sub";
                level.datas[level.count]["_open"] = "isOpen";
                level.parent += ("-"+(++c));
                subObj(_obj[name],level);
                level.str= 0;
                level.parent ="";
                level.indent=10;
            }else{
                level.datas[level.count][name] = _obj[name];
            }
        }

    }
}