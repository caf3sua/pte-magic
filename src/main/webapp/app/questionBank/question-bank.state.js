(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('questionBank', {
            parent: 'app',
            url: '/questionBank',
            data: {
                authorities: ['ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM']
            },
            views: {
                'content@': {
                    templateUrl: 'app/questionBank/question-bank-main.html',
                    controller: 'QuestionBankMainController',
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
