var sizes;
$.ajax({
    url : '/sizes',
    async : false,
    method : 'get',
    success : function(data){
        sizes = Math.ceil((data.data) / 8);
        if(sizes == 1){
            $('#pages').hide();
        }
    }
});
$(document).ready(function(){

    $.ajax({
        url : "/islogin",
        type : "get",
        success : function(data){
            if(data.data == true){
                return
            } else {
                location.href = "/login";
            }
        }
    });


    var hash = window.location.hash;
    if(!hash){
        hash = "page=1"
    }
    var page = {pageSize : hash.split("=")[1]};

    $.ajax({
        type : "post",
        data : page,
        url : "/lists",
        success : function(data){

            var list = "<div class='item-list'>";
            $.each(data.data, function(index, item){
                list += ("<div class='item'><div class='icon'><a href='/detail#%{name}'  onmouseover='des(this)' onmouseout='_des(this)' >" +
                "<img src='%{icon}' alt='图片未找到'></a></div>" +
                "<div class='item-title'><strong>%{name}</strong></div><div>作者：%{author}</div>" +
                "<div class='item-info'><span class='version'>%{version}</span><span class='time'>%{date}<br/>%{time}</span></div>" +
                "<div class='detailed'><a href='/detail#%{name}'>文档</a></div><div class='des'>%{detailed}</div>" +
                "<div class='follow'><a href='javascript:void(0)' class='%{flwed}' onclick='follow(this)' data-follow='%{follow}' ></a></div></div>").format({
                    icon : '/logo/' + item.name,
                    name : item.name,
                    author : item.author,
                    version : item.version,
                    flwed:((item.follow==true)?'flwed':''),
                    time : item.createTime.split(" ")[1],
                    date : item.createTime.split(" ")[0],
                    detailed : item.description,
                    follow:item.follow
                })
               
            });
            list += "</div>";
            $("#containal").html(list);
            //获取分页数
            location.hash = "page=" + page.pageSize;
            if(window.location.hash.split("=")[1] == "1"){
                $('.firstPage').attr('disabled', true).addClass('disabled');
                $('.prevPage').attr('disabled', true).addClass('disabled');
            } else if(window.location.hash.split("=")[1] == sizes){
                $('.lastPage').attr('disabled', true).addClass('disabled');
                $('.nextPage').attr('disabled', true).addClass('disabled');
            }



        },
        error : function(data){
            console.info(data);
        }
    })


    //关注列表
    function followLists(){
        $.ajax({
            type:"GET",
            async:false,
            url:"/followList",
            success:function(data){
                var followList="<ul>"
                $.each(data.data,function(index,item){
                    followList+=("<li><a href='javascript:void(0)'><img src='/images/drag.png' alt='' class='drag'></a>" +
                    "<a href='/detail#%{followName}' class='followName'>%{followName}</a><a href='javascript:void(0)' onclick='delFollow(this)' class='del-fol'>" +
                    "<img src='/images/pro_close.png' alt=''></a></li>").format({followName:item});

                });
                followList+="</ul>";
                $('.user-list').html(followList)
            },
            error:function(data){
                console.info(data);
            }
        })
    }
    followLists();

    $('.user-wrap .user').hover(function(){
        followLists();
    })




});

function des(_this){
    $(_this).parents('.item').children('.des').show();
}
function _des(_this){
    $(_this).parents('.item').children('.des').hide();
}

