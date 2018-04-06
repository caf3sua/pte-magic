(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('login', {
            parent: 'admin',
            url: '/admin',
            data: {
                authorities: [],
                pageTitle: 'global.menu.admin.main'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/login/login.html',
                    controller: 'AdminLoginController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('login');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
