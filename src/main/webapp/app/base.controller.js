// code style: https://github.com/johnpapa/angular-styleguide 
// Create by: Nam, Nguyen Hoai - ITSOL.vn

(function() {
    '';
    angular
      .module('pteMagicApp')
      .controller('PteMagicBaseController', PteMagicBaseController);
    
    PteMagicBaseController.$inject = ['vm', '$scope', '$window'];

    function PteMagicBaseController(vm, $scope, $window){
		vm.message = { name: 'default entry from PteMagicBaseController' };

		
		// Attribute 
		vm.answers = [];
		vm.selectedQuestion;
        $scope.models = {
	        selected: null,
	        lists: {"A": [], "B": []},
	    };
		
		// Function
		vm.initBase = initBase;
		vm.getUserAnswer = getUserAnswer;
		vm.closeExam = closeExam;
		
		
	    // add any other shared functionality here.
		function initBase() {
			console.log('init base');
		}
		
		function closeExam() {
  			$window.close();
  		}
		
		function getUserAnswer() {
  			vm.answers = [];

  			if (vm.selectedQuestion.type == 'WRITING_SUMMARIZE_WRITTEN_TEXT' || vm.selectedQuestion.type == 'WRITING_ESSAY') {
  				var answer = $('#areaTextWriting').val();
  				vm.answers.push(answer);
  			} else if (vm.selectedQuestion.type == 'LISTENING_FIB_L') {
  				$('.input_answer').each(function(){
  					vm.answers.push($(this).val());
  				 });
  			} else if (vm.selectedQuestion.type == 'READING_RE_ORDER_PARAGRAPH') {
  				var arrAnswer = $scope.models.lists.B;
  				console.log(arrAnswer);
  				angular.forEach(arrAnswer, function(value, key){
  	  				vm.answers.push(value.key);
  	            });
  				console.log(vm.answers);
  			} else {
  				angular.forEach(vm.listItemAnswer, function(value, key){
  	  				if ($('#answer' + value).is(":checked")) {
  	  	  				vm.answers.push(value);
  	  	  			}
  	  				$("#answer" + value).prop( "checked", false );
  	            });
  			}
  		}
    }
})();
