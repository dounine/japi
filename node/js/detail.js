$(document).ready(function(){
    $('#nav ').on("click", ".mainbav", function(e){
        if($(this).hasClass('open')){
            $(this).removeClass('open').siblings('ul').slideToggle("fast");
            e.stopPropagation()

        } else {
            $(this).addClass('open').siblings('ul').slideToggle("fast");
            e.stopPropagation()
        }
    });
    $("#nav").on("click", "a", function(){
        $("#nav a").parent().removeClass('active')
        $(this).not('.mainbav a', '.icon').parent().addClass('active')
    });

    $("#nav").on("click", ".icon", function(){
        if(!$(this).hasClass('ac')){
            $(this).addClass('ac');
            $(".nav-list .nav-section ul").show();
            $(".nav-list .nav-section .mainbav").addClass("open")
        } else {
            $(this).removeClass('ac');
            $(".nav-list .nav-section ul").hide();
            $(".nav-list .nav-section .mainbav").removeClass("open")
        }
    })
});


$("#content").on("mouseover", "span.itemName", function(){
    var ac = $(this).attr('data-ac');
    if(ac == "ac"){
        $(this).css({"cursor" : "pointer"})
    }
    return
});


function modalClose(){
    $("#modal .m-con").empty();
    $("#modal").hide();
    $("#modalbg").hide();
}


$('#content').on("click", ".icon-sub.first", function(e){
    if($(this).hasClass('ac')){
        $(this).removeClass('ac')
        $(this).parents('.sec-table-list').children('.sub-list').slideUp("fast");
    } else {
        $(this).addClass('ac')
        $(this).parents('.sec-table-list').children('.sub-list').slideDown("fast");
    }
});


//参数点击折叠效果

function iconSubClick(self){
    var area = $(self).parents('.sec-table-wrap');
    var par = $(self).parents('div.sec-table-list');
    var asMyParents = area.find('div.sec-table-list[index="%{index}"]'.format({index : par.attr('index')}));
    var es = area.find('div.sec-table-list[index^="%{index}"]'.format({index : par.attr('index') + "-"})).not(par);
    if(par.hasClass("isOpen")){
        es.stop();
        es.slideUp("fast");
        par.removeClass("isOpen");
        $(self).addClass('open');
    } else {
        es.stop();
        es.slideDown("fast");
        par.addClass("isOpen");
        $(self).removeClass('open');
    }
}

//复制
function copy(){
    var apiSelect = document.getElementsByClassName('urls-select')[0];
    var copyName = apiSelect.tagName;
    if(copyName == "SELECT"){
        var apiIndex = apiSelect.selectedIndex; // selectedIndex代表的是你所选中项的index
        var apiVal = apiSelect.options[apiIndex].value;
    } else if(copyName == "SPAN"){
        var apiVal = $('.urls-select').attr('data-value')
    }

    // 创建元素用于复制
    var aux = document.createElement("input");
    // 设置元素内容
    aux.setAttribute("value", apiVal);
    // 将元素插入页面进行调用
    document.body.appendChild(aux);
    aux.style.opacity = "0";
    // 复制内容
    aux.select();
    // 将内容复制到剪贴板
    document.execCommand("copy");
    $('.copysuc').show();
    $('.copy').attr("disabled");
    setTimeout(function(){
        $('.copy').removeAttr("disabled")
        $('.copysuc').hide();
    }, 2000)

}

function logout(){
    $.ajax({
        type : "get",
        url : "/logout",
        success : function(data){
            window.location.href = "/login"
        },
        error : function(data){
            console.info(data);
        }
    })
}

