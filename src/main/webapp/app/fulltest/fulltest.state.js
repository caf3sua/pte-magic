(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('fulltest', {
            parent: 'app',
            url: '/fulltest?type',
            data: {
                authorities: []
            },
            views: {
            	'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar_blank.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                'content@': {
                    templateUrl: 'app/fulltest/fulltest.html',
                    controller: 'FullTestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('testType');
                    $translatePartialLoader.addPart('questionType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Exam', function($stateParams, Exam) {
                    return Exam.startExams({examTypeId : $stateParams.type}).$promise;
                }]
            }
        });
    }
})();
