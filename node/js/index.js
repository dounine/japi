$(document).ready(function(){
    $('#nav ').on("click",".mainbav",function(){
        if($(this).hasClass('open')){
            $(this).removeClass('open').next('ul').slideToggle("fast");

        }else {
            $(this).addClass('open').next('ul').slideToggle("fast");
        }
    });
    $("#nav").on("click","a",function(){
        $("#nav a").parent().removeClass('active')
        $(this).not('.mainbav a','.icon').parent().addClass('active')
    });
    $("#nav").on("click",".icon",function(){
        if(!$(this).hasClass('ac')){
            $(this).addClass('ac');
            $(".nav-list .nav-section ul").show();
            $(".nav-list .nav-section .mainbav").addClass("open")
        }else {
            $(this).removeClass('ac');
            $(".nav-list .nav-section ul").hide();
            $(".nav-list .nav-section .mainbav").removeClass("open")
        }
    })
    $('#content').on("click",".icon-sub",function(){
        var area = $(this).parents('.sec-table-wrap').children('div').prop('id');
        var areaId = "#"+area+" ";
        var par = $(this).parents(areaId+'div.sec-table-list');
        var asMyParents = $(areaId+'div.sec-table-list[parent="%{parent}"]'.format({parent:par.attr('parent')}));
        var index = 1;
        $.each(asMyParents,function(i,item){
            if($(item).is(par)){
                index += i;
            }
        })
        var es = $(areaId+'div.sec-table-list[parent^="%{parent}"]'.format({parent:par.attr('parent')+('-'+index)}));
        if(par.hasClass("isOpen")){
            es.hide();
            par.removeClass("isOpen");
        }else{
            es.show();
            par.addClass("isOpen");
        }

         if($(this).hasClass('open')){
             $(this).removeClass('open').parents('.sec-table-list').children('.sub').slideToggle();

         }else {
             $(this).addClass('open').parents('.sec-table-list').children('.sub').slideToggle();
         }
    });
});

//复制
function copy(){
    var apiSelect=document.getElementsByClassName('urls-select')[0];
    var copyName = apiSelect.tagName;
    if(copyName=="SELECT"){
        var apiIndex=apiSelect.selectedIndex ; // selectedIndex代表的是你所选中项的index
        var apiVal = apiSelect.options[apiIndex].value;
    }else if(copyName=="SPAN"){
        var apiVal = $('.urls-select').attr('data-value')
    }

    // 创建元素用于复制
    var aux = document.createElement("input");
    // 设置元素内容
    aux.setAttribute("value", apiVal);
    // 将元素插入页面进行调用
    document.body.appendChild(aux);
    // 复制内容
    aux.select();
    // 将内容复制到剪贴板
    document.execCommand("copy");
    $('.copysuc').show();
    $('.copy').attr("disabled");
    setTimeout(function(){
        $('.copy').removeAttr("disabled")
        $('.copysuc').hide();
    },2000)

}