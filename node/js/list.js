$(document).ready(function(){
    $.ajax({
        type:"get",
        url:"/lists",
        success:function(data){
            var list="<div class='item-list'>"
            $.each(data.data,function(index,item){
                list+=("<div class='item'><div class='icon'><a href='/index#projectName=%{name}'><img src='%{icon}' alt='图片未找到'></a></div>" +
                "<div class='item-title'><strong>%{name}</strong></div><div>作者：%{author}</div>" +
                "<div class='item-info'><span class='version'>%{version}</span><span class='time'>%{date}<br/>%{time}</span></div>" +
                "<div class='detailed'><a href='/index#projectName=%{name}'>文档</a></div></div>").format({
                    icon:'/logo/'+item.name,
                    name:item.name,
                    author:item.author,
                    version:item.version,
                    time:item.createTime.split(" ")[1],
                    date:item.createTime.split(" ")[0],
                    detailed:item.description
                })

            });
            list+="</div>";
            $("#containal").html(list)
        },
        error:function(data){
            console.info(data);
        }
    })
});

$("#pages").on("click","a",function(){
    var page = $(this).attr('class');
    var pageGet;
    if(page=="firstPage"){
        pageGet=1;
    }else {

    }
    $.ajax({
        type:"post",
        url:"/pages",
        data:pageGet,
        success:function(data){
            console.info(data);
        },
        error:function(data){
            console.info(data);
        }
    })
})

function logout(){
    window.location.href="/login"
    // $.ajax({
    //     type:"get",
    //     url:"/logout",
    //     success:function(data){
    //         window.location.href="/login"
    //     },
    //     error:function(data){
    //         console.info(data);
    //     }
    // })
}