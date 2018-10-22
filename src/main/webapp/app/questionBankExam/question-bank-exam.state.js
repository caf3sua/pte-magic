(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('question-bank', {
            parent: 'app',
            url: '/question-bank?type',
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
                    templateUrl: 'app/questionBankExam/question-bank-exam.html',
                    controller: 'QuestionBankExamController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null,
                selectedSkill:null
            },
            resolve: {
            	pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('testType');
                    $translatePartialLoader.addPart('questionType');
                    return $translate.refresh();
                }]
//                entity: ['$stateParams', 'Exam', function($stateParams, Exam) {
//                    return Exam.startExams({examTypeId : $stateParams.type}).$promise;
//                }]
            }
        });
    }
})();
