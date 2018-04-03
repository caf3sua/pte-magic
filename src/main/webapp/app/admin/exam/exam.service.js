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
            'update': { method:'PUT' }
        });
    }
})();
