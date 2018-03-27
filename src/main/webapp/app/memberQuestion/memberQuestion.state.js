(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('memberQuestion', {
            parent: 'app',
            url: '/memberQuestion',
            data: {
                authorities: ['ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM']
            },
            views: {
                'content@': {
                    templateUrl: 'app/memberQuestion/memberQuestion.html',
                    controller: 'MemberQuestionController',
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
