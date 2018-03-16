
(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .factory('createAccountService', createAccountService);

    createAccountService.$inject = ['$resource'];

    function createAccountService ($resource) {

        var service = $resource('/api/users', {}, {
            'save': {
                method: 'POST',
                isArray: false
            },
        });

        return service;
    }
})();
