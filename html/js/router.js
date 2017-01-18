$(function(){
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
                window.location.hash='#1-1-1';
            }
        })
    }else {
        var arr =window.location.hash.slice(1,7).split('-');
        myattr ="tpls/tpl_guide/";
        var cid = window.location.hash.slice(1,7);
        var navId = '#nav'+cid;
        var listId = $(navId).parents('.menu').children('a').attr('id');
        $.ajax({
            url:'/html/'+myattr+listId+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
                var $color = $('header').css('background-color');
                var lhtml = $('nav #nav'+cid).text();
                var shtml = $('nav #nav'+cid).parents('.menu').children('a').children('span').text();
                var rhtml = $('nav #nav'+cid).parents('.mainbav').children('a').children('span').text();
                var contitle = rhtml + '>' + shtml + '>' + lhtml
                $('.conTitle').html(contitle)
                $('nav #nav'+cid).addClass('active');
                $('nav #nav'+cid).parents('.menu').children('a').addClass('bc').children('.iconfont').html("&#xe607;").css("color",$color);
                $('nav #nav'+cid).parents('.mainbav').children('a').addClass('ac').children('.iconfont').html("&#xe607;").css("color",$color);
                $('nav #nav'+cid).parents('.subnav').show();
                $('nav #nav'+cid).parents('.change').show();
                console.info();
                $(window).scrollTop($('#'+cid).offset().top);
            }
        })
    }

    $('nav .menu a').click(function(){
        var lastIndex = $('.active').parent().index()+1;
        var secondIndex = $('.active').parents('.menu').index()+1;
        var rootIndex = $('.active').parents('.mainbav').index()+1;
        var hash = '#'+rootIndex+'-'+secondIndex+'-'+lastIndex;
        window.location.hash = hash;
        var rI = rootIndex+1;
        var sI = secondIndex+1;
        var lI = lastIndex+1
        var myid = $('.active').parents('.menu').children('a').attr('id')
        var myattr = $('.active').parents('.menu').children('a').attr('my-attr');
        myattr ="tpls/tpl_guide/";
        $.ajax({
            url:'/html/'+myattr+myid+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
            }
        })
    })
})