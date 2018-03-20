(function() {
    'use strict';
    angular
        .module('pteMagicApp')
        .factory('ExamType', ExamType);

    ExamType.$inject = ['$resource'];

    function ExamType ($resource) {
        var resourceUrl =  'api/exam-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getAllByType': {url: 'api/get-exam-by-types/:type', method: 'GET', isArray: true},
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
