// code style: https://github.com/johnpapa/angular-styleguide
// Create by: Nam, Nguyen Hoai - ITSOL.vn

(function() {
    '';
    angular
      .module('pteMagicApp')
      .controller('PteMagicBaseController', PteMagicBaseController);

    PteMagicBaseController.$inject = ['vm', '$scope', '$window', '$compile', '$timeout', 'PTE_SETTINGS'];

    function PteMagicBaseController(vm, $scope, $window, $compile, $timeout, PTE_SETTINGS){
		vm.message = { name: 'default entry from PteMagicBaseController' };


		// Attribute
		vm.currentQuestion = 0;
		vm.totalQuestion = 0;
		vm.currentSKill = '';
		vm.countAudio = 3;
		
		// inteval
		vm.intervalAudio;
		vm.intervalCounter;
		vm.intervalToRecording;
		
		vm.countdown = 60; // 2min10second
		vm.showProgressBar = false;
		vm.countdownPercent = 0;
		vm.questionGroup;
		vm.counter;
		vm.showRecording = true;
		vm.answers = [];
		vm.selectedQuestion;
        $scope.models = {
	        selected: null,
	        lists: {"Source": [], "Target": []},
            fillInTheBlankQuestionArr: [],
            answer: [],
            fillInTheBlanklLists: {"questionPanel": []},
            startText: '',
            fillInTheBlankPartialTexts: []
	    };
        vm.intervalProgress;
        
		// Function
		vm.initBase = initBase;
		vm.getUserAnswer = getUserAnswer;
		vm.closeExam = closeExam;
		vm.updateQuestionInfo = updateQuestionInfo;
		vm.countdownToRecording = countdownToRecording;
    	vm.startRecording = startRecording;
        vm.dropCallback = dropCallback;
        vm.movedCallback = movedCallback;
        vm.resetProgressStatus = resetProgressStatus;
        vm.initCountQuestion = initCountQuestion;

    	function resetProgressStatus() {
    		vm.showProgressBar = false;
    		vm.countdownPercent = 0;
    		vm.timeProgress = 0;
    	}
    	
    	function initCountQuestion() {
    		// Speaking -> Writing -> Reading -> Listening
    		// A: Speaking/Writing
    		// B: Reading/Listening
    		// init
    		if (vm.currentSKill == '') {
    			if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A') {
    				vm.currentSKill = 'SPEAKING'; // writing
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionSpeaking;
    			} else if (vm.exam.examTypeDTO.type == 'MOCK_TEST_B') {
    				vm.currentSKill = 'READING'; // listening
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionReading;
    			} else {
    				vm.currentSKill = 'SPEAKING';
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionSpeaking;
    			}
    			
    			vm.countdown = 40 * 60;
				$scope.$broadcast('timer-stop');
				$scope.$broadcast('timer-start');
    		} else if (vm.currentSKill == 'SPEAKING') {
    			// Part A + Full
    			if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A') {
    				vm.currentSKill = 'WRITING'; // writing
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionWriting;
    			} else {
    				vm.currentSKill = 'WRITING';
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionWriting;
    			}
    		} else if (vm.currentSKill == 'WRITING') {
    			// Part A + Full
    			if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A') {
    				vm.currentSKill = 'END'; // writing
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionWriting;
    			} else {
    				vm.currentSKill = 'READING';
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionReading;
    			}
    		} else if (vm.currentSKill == 'READING') {
    			// Part B + Full
    			vm.countdown = 40 * 60;
				$scope.$broadcast('timer-stop');
				$scope.$broadcast('timer-start');
				
    			if (vm.exam.examTypeDTO.type == 'MOCK_TEST_B') {
    				vm.currentSKill = 'LISTENING'; // writing
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionListening;
    			} else {
    				vm.currentSKill = 'LISTENING';
    				vm.currentQuestion = 0;
    				vm.totalQuestion = vm.exam.numberQuestionListening;
    			}
//    		} else if (vm.currentSKill == 'LISTENING') {
    			// Part B + Full
    			
    		}
    	}

    	function calProgress() {
    		vm.timeProgress = 0;
    		vm.intervalProgress = setInterval(function() {
    			vm.timeProgress++;
    			if( vm.selectedQuestion.type == 'SPEAKING_READ_ALOUD'){
                    vm.countdownPercent = vm.timeProgress / 30 * 100;
                    console.log('countdownPercent:' + vm.countdownPercent);
                    if (vm.timeProgress == 30) {
                        console.log('timeProgress:' + vm.timeProgress);
                        // Display a login box
                        clearInterval(vm.intervalProgress);
                        vm.answer();
                    }
                }else if(vm.selectedQuestion.type == 'SPEAKING_REPEAT_SENTENCE' || vm.selectedQuestion.type == 'SPEAKING_ANSWER_SHORT_QUESTION'){
                    vm.countdownPercent = vm.timeProgress / 10 * 100;
                    console.log('countdownPercent:' + vm.countdownPercent);
                    if (vm.timeProgress == 10) {
                        console.log('timeProgress:' + vm.timeProgress);
                        // Display a login box
                        clearInterval(vm.intervalProgress);
                        vm.answer();
                    }
                }else{
                    vm.countdownPercent = vm.timeProgress / 40 * 100;
                    console.log('countdownPercent:' + vm.countdownPercent);
                    if (vm.timeProgress == 40) {
                        console.log('timeProgress:' + vm.timeProgress);
                        // Display a login box
                        clearInterval(vm.intervalProgress);
                        vm.answer();
                    }
                }
    		    // Display 'counter' wherever you want to display it.

    		}, 1000);
    	}

		function startRecording() {
			// Reset
			resetProgressStatus();

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
                || vm.selectedQuestion.type == 'SPEAKING_ANSWER_SHORT_QUESTION'
                || vm.selectedQuestion.type == 'SPEAKING_READ_ALOUD'
            ) {
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
                if(vm.selectedQuestion.type == 'SPEAKING_DESCRIBE_IMAGE'){
                    vm.counter = PTE_SETTINGS.COUNT_DOWN_TIME_SPEAKING_DESCRIBE_IMAGE; // 25
                }else{
                    vm.counter = PTE_SETTINGS.COUNT_DOWN_TIME_SPEAKING_OTHER; // 30
                }

        		vm.intervalToRecording = setInterval(function() {
        			vm.counter--;

                    // Beep sound
                    if (vm.counter == 1) {
                        $("#player1")[0].play();
                    }

        		    // Display 'counter' wherever you want to display it.
        		    if (vm.counter == 0) {
        		        // Display a login box
        		        clearInterval(vm.intervalToRecording);
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
            	        lists: {"Source": [], "Target": []},
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
  				$scope.models.lists.Source = [];
  				$scope.models.lists.Target = [];
  				$scope.models.selected = null;
  				// Build models
  				if (selQuestion.answerA != "" && selQuestion.answerA != null) {
  					$scope.models.lists.Source.push({label: selQuestion.answerA, key: "A"});
  				}
  				if (selQuestion.answerB != "" && selQuestion.answerB != null) {
  					$scope.models.lists.Source.push({label: selQuestion.answerB, key: "B"});
  				}
  				if (selQuestion.answerC != "" && selQuestion.answerC != null) {
  					$scope.models.lists.Source.push({label: selQuestion.answerC, key: "C"});
  				}
  				if (selQuestion.answerD != "" && selQuestion.answerD != null) {
  					$scope.models.lists.Source.push({label: selQuestion.answerD, key: "D"});
  				}
  				if (selQuestion.answerE != "" && selQuestion.answerE != null) {
  					$scope.models.lists.Source.push({label: selQuestion.answerE, key: "E"});
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
  				var arrAnswer = $scope.models.lists.Target;
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
