function input_hint() {
    // placeholder text
    $('[placeholder]').focus(function () {
        var input = $(this);
        if (input.val() == input.attr('placeholder')) {
            input.val('');
            input.removeClass('placeholder');
        }
    }).blur(function () {
        var input = $(this);
        if (input.val() == '' || input.val() == input.attr('placeholder')) {
            input.addClass('placeholder');
            input.val(input.attr('placeholder'));
        }
    }).blur();
    $('[placeholder]').parents('form').submit(function () {
        $(this).find('[placeholder]').each(function () {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
            }
        })
    });

}

function focussearch() {$('#s').focus(); }

$(document).ready(function () {
    input_hint();

    //    // Animated Scroll
    //    $('a[href*=#]').click(function () {
    //        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '')
    //        && location.hostname == this.hostname) {
    //            var $target = $(this.hash);
    //            $target = $target.length && $target
    //          || $('[name=' + this.hash.slice(1) + ']');
    //            if ($target.length) {
    //                var targetOffset = $target.offset().top - 30;
    //                $('html,body')
    //            .animate({ scrollTop: targetOffset }, 750);
    //                return false;
    //            }
    //        }
    //    });

    $('.fancybox').fancybox({
        padding: 16,
        width: '70%',
        height: '80%',
        autoSize: false,      
        openEffect: 'fade'
    });

    $('.fancybox-auto').fancybox({
        padding: 16,
        autoSize: true,      
        openEffect: 'fade'
    });

    $('.form-search input[type=text]').focus(function () {
        $(this).animate({ width: '160px' }, 'normal');
    }).blur(function () {
        $(this).animate({ width: '128px' }, 'normal');
    });

    $('.nav li.screen').hover(function () {
        $(this).children('a').toggleClass('active');
        $(this).find('.menu').fadeIn('fast');
    }, function () {
        $(this).children('a').toggleClass('active');
        $(this).find('.menu').fadeOut('fast');
    });

    $('.nav li.compact a').click(function () {
        $(this).toggleClass('active');
        $(this).next('.menu').fadeToggle('fast');
    });

    $('ul.grid-main li.item .block').hover(function () {
        $(this).find('.block-description').slideToggle('fast');
    }, function () {
        $(this).find('.block-description').slideToggle('fast');
    });

    $('ul.grid-set li.item .block').hover(function () {
        $(this).find('.block-info-popup').fadeIn('fast');
    }, function () {
        $(this).find('.block-info-popup').fadeOut('fast');
    });

    $('.toolbar-size').click(function () {
        $('.toolbar-size').removeClass('active');
        $(this).addClass('active');
        size = $(this).attr('id');
        $('ul.grid-set li.item .block img').css('width', size);
    });

    $('a#grid').click(function () {
        $(this).toggleClass('active');
        $('.block-icon .item img').toggleClass('grid');
    });


/*    $('#slideshow').cycle({
        fx: 'scrollHorz',
        speed: '125',
        timeout: 0,
        prev: '.arrow-left',
        next: '.arrow-right',
        pager: '#itemnav',
        pagerAnchorBuilder: function (idx, slide) {
            // return sel string for existing anchor
            return '#itemnav li:eq(' + (idx) + ') a';
        }
    });*/

    if ($('#slideshow').children().length <= 1) {
        $('.arrow-left').hide();
        $('.arrow-right').hide();
        if ($('#slideshow').hasClass('upload-preview')) {

        } else {
            $('#itemnav').hide();
        }
    }

    $('.form-section input[type="text"], .form-section textarea').focus(function () {
        $(this).next().fadeIn('fast');
    }).blur(function () {
        $(this).next().fadeOut('fast');
    });

    $('.wpcf7-form-control-wrap input[type="text"], .wpcf7-form-control-wrap textarea').focus(function () {
        $(this).parents('.form-section').children('.hint').fadeIn('fast');
    }).blur(function () {
        $(this).parents('.form-section').children('.hint').fadeOut('fast');
    });


    $('.blog-content img').parents('p').addClass('block block-image');
});