function pageBtn(self, name){
    var pagination = {};
    var curPage = location.hash.split("=")[1];

    if(name == "first"){
        console.info("first");
        pagination.pageSize = 1;
        $(self).attr("disabled", true).addClass('disabled').siblings().attr("disabled", false).removeClass('disabled')
        $("#pages .prevPage").attr("disabled", true).addClass("disabled");
    } else if(name == "prev"){
        console.info("prev");
        pagination.pageSize = --curPage;
        $("#pages button").removeAttr("disabled").removeClass('disabled');
        if(pagination.pageSize <= 1){
            pagination.pageSize = 1;

            $("#pages .firstPage").attr("disabled", true).addClass("disabled");
            $("#pages .prevPage").attr("disabled", true).addClass("disabled");
        }

    } else if(name == "next"){
        console.info("next");
        pagination.pageSize = ++curPage;
        $("#pages button").removeAttr("disabled").removeClass('disabled');
        if(pagination.pageSize >= sizes){
            pagination.pageSize = sizes;
            // $("#pages button").removeAttr("disabled").removeClass('disabled');
            $("#pages .lastPage").attr("disabled", true).addClass("disabled");
            $("#pages .nextPage").attr("disabled", true).addClass("disabled");
        }

    } else if(name == "end"){
        console.info("end");
        pagination.pageSize = sizes;
        $(self).attr("disabled", true).addClass('disabled').siblings().attr("disabled", false).removeClass('disabled');
        $("#pages .nextPage").attr("disabled", true).addClass("disabled");
    }

    $.ajax({
        type : "post",
        url : "/pageSize",
        data : pagination,
        success : function(data){

            var list = "<div class='item-list'>"
            $.each(data.data, function(index, item){
                list += ("<div class='item'><div class='icon'><a href='/detail#%{name}'><img src='%{icon}' alt='图片未找到'></a></div>" +
                "<div class='item-title'><strong>%{name}</strong></div><div>作者：%{author}</div>" +
                "<div class='item-info'><span class='version'>%{version}</span><span class='time'>%{date}<br/>%{time}</span></div>" +
                "<div class='follow'><a href='javascript:void(0)' onclick='follow(this)' data-follow='%{follow}'></a></div><div class='detailed'><a href='/detail#%{name}'>文档</a></div></div>").format({
                    icon : '/logo/' + item.name,
                    name : item.name,
                    author : item.author,
                    version : item.version,
                    time : item.createTime.split(" ")[1],
                    date : item.createTime.split(" ")[0],
                    detailed : item.description,
                    follow:item.follow
                })

            });
            list += "</div>";
            $("#containal").html(list)
            location.hash = "page=" + pagination.pageSize;

        },
        error : function(data){
            console.info(data);
        }
    })

}


//分页跳转


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

function follow(_this){
    var data={};
    data.projectName=$(_this).parents('.item').children('.item-title').children().text();

    if($(_this).hasClass('flwed')){
        $(_this).removeClass('flwed');
        $.ajax({
            type:"post",
            url:"/delFollow",
            data:data,
            success:function(data){
                console.info(data);
            },
            error:function(data){
                console.info(data);
            }
        })
    }else {
        $(_this).addClass('flwed');
        $.ajax({
            type:"POST",
            url:"/addFollow",
            data:data,
            success:function(data){
                console.info();
            },
            error:function(data){
                console.info(data);
            }
        })
    }
}

function delFollow(_this){
    var data={};
    data.projectName = $(_this).siblings('.followName').text();
    $.ajax({
        type:"POST",
        url:"/delFollow",
        data:data,
        success:function(data){
            var proName = $(_this).parent().children('.followName').text()
            $(_this).parent().remove();
            // $('.item-list .item .item-title strong[text='+ proName +']').parents('.item').children('.follow').children().removeClass('flwed')
            // // console.info($('.item-list .item .item-title strong[innerHTML='+ proName +']'));
        },
        error:function(data){
            console.info(data);
        }
    })
}

//ajax更新
var Update = function() {
    var projects = $('#orderlist').val();
    $.ajax({
        type: "post",
        async:false,
        url: "/sortList",
        data: { projects},
        success: function(data) {
            $.each(data.data,function(index,item){
                var followList="<ul>"
                $.each(data.data,function(index,item){
                    followList+=("<li><a href='javascript:void(0)'><img src='/images/drag.png' alt='' class='drag'></a>" +
                    "<a href='javascript:void(0)' class='followName'>%{followName}</a><a href='javascript:void(0)' onclick='delFollow(this)' class='del-fol'>" +
                    "<img src='/images/pro_close.png' alt=''></a></li>").format({followName:item})
                });
                followList+="</ul>";
                $('.user-list').html(followList)
            })
        },
        error:function(data){
            console.info(data);
        }
    });
};



