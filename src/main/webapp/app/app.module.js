(function() {
    'use strict';

    angular
        .module('pteMagicApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ngSanitize',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'timer',
            'ngAudio',
            'dndLists',
            'smart-table',
            'cp.ngConfirm'
//            'angularAudioRecorder'
        ])
        .run(run);
//        .config(['recorderServiceProvider', function(recorderServiceProvider){
//        	recorderServiceProvider
//	        	.forceSwf(false)
//	            //.setSwfUrl('/lib/recorder.swf')
//	            .withMp3Conversion(true)
//        }]);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
