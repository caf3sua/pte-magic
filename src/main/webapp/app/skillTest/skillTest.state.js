(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('skill-test', {
            parent: 'app',
            url: '/skill-test',
            data: {
                authorities: ['ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM']
            },
            views: {
                'content@': {
                    templateUrl: 'app/skillTest/skillTest.html',
                    controller: 'SkillTestController',
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
