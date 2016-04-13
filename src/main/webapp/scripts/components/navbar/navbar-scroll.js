var previousScroll = 0,
    headerOrgOffset = $('#header').height();

$('#header-wrap').height(headerOrgOffset);

$(window).scroll(function () {
    var currentScroll = $(this).scrollTop();

    //console.log(currentScroll + " and " + previousScroll + " and " + headerOrgOffset);

    if (currentScroll == 0) {
        $('#header-wrap').filter(':not(:animated)').slideDown();
    }

    if (currentScroll > headerOrgOffset) {
        if (currentScroll > previousScroll) {
            $('#header-wrap').filter(':not(:animated)').slideUp();
        } else {
            $('#header-wrap').filter(':not(:animated)').slideDown();
        }
    } else {
        $('#header-wrap').filter(':not(:animated)').slideDown();
    }
    previousScroll = currentScroll;


});