// code style: https://github.com/johnpapa/angular-styleguide 
// Create by: Nam, Nguyen Hoai - ITSOL.vn

(function() {
    'use strict';
    angular
      .module('pteMagicApp')
      .controller('PteMagicBaseController', PteMagicBaseController);
    
    //PteMagicBaseController.$inject = ['$scope'];

    function PteMagicBaseController(vm, ProfileService){
    		vm.message = { name: 'default entry from PteMagicBaseController' };

		ProfileService.getProfileInfo().then(function(response) {
            if (response.ribbonEnv) {
                console.log(response);
            }
        });
    	    // add any other shared functionality here.
    		
    		
    }
})();
