﻿/* English/UK initialisation for the jQuery UI date picker plugin. */
/* Written by Stuart. */
(function($) {
        $.datepicker.regional = {
                renderer: $.datepicker.defaultRenderer,
                monthNames: ['January','February','March','April','May','June','July','August','September','October','November','December'],
                monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
                dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                dayNamesMin: ['Su','Mo','Tu','We','Th','Fr','Sa'],
                dateFormat: 'dd/mm/yyyy',
                firstDay: 1,
                prevText: '&#x3c;&#x3c;', prevStatus: '',
                prevJumpText: '&#x3c;&#x3c;', prevJumpStatus: '',
                nextText: '&#x3e;&#x3e;', nextStatus: '',
                nextJumpText: '&#x3e;&#x3e;', nextJumpStatus: '',
                currentText: 'Current', currentStatus: '',
                todayText: 'Today', todayStatus: '',
                clearText: 'Clear', clearStatus: '',
                closeText: 'Done', closeStatus: '',
                yearStatus: '', monthStatus: '',
                weekText: 'Wk', weekStatus: '',
                dayStatus: 'DD d MM',
                defaultStatus: '',
                isRTL: false
        };
        $.extend($.datepicker.defaults, $.datepicker.regional);
})(jQuery);