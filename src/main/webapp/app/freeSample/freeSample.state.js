(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('freeSample', {
            parent: 'app',
            url: '/freeSample',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/freeSample/freeSample.html',
                    controller: 'FreeSampleController',
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
