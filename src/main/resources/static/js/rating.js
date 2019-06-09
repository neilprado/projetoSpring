$(document).ready(function () {

    if($('#rated').length){
        const nota = $('#rated').text();
        defaultStart(nota);
    }

    $('#stars li')
        .on('mouseover', function () {
            const onStar = parseInt($(this).data('value'), 10); 

            $(this)
                .parent()
                .children('li.star')
                .each(function (e) {
                    if (e < onStar) {
                        $(this).addClass('hover');
                    } else {
                        $(this).removeClass('hover');
                    }
                });
        })
        .on('mouseout', function () {
            $(this)
                .parent()
                .children('li.star')
                .each(function (e) {
                    $(this).removeClass('hover');
                });
        });

    $('#stars li').on('click', function () {
        const onStar = parseInt($(this).data('value'), 10); 
        const stars = $(this)
            .parent()
            .children('li.star');

        for (i = 0; i < stars.length; i++) {
            $(stars[i]).removeClass('selected');
        }

        for (i = 0; i < onStar; i++) {
            $(stars[i]).addClass('selected');
        }

        const ratingValue = parseInt(
            $('#stars li.selected')
                .last()
                .data('value'),
            10
        );

        $("#rating_value").val(ratingValue);
        responseMessage(ratingValue);
    });
});

function defaultStart(ratingValue){
    const stars = $("#stars li");

    for (i = 0; i < stars.length; i++) {
        $(stars[i]).removeClass('selected');
    }

    for (i = 0; i < ratingValue; i++) {
        $(stars[i]).addClass('selected');
    }
    responseMessage(ratingValue);
}

function responseMessage(ratingValue) {
    if (ratingValue > 1) {
        msg = 'Obrigado! Você deu ' + ratingValue + ' estrelas.';
    } else {
        msg =
            'Que pena. Você deu ' +
            ratingValue +
            ' estrela.';
    }
    $('.success-box div.text-message').html('<span>' + msg + '</span>');
    $(".success-box").removeClass('hidden');
}