//Agrega la locale es para todos los componentes pf
PrimeFaces.locales['es'] = { closeText: 'Cerrar', prevText: 'Anterior', nextText: 'Siguiente', monthNames: ['Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'], monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun','Jul','Ago','Sep','Oct','Nov','Dic'], dayNames: ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'], dayNamesShort: ['Dom','Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'], dayNamesMin: ['D','L','M','X','J','V','S'], weekHeader: 'Semana', firstDay: 1, isRTL: false, showMonthAfterYear: false, yearSuffix: '', timeOnlyTitle: 'Sólo hora', timeText: 'Tiempo', hourText: 'Hora', minuteText: 'Minuto', secondText: 'Segundo', currentText: 'Fecha actual', ampm: false, month: 'Mes', week: 'Semana', day: 'Día', allDayText : 'Todo el día' }; 

//Código ejecutado luego de cada submit
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