$(document).ready(function(){
    $.get("/data/data.json",function(data){
        var itemList="<div class='item-list'>";
        $.each(data.list,function(index,item){
            itemList+="<div class='item'><div class='icon'><img src='%{icon}' alt=''></div><div class='item-title'><strong>%{name}</strong></div><div class='item-info'><span class='version'>%{version}</span><span class='time'>%{time}</span></div><div class='detailed'><a href='javascript:void(0)'>%{detailed}</a></div></div>".format({
                icon:item[index]
            }

            );
        });
        itemList+="</div>";
        $('#containal').html(itemList)
    })
})