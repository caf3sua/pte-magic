(function () {
    'use strict';

    angular
        .module('pteMagicApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ url: 'api/users/:id', method:'DELETE'},
            'getAll':{ url: 'api/users/get-all', method:'GET', isArray: true},
        });

        return service;
    }
})();
