(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FreeSampleController', FreeSampleController);

    FreeSampleController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope'];

    function FreeSampleController ($scope, Principal, LoginService, $state, $rootScope) {
        var vm = this;
    }
})();
