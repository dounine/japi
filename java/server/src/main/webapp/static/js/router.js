$(function(){


    var $color = $('header').css('background-color');
    var current = window.location.hash.slice(1,4);
    if(!current){
        var defaultHtml = $('nav .mainbav').eq(0).find('.menu').eq(0).children('a').attr('id');
        var myattr = $('nav .mainbav').eq(0).find('.menu').eq(0).children('a').attr('my-attr');
        myattr ="/interfaceapidoc/tpls/tpl_guide/"+myattr;
        $.ajax({
            url:myattr+'/'+ defaultHtml+'.html',
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
        // var arr =window.location.hash.slice(1,7).split('-');
        // var listId = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').attr('id');
        // var myattr = $('nav .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').attr('my-attr');

        var cid = window.location.hash.slice(1,7);
        var navId = '#nav'+cid;
        var listId = $(navId).parents('.menu').children('a').attr('id');
        var myattr = $(navId).parents('.menu').children('a').attr('my-attr');
        myattr ="/interfaceapidoc/tpls/tpl_guide/"+myattr;
        $.ajax({
            url:myattr+'/'+listId+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
                var $color = $('header').css('background-color');
                var rhtml = $('nav #nav'+cid).parents('.mainbav').children('a').children('span').text();
                var shtml = $('nav #nav'+cid).parents('.menu').children('a').children('span').text();
                var lhtml = $('nav #nav'+cid).text();
                var contitle = rhtml + '>' + shtml + '>' + lhtml

                $('.conTitle').html(contitle)

                $('nav #nav'+cid).addClass('active');
                $('nav #nav'+cid).parents('.menu').children('a').addClass('bc').children('.iconfont').html('&#xe607').css('color',$color)
                $('nav #nav'+cid).parents('.mainbav').children('a').addClass('ac').children('.iconfont').html('&#xe607').css('color',$color)
                $('nav #nav'+cid).parents('.subnav').show();
                $('nav #nav'+cid).parents('.change').show();
                $(window).scrollTop($('#'+cid).offset().top);
                // $('nav  .mainbav').eq(arr[0]).children('a').next().show();
                // $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').next().show();
                // $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).find('.change li').eq(arr[2]).children('a').addClass('active');
                // $('nav  .mainbav').eq(arr[0]).children('a').addClass('ac').children('.iconfont').html("&#xe607;").css("color",$color);
                // $('nav  .mainbav').eq(arr[0]).find('.menu').eq(arr[1]).children('a').addClass('bc').children('.iconfont').html("&#xe607;").css("color",$color);

                // var rid = ++arr[0];
                // var sid = ++arr[1];
                // var lid = ++arr[2];
                //
                // var goid = '#'+rid+'-'+sid+'-'+lid;
                // var gotop = $(goid).offset().top;
                // $(window).scrollTop(gotop);
            }
        })
    }


    $('nav .menu a').click(function(){
        var $nav = $('nav .active');
        var lastIndex = $nav.parent().index()+1;
        var secondIndex = $nav.parents('.menu').index()+1;
        var rootIndex = $nav.parents('.mainbav').index();
        var hash = '#'+rootIndex+'-'+secondIndex+'-'+lastIndex;
        window.location.hash = hash;

        var rI = rootIndex;
        var sI = secondIndex;
        var lI = lastIndex
        var myid = $('nav .active').parents('.menu').children('a').attr('id')
        var myattr = $('nav .active').parents('.menu').children('a').attr('my-attr');
        myattr = "/interfaceapidoc/tpls/tpl_guide/"+myattr;
        $.ajax({
            url:myattr+'/'+myid+'.html',
            type:'GET',
            success:function(data){
                $('.container').html(data);
                var rhtml = $('nav .mainbav').eq(rootIndex-1).children('a').children('span').html();
                var shtml = $('nav .mainbav').eq(rootIndex-1).find('.menu').eq(secondIndex-1).children('a').children('span').html();
                var lhtml = $('nav .mainbav').eq(rootIndex-1).find('.menu').eq(secondIndex-1).find('.change').children('li').eq(lastIndex-1).children('a').html()
                var contitle = rhtml + '>' + shtml + '>' + lhtml
                $('.conTitle').html(contitle)
                // var goId = '#'+rI+'-'+sI+'-'+lI;
                // console.info(goId);
                // var actop =$(goId).offset().top;
                // $(window).scrollTop(actop);
            }
        })

    })

})
