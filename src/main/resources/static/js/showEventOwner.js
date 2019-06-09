// Javascript to enable link to tab
let url = document.location.toString();
if (url.match('#')) {
    $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
}

// Change hash for page-reload
$('.nav-tabs a').on('shown.bs.tab', function (e) {
    window.location.hash = e.target.hash;
})

    $('.btn-rating').on('click', function (event) {
        event.preventDefault();
        const id = $(this).siblings( "#id_candidate" ).text();
        const url = `${window.location.origin}/events/candidate/${id}`;
        $.get(
            url,
            {},
            function(data) {
               $('#modal-rating-body').html(data);
            }
        );
        // if ($(this).text().trim() === "Aceitar")
        //     $('#modal-candidate-body').html(`<p class="my-0">Você deseja <span class="text-modal text-confirm">confirmar</span> esse candidato na equipe ?<p>
        //     <p class='modal-info'>Aceitar um usuário em uma vaga irá negar automaticamente todas as solicitações para outras vagas
        //     desse mesmo usuário para este evento.</p>`)
        // else
        //     $('#modal-candidate-body').html(`<p class="my-0">Você deseja <span class="text-modal text-cancel">negar</span> esse candidato na equipe ?<p>
        //     <p class='modal-info'>Aceitar um usuário em uma vaga irá negar automaticamente todas as solicitações para outras vagas
        //     desse mesmo usuário para este evento.</p>`)
        // let action = $(this).parent().attr('action');
        // $('#modal-form').attr('action', action);
        $('#modalRating').modal('show');
    });

$(".btn-candidate").on('click', function (event) {
    event.preventDefault();
    if ($(this).text().trim() === "Aceitar")
        $('#modal-candidate-body').html(`<p class="my-0">Você deseja <span class="text-modal text-confirm">confirmar</span> esse candidato na equipe ?<p>
<p class='modal-info'>Aceitar um usuário em uma vaga irá negar automaticamente todas as solicitações para outras vagas
desse mesmo usuário para este evento.</p>`)
    else
        $('#modal-candidate-body').html(`<p class="my-0">Você deseja <span class="text-modal text-cancel">negar</span> esse candidato na equipe ?<p>
<p class='modal-info'>Aceitar um usuário em uma vaga irá negar automaticamente todas as solicitações para outras vagas
desse mesmo usuário para este evento.</p>`)
    let action = $(this).parent().attr('action');
    $('#modal-form').attr('action', action);
    $('#modalCandidate').modal('show');
});

    