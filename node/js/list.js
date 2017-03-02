$(document).ready(function(){

    $.get("/islogin",function(data){
        if(data.data==true){
           return
        }else {
            location.href="/login";
        }
    })



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
            location.hash="page=1"
        },
        error:function(data){
            console.info(data);
        }
    })
});

//获取分页数
var sizes;
$.get('/sizes',function(data){
    sizes=data.data

});


//分页跳转
$("#pages").on("click","a",function(){
    var pageName = $(this).attr('class');
    var pagination={};
    var curPage = location.hash.split("=")[1];
    if(pageName=="firstPage"){
        pagination.pageSize=1;
    }else if(pageName=="nextPage"){
        pagination.pageSize = ++curPage;
        if(pagination.pageSize>sizes){
            pagination.pageSize=sizes;
        }
    }else if(pageName="prevPage"){
        pagination.pageSize = --curPage;
        if(pagination.pageSize<1){
            pagination.pageSize=1
        }
    }else if(pageName="lastPage"){
        pagination.pageSize=sizes;
    }
    $.ajax({
        type:"post",
        url:"/pageSize",
        data:pagination,
        success:function(data){
            location.hash="page="+pagination.pageSize
        },
        error:function(data){
            console.info(data);
        }
    })
})

function logout(){
    $.ajax({
        type:"get",
        url:"/logout",
        success:function(data){
            window.location.href="/login"
        },
        error:function(data){
            console.info(data);
        }
    })
}