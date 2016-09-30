$(function(){
    //改变主题
    $('.changeColor a').click(function(){
        var style = $(this).attr("id");
        $("link[title='"+style+"']").removeAttr("disabled");
        $("link[title!='"+style+"']").attr("disabled","disabled");
        $.cookie('mystyle',style,{expires:30});
        $('.changeColor a').show();
        $(this).hide()
        var $color= $("link[title='"+style+"']").attr('my-color');
        $('nav a.ac .iconfont').css('color',$color);
        $('nav a.bc .iconfont').css('color',$color);

    });
    //设置cookie
    var cookie_style = $.cookie("mystyle");
    if(cookie_style==null){
        $("link[title='theme_red']").removeAttr("disabled");
    }else{
        $("link[title='"+cookie_style+"']").removeAttr("disabled");
        $("link[title!='"+cookie_style+"']").attr("disabled","disabled");
        $('.changeColor a').show();
        $('#'+cookie_style).hide();
    }

    $('.subnav').hide();
    $('.change').hide()
    $('.mainbavClick').click(function(){
        var $color = $('header').css('background-color');
        if(!$(this).hasClass('ac')){
            $(this).parent('.mainbav').siblings('.mainbav').children('.mainbavClick').removeClass('ac');
            $('.menuClick').removeClass('bc')
            $(this).next().show();
            $(this).addClass('ac');
            $(this).children('i').html("&#xe607;");
            $(this).children('i').css("color",$color);
            $(this).parent('.mainbav').siblings('.mainbav').children('.subnav').hide()
            $(this).parent('.mainbav').siblings('.mainbav').find('.iconfont').html('&#xe608;');
            $(this).parent('.mainbav').siblings('.mainbav').find('.iconfont').css("color","#555B5E")
            $(this).next().find(' a:first').click();

        }else {
            $(this).removeClass('ac');
            $(this).next().hide();
            $(this).children('i').html("&#xe608;");
            $(this).children('i').css("color","#555B5E");
            $('.change').hide()
        }
    })

    $('.menuClick').click(function(){
        var $color = $('header').css('background-color');
        if(!$(this).hasClass('bc')){
            $('nav menu a').removeClass('active');
            $('nav a').removeClass('bc')
            $(this).addClass('bc');
            $(this).children('i').html("&#xe607;");
            $(this).children('i').css("color",$color)
            $(this).parent('.menu').siblings('.menu').children('a').children('.iconfont').html("&#xe608;").css("color","#555B5E");
            $(this).parent().siblings('.menu').children('.change').hide();
            $(this).next().show();
            $(this).next().find('li:first a:first').click();
        }else {
            $(this).removeClass('bc');
            $(this).next().hide();
            $(this).children('i').html("&#xe608;");
            $(this).children('i').css("color","#555B5E")
        }
    });
    $('.submenu').click(function(){
        $('.change .submenu').removeClass('active');
        $(this).addClass('active')
    })

    //滚动监听
    $(window).scroll(function(){
        var wst = $(window).scrollTop();
        var i= 0,j= 0,rl,sl,ll;
        var lastIndex = $('.active').parent().index();
        var secondIndex = $('.active').parents('.menu').index();
        var rootIndex = $('.active').parents('.mainbav').index();

        rl=rootIndex+1;sl=secondIndex+1;ll=lastIndex+1
        var navLen = $('.active').parents('.change').children('li').length;
        for(var i=0;i<navLen; i++){
            ll = i+1
            var scrId = '#'+rl+'-'+sl+'-'+ll

            if($(scrId).offset().top<=wst){
                $('.change a').removeClass('active');
                $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).find('.change li').eq(i).children('a').addClass('active');
            }
        }
        var newhash = '#'+rootIndex+'-'+secondIndex+'-'+lastIndex;
        if(window.location.hash==="#-1--1--1"){
            window.location.hash="#0-0-0"
        }else {

            window.location.hash = newhash;
        }





    })

})



