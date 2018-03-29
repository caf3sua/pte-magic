(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('mockTest', {
            parent: 'app',
            url: '/mockTest',
            data: {
                authorities: ['ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM']
            },
            views: {
                'content@': {
                    templateUrl: 'app/mockTest/mockTest.html',
                    controller: 'MockTestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
