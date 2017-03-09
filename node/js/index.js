var sizes;
$.ajax({
    url : '/sizes',
    async : false,
    method : 'get',
    success : function(data){
        sizes = Math.ceil((data.data) / 8);
        if(sizes != 1){
            $('#pages').show();
        }
    }
});

$(document).ready(function(){
    $.ajax({
        url : "/islogin",
        async : false,
        type : "get",
        success : function(data){
            if(!data.data){
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
                "<div class='follow'><a href='javascript:void(0)' class='%{flwed}' onclick='follow(this)' data-name='%{name}' data-follow='%{follow}' ></a></div></div>").format({
                    icon : '/logo/' + item.name,
                    name : item.name,
                    author : item.author,
                    version : item.version,
                    flwed : ((item.follow == true) ? 'flwed' : ''),
                    time : item.createTime.split(" ")[1],
                    date : item.createTime.split(" ")[0],
                    detailed : item.description,
                    follow : item.follow
                })

            });
            list += "</div>";
            $("#containal").html(list).show();
            userName()
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




    followLists();


});

//读取用户名
function userName(){
    var userName = $.cookie("userName");
    $('.user strong').text(userName)
}

//关注列表
function followLists(){
    $.ajax({
        type : "GET",
        async : false,
        url : "/followList",
        success : function(data){
            var followList = "<ul>"
            $.each(data.data, function(index, item){
                followList += ("<li><span class='sort'><a href='javascript:void(0)' onclick='folSort(this)' class='prev'><img src='/images/up.png' alt=''></a><a href='javascript:void(0)' class='next' onclick='folSort(this)'><img src='/images/down.png' alt=''></a></span>" +
                "<a href='/detail#%{followName}'  class='followName'>%{followName}</a><a href='javascript:void(0)' onclick='delFollow(this)' class='del-fol'>" +
                "<img src='/images/pro_close.png' alt=''></a></li>").format({followName : item});

            });
            followList += "</ul>";
            $('.user-list').html(followList)
        },
        error : function(data){
            console.info(data);
        }
    })
}

function user(self){

    if(!$(self).hasClass('ac')){
        $(self).addClass('ac');
      $('.user-list').show();
        followLists();
    }else {
        $(self).removeClass('ac');
        $('.user-list').hide()
    }
}

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
        pagination.limit = 1;
        $(self).attr("disabled", true).addClass('disabled').siblings().attr("disabled", false).removeClass('disabled')
        $("#pages .prevPage").attr("disabled", true).addClass("disabled");
    } else if(name == "prev"){
        pagination.limit = --curPage;
        $("#pages button").removeAttr("disabled").removeClass('disabled');
        if(pagination.limit <= 1){
            pagination.limit = 1;

            $("#pages .firstPage").attr("disabled", true).addClass("disabled");
            $("#pages .prevPage").attr("disabled", true).addClass("disabled");
        }

    } else if(name == "next"){
        pagination.limit = ++curPage;
        $("#pages button").removeAttr("disabled").removeClass('disabled');
        if(pagination.limit >= sizes){
            pagination.limit = sizes;
            // $("#pages button").removeAttr("disabled").removeClass('disabled');
            $("#pages .lastPage").attr("disabled", true).addClass("disabled");
            $("#pages .nextPage").attr("disabled", true).addClass("disabled");
        }

    } else if(name == "end"){
        pagination.limit = sizes;
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
                "<div class='follow'><a href='javascript:void(0)' class='%{flwed}' onclick='follow(this)' data-follow='%{follow}'></a></div><div class='detailed'><a href='/detail#%{name}'>文档</a></div></div>").format({
                    icon : '/logo/' + item.name,
                    name : item.name,
                    author : item.author,
                    version : item.version,
                    time : item.createTime.split(" ")[1],
                    date : item.createTime.split(" ")[0],
                    flwed : ((item.follow == true) ? 'flwed' : ''),
                    detailed : item.description,
                    follow : item.follow
                })

            });
            list += "</div>";
            $("#containal").html(list).fadeIn()
            location.hash = "page=" + pagination.limit;

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
        url : "/userlogout",
        success : function(data){
            window.location.href = "/login"
        },
        error : function(data){
            console.info(data);
        }
    })
}

function follow(_this){
    var data = {};
    data.projectName = $(_this).parents('.item').children('.item-title').children().text();

    if($(_this).hasClass('flwed')){
        $(_this).removeClass('flwed');
        $.ajax({
            type : "POST",
            url : "/delFollow",
            data : data,
            success : function(data){
//                console.info(data);
            },
            error : function(data){
                console.info(data);
            }
        })
    } else {
        $(_this).addClass('flwed');
        $.ajax({
            type : "POST",
            url : "/addFollow",
            data : data,
            success : function(data){
//                console.info();
            },
            error : function(data){
                console.info(data);
            }
        })
    }
}

function delFollow(_this){
    var data = {};
    data.projectName = $(_this).siblings('.followName').text();
    $.ajax({
        type : "POST",
        url : "/delFollow",
        data : data,
        success : function(data){
            var proName = $(_this).parent().children('.followName').text()
            $(_this).parent().remove();
            $('.item-list .item .follow a[data-name='+ proName+']').removeClass('flwed')
        },
        error : function(data){
            console.info(data);
        }
    })
}

//关注排序

function folSort(self){
    var name = $(self).attr("class");
    var li = $(self).parents('li');
    var sortData={};
    if(name=="next"&&li.next()){
        li.next().after(li)
    }else if(name=="prev"&&li.prev()){
        li.prev().before(li);
    }
    var listSort=[];
    for(var i=0,len=$('.user-list li').length; i<len; i++){
        var listName = $('.user-list li').eq(i).children('.followName').text();
        listSort.push(listName)
    }
    sortData.projects = listSort.join(',');
   $.ajax({
       type:"post",
       url:"/sortList",
       async:false,
       data:sortData,
       success:function(data){
       },
       error:function(data){
       }
   })
}





