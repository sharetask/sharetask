/* Slovak initialisation for the jQuery UI date picker plugin. */
/* Written by Vojtech Rinik (vojto@hmm.sk). */
(function($) {
        $.datepicker.regional = {
                renderer: $.datepicker.defaultRenderer,
                monthNames: ['Január','Február','Marec','Apríl','Máj','Jún','Júl','August','September','Október','November','December'],
                monthNamesShort: ['Jan','Feb','Mar','Apr','Máj','Jún','Júl','Aug','Sep','Okt','Nov','Dec'],
                dayNames: ['Nedel\'a','Pondelok','Utorok','Streda','Štvrtok','Piatok','Sobota'],
                dayNamesShort: ['Ned','Pon','Uto','Str','Štv','Pia','Sob'],
                dayNamesMin: ['Ne','Po','Ut','St','Št','Pia','So'],
                dateFormat: 'dd.mm.yyyy',
                firstDay: 0,
                prevText: '&#x3c;&#x3c;', prevStatus: '',
                prevJumpText: '&#x3c;&#x3c;', prevJumpStatus: '',
                nextText: '&#x3e;&#x3e;', nextStatus: '',
                nextJumpText: '&#x3e;&#x3e;', nextJumpStatus: '',
                currentText: 'Dnes', currentStatus: '',
                todayText: 'Dnes', todayStatus: '',
                clearText: '-', clearStatus: '',
                closeText: 'Zavrieť', closeStatus: '',
                yearStatus: '', monthStatus: '',
                weekText: 'Ty', weekStatus: '',
                dayStatus: 'DD d MM',
                defaultStatus: '',
                isRTL: false
        };
        $.extend($.datepicker.defaults, $.datepicker.regional);
})(jQuery);