(function() {
    'use strict';
    angular
        .module('pteMagicApp')
        .factory('Exam', Exam);

    Exam.$inject = ['$resource'];

    function Exam ($resource) {
        var resourceUrl =  'api/exams/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'resetLimitTest': {url: 'api/reset-limit-exam/:userId', method: 'GET'},
            'startExams': {url: 'api/start-exam', method: 'POST'},
            'finishExam': {url: 'api/finish-exam', method: 'POST'},
            'finishMarkingExam': {url: 'api/finish-marking-exam', method: 'POST'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getAll': {
                method: 'GET',
                url: 'api/exams-all',
                isArray: true
            },
            'removeAll': {url: 'api/exams-remove-all', method: 'POST'},
            'update': { method:'PUT' }
        });
    }
})();
