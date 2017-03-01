/**
 * Created by ike on 2016/12/9.
 */
$(function(){
    $('.log-red').hover(function(){
        $(".login-WeChat",this).toggle();
    });
    $('.login-page:nth-of-type(2)').hide();
    $('.login-tabs a').click(function(){
        $(this).addClass('current').siblings().removeClass('current');
        $(".login-page").eq($(this).index()).show().siblings().hide();
    });
    $(".checkedNum").each(function(i){
        $(this).attr({"id":"checked"+i});
        $(this).next("label").attr("for","checked"+i);
    })
    $('#form').validate({
        rules:{
            username:{
                required:true,
            },
            password:{
                required:true,
                minlength:5,
                maxlength:14
            }
        },
        messages:{
            username:{
                required:"用户名不能为空！",
            },
            password:{
                required:"密码不能为空！",
                minlength:"密码长度为5-14位!",
                maxlength:"密码长度为5-14位!"
            }
        }
    });
});

$("#form").submit(function(e){
    var user={}
    user.username=$('#username').val();
    user.password=$("#userpass").val();
});

function login(){
    var user={}
    user.username=$('#username').val();
    user.password=$("#userpass").val();
    $.ajax({
        type:"post",
        url:"/login",
        data:user,
        success:function(res){
            var token = res.data;
            window.location.href="/list"
        },
        error:function(res){
            console.info(res);
        }
    })
}