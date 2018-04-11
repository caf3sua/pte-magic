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
		vm.showRecording = true;
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
		vm.updateQuestionInfo = updateQuestionInfo;
		
		function buildSelectElement(answer) {
  			var arrAnswer = answer.split('/');
			var optTmp = '';
			angular.forEach(arrAnswer, function (data) {
					optTmp = optTmp + "<option>" + data + "</option>";
			});
			var sel = '<select name="select" class="select_READING_FIB_R_W"><option value=""></option>' + optTmp + '</select>';
			return sel;
  		}
		
	    // add any other shared functionality here.
		function initBase() {
			console.log('init base');
		}
		
		function closeExam() {
  			$window.close();
  		}
		
		function updateQuestionInfo(selQuestion) {
  			// Replace @Blank@
  			if (selQuestion.type == 'LISTENING_FIB_L') {
  				selQuestion.description = selQuestion.description.replace(/@Blank@/g, '<input type="text" name="input" class="input_answer pte-writing-input"/>');
  				//selQuestion.description.split('@Blank@').join('xxxxxxx');
  			}
  			
  			if (selQuestion.type == 'READING_FIB_R_W') {
  				if (selQuestion.answerA != "" && selQuestion.answerA != null) {
  					var txt = buildSelectElement(selQuestion.answerA);
  					selQuestion.text = selQuestion.text.replace(/@Blank@/, txt);
  				}
  				if (selQuestion.answerB != "" && selQuestion.answerB != null) {
  					var txt = buildSelectElement(selQuestion.answerB);
  					selQuestion.text = selQuestion.text.replace(/@Blank@/, txt);
  				}
  				if (selQuestion.answerC != "" && selQuestion.answerC != null) {
  					var txt = buildSelectElement(selQuestion.answerC);
  					selQuestion.text = selQuestion.text.replace(/@Blank@/, txt);
  				}
  				if (selQuestion.answerD != "" && selQuestion.answerD != null) {
  					var txt = buildSelectElement(selQuestion.answerD);
  					selQuestion.text = selQuestion.text.replace(/@Blank@/, txt);
  				}
  				if (selQuestion.answerE != "" && selQuestion.answerE != null) {
  					var txt = buildSelectElement(selQuestion.answerE);
  					selQuestion.text = selQuestion.text.replace(/@Blank@/, txt);
  				}
  			}
  			
  			// Update re-order
  			if (selQuestion.type == 'READING_RE_ORDER_PARAGRAPH') {
  				$scope.models.lists.A = [];
  				$scope.models.lists.B = [];
  				$scope.models.selected = null;
  				// Build models
  				if (selQuestion.answerA != "" && selQuestion.answerA != null) {
  					$scope.models.lists.A.push({label: selQuestion.answerA, key: "A"});
  				}
  				if (selQuestion.answerB != "" && selQuestion.answerB != null) {
  					$scope.models.lists.A.push({label: selQuestion.answerB, key: "B"});
  				}
  				if (selQuestion.answerC != "" && selQuestion.answerC != null) {
  					$scope.models.lists.A.push({label: selQuestion.answerC, key: "C"});
  				}
  				if (selQuestion.answerD != "" && selQuestion.answerD != null) {
  					$scope.models.lists.A.push({label: selQuestion.answerD, key: "D"});
  				}
  				if (selQuestion.answerE != "" && selQuestion.answerE != null) {
  					$scope.models.lists.A.push({label: selQuestion.answerE, key: "E"});
  				}
  			}
  			if (selQuestion.type == 'SPEAKING_REPEAT_SENTENCE' || selQuestion.type == 'SPEAKING_RETELL_LECTURE' || selQuestion.type == 'SPEAKING_ANSWER_SHORT_QUESTION') {
  				vm.showRecording = false;
  			}
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
  			} else if (vm.selectedQuestion.type == 'READING_FIB_R_W') {
  				$('.select_READING_FIB_R_W').each(function(){
  					vm.answers.push($(this).find('option:selected').text());
  				});
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
