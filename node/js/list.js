$(document).ready(function(){
    $.post("/list",function(data){

        var itemList="<div class='item-list'>";
        $.each(data.list,function(index,item){
            itemList+=("<div class='item'><div class='icon'><a href='%{url}'><img src='%{icon}' alt=''></a></div>" +
            "<div class='item-title'><strong>%{name}</strong></div><div>作者：%{author}</div><div class='item-info'><span class='version'>%{version}</span>" +
            "<span class='time'>%{time}</span></div><div class='detailed'><a href='%{url}'>%{detailed}</a></div></div>").format({
                icon:item.icon,name:item.name,version:item.version,time:item.time,detailed:item.detailed,url:item.url,author:item.author
            }
            );
        });
        itemList+="</div>";
        $('#containal').html(itemList);
    })
})