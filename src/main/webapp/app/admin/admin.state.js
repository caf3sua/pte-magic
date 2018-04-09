(function () {
    'use strict';

    angular
        .module('pteMagicApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('admin', {
            abstract: true,
            parent: 'app',
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar_admin.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
        });
    }
})();
