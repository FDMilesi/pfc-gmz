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

//Cuando quiero cerras dos dialogs luego de un submit
function handleSubmitTwoDialogs(args, dialog1, dialog2) {
    var jqDialog = jQuery('#' + dialog1);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog1).hide();
        PF(dialog2).hide();
    }
}