function handleSubmit(args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog).hide();
    }
}

//Listener de cambio de tamaño
var mediaMatchMax640 = window.matchMedia("(max-width: 40.063em)");
function mediaMatchMax640Listener(mediaQuery) {
    if (mediaQuery.matches) {
        //más chico que 640
        setTimeout(function () {
            if (PrimeFaces.widgets['agenda']) {
                var fullCal = PF('agenda').jq.children(':first');
//                fullCal.fullCalendar({header: false});
                fullCal.fullCalendar('changeView', 'agendaDay');
                $('.fc-right').css("display", "none");
            }
        }, 10);
    } else {
        //más grande que 640
        if (PrimeFaces.widgets['agenda']) {
            $('.fc-right').css("display", "block");
        }
    }
}
mediaMatchMax640.addListener(mediaMatchMax640Listener);