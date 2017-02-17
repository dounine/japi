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
        var $color = $('header').css('background-color');
        $('.change .submenu').removeClass('active');
        $(this).parents('.mainbav').siblings('.mainbav').children('.subnav').hide();
        $(this).parents('.mainbav').siblings('.mainbav').children('.mainbavClick').children('i').html("&#xe608;").css('color',"#555B5E");
        $('.mainbavClick').removeClass('ac');
        $('.menuClick').removeClass('bc');
        $(this).parents('.mainbav').children('a').addClass('ac');
        $(this).parents('.mainbav').children('a').children('i').html("&#xe607;");
        $(this).parents('.mainbav').children('a').children('i').css("color",$color);
        $(this).parent('.mainbav').siblings('.mainbav').children('.subnav').hide()
        $(this).parents('.mainbav').children('a').next().show()

        $(this).parents('.menu').children('a').addClass('bc');
        $(this).parents('.menu').children('a').children('i').html("&#xe607;");
        $(this).parents('.menu').children('a').children('i').css("color",$color);
        $(this).parents('.menu').siblings('.menu').children('a').children('.iconfont').html("&#xe608;").css("color","#555B5E");
        $(this).parents('.menu').siblings('.menu').children('.change').hide();
        $(this).parents('.menu').children('a').next().show()
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
        var ahash = window.location.hash.slice(1,7);
        var ahashLen = $('#nav'+ahash).parents('.change').children().length;
        var sobj = [];
        for(var i=0;i<ahashLen; i++){
            var navId = $('#nav'+ahash).parents('.change').children().eq(i).find('.submenu').attr('id');
            var conId = $('#'+navId.slice(3,8));
            if(conId.offset().top<=wst+80){
                var idValue = conId.attr('id');
                sobj.push(conId.attr('id'));
            }
        }
        for(var n =0;n<sobj.length;n++){
            if( n== (sobj.length-1)){
                $('nav .submenu').removeClass('active')
                $('#nav'+sobj[n]).addClass('active');
                window.location.hash='#'+sobj[n];
            }
        }
        var t = $('#'+ahash).children('.main').offset().top;
        var h =$('#'+ahash).children('.main').height();
        var wstH = wst+120;
        var a = (wstH-t)/h*100+'%';
        $('.bar .barload').css('width',a);
        
        var lhtml = $('#nav'+ahash).text();
        var shtml = $('#nav'+ahash).parents('.menu').children('a').children('span').text();
        var rhtml = $('#nav'+ahash).parents('.mainbav').children('a').children('span').text();
            var contitle = rhtml + '>' + shtml + '>' + lhtml
            $('.conTitle').html(contitle);


    })


    //搜索框
    var searchArr = [];
    var searchLen = $('nav').find('.submenu').length;
    for(var i=0;i<searchLen; i++){
        var allsubmenu = $('nav').find('.submenu').eq(i).text();
        var allid = $('nav').find('.submenu').eq(i).attr('id');
        var obj = {};
        obj.text = allsubmenu;
        obj.id = allid;
        searchArr.push(obj)
    }

    $('#search').keyup(function(){
        var inputTxt = $('#search').val();
        if(inputTxt!=""){
            $('.searchtxt').show();
            var tab ="<ul style='max-height:100px'>";
            $.each(searchArr,function(n,item){
                if(item.text.indexOf(inputTxt)!=-1){
                    tab+="<li><a href='javascript:void(0)' myid ='"+ item.id+"'>"+item.text+"</a></li>"
                }
            })
            tab+="</ul>";
            $('.searchtxt').html(tab);
            $('.searchtxt li a').eq(0).addClass('active')
            $('.searchtxt a').click(function(){
                var findmenu = $(this).text();
                var findid = $(this).attr('myid');
                $('#'+findid).click()
                $('.searchtxt').hide();
                $('#search').val(findmenu);
            });
            $('.searchtxt li a').mouseover(function(){
                $('.searchtxt li a').removeClass('active')
                $(this).addClass('active')
            })
        }else{
            $('.searchtxt').hide();
        }

    });
    $('.searchBtn').click(function(){
        var findmenu = $('#search').val();
        if(!findmenu){
            return;
        }
        $("nav .submenu:contains('"+findmenu+"')").eq(0).click()

    })

    

});






