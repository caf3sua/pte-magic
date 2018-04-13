// code style: https://github.com/johnpapa/angular-styleguide
// Create by: Nam, Nguyen Hoai - ITSOL.vn

(function() {
    '';
    angular
      .module('pteMagicApp')
      .controller('PteMagicBaseController', PteMagicBaseController);

    PteMagicBaseController.$inject = ['vm', '$scope', '$window', '$compile', '$timeout'];

    function PteMagicBaseController(vm, $scope, $window, $compile, $timeout){
		vm.message = { name: 'default entry from PteMagicBaseController' };


		// Attribute
		vm.showProgressBar = false;
		vm.countdownPercent = 0;
		vm.questionGroup;
		vm.counter;
		vm.showRecording = true;
		vm.answers = [];
		vm.selectedQuestion;
        $scope.models = {
	        selected: null,
	        lists: {"A": [], "B": []},
            fillInTheBlankQuestionArr: [],
            answer: [],
            fillInTheBlanklLists: {"questionPanel": []},
            startText: '',
            fillInTheBlankPartialTexts: []
	    };

		// Function
		vm.initBase = initBase;
		vm.getUserAnswer = getUserAnswer;
		vm.closeExam = closeExam;
		vm.updateQuestionInfo = updateQuestionInfo;
		vm.countdownToRecording = countdownToRecording;
    	vm.startRecording = startRecording;
        vm.dropCallback = dropCallback;
        vm.movedCallback = movedCallback;

    	function resetStatus() {
    		vm.showProgressBar = false;
    		vm.countdownPercent = 0;
    	}

    	function calProgress() {
    		vm.timeProgress = 0;
    		var intervalProgress = setInterval(function() {
    			vm.timeProgress++;
    			vm.countdownPercent = vm.timeProgress / 40 * 100;
    		    // Display 'counter' wherever you want to display it.
    		    if (vm.timeProgress == 40) {
    		        // Display a login box
    		        clearInterval(intervalProgress);
    		    }
    		}, 1000);
    	}

		function startRecording() {
			// Reset
	        resetStatus();

    		// start recording
	        if (!audioRecorder)
	            return;
	        audioRecorder.clear();
	        audioRecorder.record();
	        vm.btnEnable = true;
	        vm.txtInfoCountdown = "Recording ..."
            document.getElementById('pteBlockRecord').className = "pte-block-record";
        	vm.isRecording = true;

	        if (vm.selectedQuestion.type == 'SPEAKING_REPEAT_SENTENCE'
                || vm.selectedQuestion.type == 'SPEAKING_DESCRIBE_IMAGE'
                || vm.selectedQuestion.type == 'SPEAKING_RETELL_LECTURE'
                || vm.selectedQuestion.type == 'SPEAKING_ANSWER_SHORT_QUESTION') {
	        	vm.showProgressBar = true;
	        	calProgress();
	        }
    	}

		function countdownToRecording() {
    		if (vm.questionGroup == 'SPEAKING') {
    			if (vm.selectedQuestion.type == 'SPEAKING_REPEAT_SENTENCE' || vm.selectedQuestion.type == 'SPEAKING_RETELL_LECTURE' || vm.selectedQuestion.type == 'SPEAKING_ANSWER_SHORT_QUESTION') {
    				return;
    			}

    			console.log('countdownToRecording!');
        		vm.showRecording = true;

        		vm.counter = 5;
        		var interval = setInterval(function() {
        			vm.counter--;
        		    // Display 'counter' wherever you want to display it.
        		    if (vm.counter == 0) {
        		        // Display a login box
        		        clearInterval(interval);
        		        startRecording();
        		    }
        		}, 1000);
    		}
    	}

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
            if (selQuestion.type == 'READING_FIB_R') {
            	$scope.models = {
            	        selected: null,
            	        lists: {"A": [], "B": []},
                        fillInTheBlankQuestionArr: [],
                        answer: [],
                        fillInTheBlanklLists: {"questionPanel": []},
                        startText: '',
                        fillInTheBlankPartialTexts: []
            	    };

            	$scope.models.answer = {};
                $scope.models.selected = null;
                // selQuestion.description = selQuestion.description.replace(/@Blank@/g, '<input type="text" name="input" class="input_answer pte-writing-input"/>');
                //selQuestion.description.split('@Blank@').join('xxxxxxx');
                $scope.models.fillInTheBlanklLists.questionPanel = [];

                var count = (selQuestion.text.match(/@Blank@/g) || []).length;

                for (var i = 0; i < count; i++) {
                    var name = "answer" + i;
                    // $scope.models.answer[i] = {"answer": []};
                    $scope.models.answer[name] = {};
                    $scope.models.answer[name][i] = [];
                    $scope.models.fillInTheBlankQuestionArr.push($scope.models.answer[name]);
                }

                $scope.models.fillInTheBlanklLists.questionPanel.push({label: selQuestion.answerA, key: "A"});
                $scope.models.fillInTheBlanklLists.questionPanel.push({label: selQuestion.answerB, key: "B"});
                $scope.models.fillInTheBlanklLists.questionPanel.push({label: selQuestion.answerC, key: "C"});
                $scope.models.fillInTheBlanklLists.questionPanel.push({label: selQuestion.answerD, key: "D"});
                $scope.models.fillInTheBlanklLists.questionPanel.push({label: selQuestion.answerE, key: "E"});

                var partialTexts = selQuestion.text.split('@Blank@');
                if(partialTexts.length > count) {
                    $scope.models.startText = partialTexts[0];
                    partialTexts.splice(0, 1);
                    $scope.models.fillInTheBlankPartialTexts = partialTexts;
                } else if(partialTexts.length == count) {
                    if(selQuestion.text.indexOf('@Blank@') > 0) {
                        $scope.models.startText = partialTexts[0];
                        partialTexts.splice(0, 1);
                        $scope.models.fillInTheBlankPartialTexts = partialTexts;
                    } else {
                        $scope.models.fillInTheBlankPartialTexts = partialTexts;
                    }
                } else {
                    $scope.models.fillInTheBlankPartialTexts = partialTexts;
                    $scope.models.fillInTheBlankPartialTexts[$scope.models.fillInTheBlankPartialTexts.length] = '';
                }
            }

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
            if (selQuestion.type == 'LISTENING_HIGHLIGHT_INCORRECT_WORD') {
                parseTextToWords(selQuestion.description);
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
  				angular.forEach(arrAnswer, function(value, key){
  	  				vm.answers.push(value.key);
  	            });
  				console.log(vm.answers);
  			} else if (vm.selectedQuestion.type == 'READING_FIB_R_W') {
  				$('.select_READING_FIB_R_W').each(function(){
  					vm.answers.push($(this).find('option:selected').text());
  				});
  			} else if (vm.selectedQuestion.type == 'READING_FIB_R') {
  				var count = 0;
  				for (var name in $scope.models.answer) {
  				  if ($scope.models.answer.hasOwnProperty(name)) {
  					  var item = $scope.models.answer[name]
                      if(item[count] && item[count][0]) {
                          console.log(item[count][0].key);
                          vm.answers.push(item[count][0].key);
                          count++;
                      }
  				  }
  				}

  			} else if (vm.selectedQuestion.type == 'LISTENING_HIGHLIGHT_INCORRECT_WORD') {
  				$('.word-hightlight').each(function(){
  					if ($(this).hasClass('hightlight')) {
  						var answer = $.trim($(this).text());
  						vm.answers.push(answer);
  					}
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

        function parseTextToWords(textToClear){
            // basic interpunct chars should be single elements to click
            textToClear = textToClear.replace(/\./g, ' .');
            textToClear = textToClear.replace(/\?/g, ' ?');
            textToClear = textToClear.replace(/\!/g, ' !');
            textToClear = textToClear.replace(/\,/g, ' ,');
            textToClear = textToClear.replace(/\"/g, ' " ');
            // removing multiple spaces for single
            textToClear = textToClear.replace(/ +(?= )/g,'');

            var words = textToClear.split(" ");

            vm.selectedQuestion.description = '';

            // generate words with ids to change their future css
            for ( var i = 0, l = words.length; i < l; i++ ) {
                var word = $('<span onclick="hightlight(this)" class="word-hightlight"/>').attr({'id':'word'+i }).html(" "+words[i]);
                word.css('color','black');
                vm.selectedQuestion.description += word.prop('outerHTML');
            }
        }

        function dropCallback(index, item, external, type, list, listName) {
            var parentIndex = parseInt(listName);
            if(list[0]) {
                $scope.models.fillInTheBlanklLists.questionPanel.push(list[0]);
            }
            document.getElementById('drag-panel'+ parentIndex).className = "panel panel-info ";
            $scope.models.answer['answer' + listName][listName] = [item];
            // Return false here to cancel drop. Return true if you insert the item yourself.
            return item;
        };

        function movedCallback(index, list, listName) {
            var parentIndex = parseInt(listName);
            list.splice(index, 1)
            document.getElementById('drag-panel'+ parentIndex).className = "panel panel-info pte-position-top10";
        };
    }
})();
