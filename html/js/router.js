$(function(){


    var $color = $('header').css('background-color');
    var current = window.location.hash.slice(1,4);
    if(!current){
        var defaultHtml = $('nav .mainbav').eq(0).find('.menu').eq(0).children('a').attr('id');
        var myattr = $('nav .mainbav').eq(0).find('.menu').eq(0).children('a').attr('my-attr');
        myattr ="tpls/tpl_guide/"
        $.ajax({
            url:'/html/'+myattr+defaultHtml+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
                var $color = $('header').css('background-color');
                var arr = [0,0,0];
                var rhtml = $('nav .mainbav').eq(arr[0]).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').children('span').html();

                var lhtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change').children('li').eq(arr[2]).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml
                $('.conTitle').html(contitle);
                $('nav  .mainbav').eq(arr[0]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change li').eq(arr[2]).children('a').addClass('active');
                $('nav  .mainbav').eq(arr[0]).children('a').addClass('ac').children('.iconfont').html("&#xe607;").css("color",$color);
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').addClass('bc').children('.iconfont').html("&#xe607;").css("color",$color);
                window.location.hash='#0-0-0';

            }
        })
    }else {
        var arr =window.location.hash.slice(1,7).split('-');
        var listId = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').attr('id');
        var myattr = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').attr('my-attr');
        myattr ="tpls/tpl_guide/";
        $.ajax({
            url:'/html/'+myattr+listId+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
                var $color = $('header').css('background-color');
                var rhtml = $('nav .mainbav').eq(arr[0]).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').children('span').html();
                var lhtml = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change').children('li').eq(arr[2]).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml

                $('.conTitle').html(contitle)


                $('nav  .mainbav').eq(arr[0]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').next().show();
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change li').eq(arr[2]).children('a').addClass('active');
                $('nav  .mainbav').eq(arr[0]).children('a').addClass('ac').children('.iconfont').html("&#xe607;").css("color",$color);
                $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').addClass('bc').children('.iconfont').html("&#xe607;").css("color",$color);

                var rid = ++arr[0];
                var sid = ++arr[1];
                var lid = ++arr[2];

                var goid = '#'+rid+'-'+sid+'-'+lid;
                var gotop = $(goid).offset().top;
                $(window).scrollTop(gotop);
            }
        })
    }


    $('nav .menu a').click(function(){
        var lastIndex = $('nav .active').parent().index();
        var secondIndex = $('nav .active').parents('.menu').index();
        var rootIndex = $('nav .active').parents('.mainbav').index();
        var hash = '#'+rootIndex+'-'+secondIndex+'-'+lastIndex;
        window.location.hash = hash;

        var rI = rootIndex+1;
        var sI = secondIndex+1;
        var lI = lastIndex+1
        var myid = $('nav .active').parents('.menu').children('a').attr('id')
        var myattr = $('nav .active').parents('.menu').children('a').attr('my-attr');
        myattr ="tpls/tpl_guide/";
        $.ajax({
            url:'/html/'+myattr+myid+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
                var rhtml = $('nav .mainbav').eq(rootIndex).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).children('a').children('span').html();
                var lhtml = $('nav .mainbav').eq(rootIndex).find('.menu').eq(secondIndex).find('.change').children('li').eq(lastIndex).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml
                $('.conTitle').html(contitle)
                var goId = '#'+rI+'-'+sI+'-'+lI;
                var actop =$(goId).offset().top;
                $(window).scrollTop(actop);
            }
        })

    })

})
