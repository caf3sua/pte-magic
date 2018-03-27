(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exam', {
            parent: 'admin',
            url: '/exam?page&sort&search',
            data: {
                authorities: [],
                pageTitle: 'pteMagicApp.exam.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/exam/exams.html',
                    controller: 'ExamController',
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
                    $translatePartialLoader.addPart('exam');
                    $translatePartialLoader.addPart('progressType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exam-detail', {
            parent: 'exam',
            url: '/exam/{id}',
            data: {
                authorities: [],
                pageTitle: 'pteMagicApp.exam.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/exam/exam-detail.html',
                    controller: 'ExamDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('exam');
                    $translatePartialLoader.addPart('progressType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Exam', function($stateParams, Exam) {
                    return Exam.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exam',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exam-detail.edit', {
            parent: 'exam-detail',
            url: '/detail/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam/exam-dialog.html',
                    controller: 'ExamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Exam', function(Exam) {
                            return Exam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam.new', {
            parent: 'exam',
            url: '/new',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam/exam-dialog.html',
                    controller: 'ExamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                examTypeId: null,
                                result: null,
                                scoreWriting: null,
                                scoreListening: null,
                                scoreReading: null,
                                scoreSpeaking: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('exam', null, { reload: 'exam' });
                }, function() {
                    $state.go('exam');
                });
            }]
        })
        .state('exam.edit', {
            parent: 'exam',
            url: '/{id}/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam/exam-dialog.html',
                    controller: 'ExamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Exam', function(Exam) {
                            return Exam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam', null, { reload: 'exam' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam.delete', {
            parent: 'exam',
            url: '/{id}/delete',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/exam/exam-delete-dialog.html',
                    controller: 'ExamDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Exam', function(Exam) {
                            return Exam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam', null, { reload: 'exam' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
