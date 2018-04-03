(function() {
    'use strict';
    angular
        .module('pteMagicApp')
        .factory('Answer', Answer);

    Answer.$inject = ['$resource'];

    function Answer ($resource) {
        var resourceUrl =  'api/answers/:id';

        return $resource(resourceUrl, {}, {
        	'updateStatus': { url: 'api/answer-update-status',  method: 'PUT'},
            'query': { method: 'GET', isArray: true},
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
