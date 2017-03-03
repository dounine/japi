$.get("/islogin", function(data){
    if(data.data == true){
        location.href = "/index"
    } else {
        return
    }
})


$(function(){
    $('.log-red').hover(function(){
        $(".login-WeChat", this).toggle();
    });
    $('.login-page:nth-of-type(2)').hide();
    $('.login-tabs a').click(function(){
        $(this).addClass('current').siblings().removeClass('current');
        $(".login-page").eq($(this).index()).show().siblings().hide();
    });
    $(".checkedNum").each(function(i){
        $(this).attr({"id" : "checked" + i});
        $(this).next("label").attr("for", "checked" + i);
    });
    function userRem(){
        if($.cookie("username") && $.cookie("password") && $.cookie("remUser")){
            var username = $.cookie("username");
            var password = $.cookie("password");
            $("#username").val(username);
            $("#password").val(password);
            $(".checkedNum").attr("checked", "true")
        } else {
            $("#username").val();
            $("#password").val();
        }

    }

    $("form input").focus(function(){
        $('.form-msg').html(" ")
    })

    userRem()
    $('#form').validate({
        rules : {
            username : {
                required : true,
            },
            password : {
                required : true,
                minlength : 5,
                maxlength : 14
            }
        },
        messages : {
            username : {
                required : "用户名不能为空！",
            },
            password : {
                required : "密码不能为空！",
                minlength : "密码长度为5-14位!",
                maxlength : "密码长度为5-14位!"
            }
        },
    });
});


function login(){
    var user = {}
    user.username = $('#username').val();
    user.password = $("#password").val();
    var remember = $('.checkedNum').prop('checked')

    console.info(user);
    $.ajax({
        type : "post",
        url : "/login",
        data : user,
        success : function(resData){
            if(resData.code == "1"){
                $(".form-msg").text(resData.msg)
            } else if(resData.code == "0"){
                if(remember){
                    $.cookie("username", user.username, {expires : 7});
                    $.cookie("password", user.password, {expires : 7});
                    $.cookie("remUser", "true", {expires : 7})
                }
                window.location.href = "/index"
            }

        },
        error : function(res){
            console.info(res);
        }
    })
}

document.onkeydown = function(event){
    if(event.keyCode == "13"){
        login()
    } else {
        return
    }

}


