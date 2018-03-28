(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exam-type', {
            parent: 'admin',
            url: '/exam-type?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'pteMagicApp.examType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/exam-type/exam-types.html',
                    controller: 'ExamTypeController',
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
                search: null
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
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('examType');
                    $translatePartialLoader.addPart('testType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exam-type-detail', {
            parent: 'exam-type',
            url: '/exam-type/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'pteMagicApp.examType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/exam-type/exam-type-detail.html',
                    controller: 'ExamTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('examType');
                    $translatePartialLoader.addPart('testType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExamType', function($stateParams, ExamType) {
                    return ExamType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exam-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exam-type-detail.edit', {
            parent: 'exam-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam-type/exam-type-dialog.html',
                    controller: 'ExamTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['ExamType', function(ExamType) {
                            return ExamType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-type.new', {
            parent: 'exam-type',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam-type/exam-type-dialog.html',
                    controller: 'ExamTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                numberQuestionWriting: null,
                                numberQuestionReading: null,
                                numberQuestionListening: null,
                                numberQuestionSpeaking: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('exam-type', null, { reload: 'exam-type' });
                }, function() {
                    $state.go('exam-type');
                });
            }]
        })
        .state('exam-type.edit', {
            parent: 'exam-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam-type/exam-type-dialog.html',
                    controller: 'ExamTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExamType', function(ExamType) {
                            return ExamType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-type', null, { reload: 'exam-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-type.delete', {
            parent: 'exam-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam-type/exam-type-delete-dialog.html',
                    controller: 'ExamTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExamType', function(ExamType) {
                            return ExamType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-type', null, { reload: 'exam-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
