// (function() {
//     'use strict';
//
//     angular
//         .module('pteMagicApp')
//         .config(stateConfig);
//
//     stateConfig.$inject = ['$stateProvider'];
//
//     function stateConfig($stateProvider) {
//         parent: 'home',
//             data: {},
//         onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//             $uibModal.open({
//                 templateUrl: 'app/components/createAccount/createAccount.html',
//                 controller: 'createAccountController',
//                 controllerAs: 'vm'
//                 backdrop: 'static',
//                 size: 'lg'
//             }).result.then(function() {
//                 $state.go('home', {}, { reload: false });
//             }, function() {
//                 $state.go('^');
//             });
//         }]
//
//
//     }
// })();
