$(function(){
    $('.page a').each(function(){
        $(this).click(function(){
            $('.page a').removeClass('active');
            $(this).addClass('active')
        })
    });
    $('.previous').click(function(){
        var i = $('.active').parent('li').index()-1
        if(i<0){
            i=0
        }
        $('.page a').removeClass('active');
        $('.page li').eq(i).find('a').addClass('active')
        
    })
    $('.next').click(function(){

        var i = $('.active').parent('li').index()+1;
        var j = $('.page li').length-1;
        if(i>j){
            i=j;
        }
        $('.page a').removeClass('active');
        $('.page li').eq(i).find('a').addClass('active')
    })



    $('.blue').click(function(){
        $('link').attr('href','/html/css/index_blue.css');
        $('.changeColor a').show();
        $(this).hide();
    })
    $('.yellow').click(function(){
        $('link').attr('href','/html/css/index_yellow.css');
        $('.changeColor a').show();
        $(this).hide();
    })
    $('.red').click(function(){
        $('link').attr('href','/html/css/index_red.css');
        $('.changeColor a').show();
        $(this).hide();
    })
    $('.green').click(function(){
        $('link').attr('href','/html/css/index_green.css');
        $('.changeColor a').show();
        $(this).hide();
    })
})
