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
        if($(this).hasClass('open')){
            $(this).removeClass('open').parents('.sec-table-list').children('.sub').slideToggle("fast");

        }else {
            $(this).addClass('open').parents('.sec-table-list').children('.sub').slideToggle("fast");
        }
    });
});

//复制
function copy(){
    var apiSelect=document.getElementsByClassName('api-select')[0];
    var index=apiSelect.selectedIndex ; // selectedIndex代表的是你所选中项的index
    var apiVal = apiSelect.options[index].value;
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
}