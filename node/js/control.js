var newDatas,urls,extype,reqParm,resParm ;

$(document).ready(function(){
    $.ajax({
        type:"post",
        url:"/index",
        success:function(data){
            //遍历生成导航
            $.each(data.nav,function(ind,item){
                var mainbav = ("<div class='nav-section' data-index='%{mainInd}'><span class='mainbav open'><a href='javascript:void(0)' >" +
                " %{mainNav}</a></span><ul>").format({mainInd:item.mainInd,mainNav:item.mainNav});
                for(var index in item.subNav){
                    mainbav+="<li><a href='javascript:void(0)' data-index='%{subInd}'>%{navList}</a></li>".format({subInd:item.subNav[index].subInd,navList:item.subNav[index].subName})
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
        var subInd = $(this).attr("data-index"),
         mainInd = $(this).parents('.nav-section').attr("data-index"),
         navInd = mainInd+subInd,
         cont;
        $.get("/data/data_new.json",function(data){
            cont="<div><h4>基本信息</h4><div class='version'>";
            $.each(data.contDatas,function(ind,item){

                if(item.key==navInd){
                    newDatas = item.version;
                    var dateTime;
                    cont+="<p>版本：<span class='verNo'></span><select class='version-list' onchange='verSel()'>";
                    urls = "接口地址：<select class='api-select'>"
                    $.each(newDatas,function(i,t){
                        cont += "<option value=%{verVal}>%{verMsg}</option>".format({verVal:t.name,verMsg:t.msg});
                        var newDate = t.datetime;
                    });
                    cont+="</select></p><p>更新时间：<select class='time' onchange='timeSel()'></select></p>";

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
    })
});


function verSel(){
    var verSelect = $('.version-list option:selected').val();
    $.each(newDatas,function(index,item){
        if(item.name==verSelect){
            var _time
            $.each(item.datetime,function(index,item){
                _time+="<option value='%{itemValue}'>%{itemDate}</option>".format({itemDate:item.date,itemValue:item.value})
            })
            $('.time').html(_time);
            timeSel()
        }
    })
}
function timeSel(){
    var timeSelect = $('.time option:selected').val();
    console.info(timeSelect);
   $.get('data/data_new.json',function(data){
       extype="<div>";
       reqParm="<div >";
       resParm="<div >";
       $.each(data.details,function(i,t){
            if(t.name==timeSelect){
                console.info(t);
                if(t.urls.length=="1"){
                    urls=(" <span class='urls-select' data-value='%{urlVal}' data-method=%{dataMethod}>%{urlVal}</span><span class='method'>请求方式：<strong>%{dataMethod}</strong></span><button class='copy' onclick='copy()'>复制</button><span class='copysuc'>复制成功</span>").format({urlVal:t.urls[0].url,dataMethod:t.urls[0].method})
                }else {
                    urls="<select class='urls-select' onchange='urlSel()'>";
                    for(var idx in t.urls){
                        urls+="<option value=%{urlVal} data-method=%{dataMethod}>%{urlVal}</option>".format({urlVal:t.urls[idx].url,dataMethod:t.urls[idx].method})
                    }
                    urls+="</select><span class='method'>请求方式：<strong></strong></span>" +
                        "<button class='copy' onclick='copy()'>复制</button><span class='copysuc'>复制成功</span> ";
                }

                for(var idx in t.ex){
                    extype+="<p>%{exName}:<span>%{exVal}</span></p>".format({exName:t.ex[idx].name,exVal:t.ex[idx].value})
                }
                var level = new Object();
                level.indent = 0;
                level.count = 0;
                level.parent = "";
                level.datas = new Object();
                subObj(t.requestParms,level);

                for(var subIndex in level.datas){
                    level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"]+2;
                    reqParm+="<div class='sec-table-list %{_open}' parent='%{_parent}'> <ul><li class='col-2'><i class='%{_sub}' style='left:%{_indent}px'></i> <span style='text-indent:%{_indent}px'>%{requestName}</span></li><li class='col-1'>%{required}</li><li class='col-1'>%{datatype}</li><li class='col-1'>%{default}</li><li class=' col-6'>%{des}</li></ul></div>".format(level.datas[subIndex])
                }

                var level = new Object();
                level.indent = 0;
                level.count = 0;
                level.parent = "";
                level.datas = new Object();
                subObj(t.responseParms,level);
                for(var subIndex in level.datas){
                    level.datas[subIndex]["_indent1"] = level.datas[subIndex]["_indent"]+2;
                    resParm+="<div class='sec-table-list %{_open}' parent='%{_parent}'> <ul><li class='col-2'><i class='%{_sub}' style='left:%{_indent}px'></i> <span style='text-indent:%{_indent}px'>%{responseName}</span></li><li class='col-1'>%{datatype}</li><li class='col-1'>%{default}</li><li class=' col-6'>%{des}</li></ul></div>".format(level.datas[subIndex])
                }
            }
       });
       extype+="</div>";
       reqParm+="</div>";
       resParm+="</div>";

       $('.urls').html(urls);
       $('.extype').html(extype);
       $('#req').html(reqParm);
       $('#res').html(resParm);
       var urlMethod = $('.urls-select option:selected').attr('data-method');
       $('.method strong').html(urlMethod)
   })
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
var is_array = function(value) {
    return value &&
        typeof value === 'object' &&
        typeof value.length === 'number' &&
        typeof value.splice === 'function' &&
        !(value.propertyIsEnumerable('length'));
};