
(function() {
    'use strict';

    angular
        .module('testOnlineApp')
        .factory('createAccountService', createAccountService);

    createAccountService.$inject = ['$resource'];

    function createAccountService ($resource) {

        var service = $resource('api/step/:botId', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'update':{
                url:'api/step',
                method:'PUT'
            },
            'deleteStep': {
                url: 'api/step/:botId/:stepId',
                method: 'DELETE'
            },
            'updateLocationStep':{
                url:'api/step/updateLocationStep',
                method:'PUT'
            },
            'getListExpression':{
                url:'api/dictionary/listExpression',
                method:'GET',
                isArray: true
            },
            'getListVariable':{
                url:'api/dictionary/listVariable',
                method:'GET',
                isArray: true
            }
        });

        return service;
    }
})();
