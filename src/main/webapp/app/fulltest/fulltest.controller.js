(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FullTestController', FullTestController);

    FullTestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
    	, '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce', 'entity'];

    function FullTestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
    		, $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce, entity) {

    	var vm = this;
    	// Function
    	vm.answer = answer;
    	vm.trustAsHtml = $sce.trustAsHtml;

    	// Variable, flag
    	vm.examTypeId;
    	vm.exam = entity;
    	vm.questions = entity.questions;
    	vm.selectedQuestion;
    	vm.answers = [];
    	vm.isFinish = false;
    	vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E'];
    	vm.audio;
    	vm.fileUpload;
    	vm.btnEnable = true;
    	vm.toggleRecording = toggleRecording;
    	vm.startNewPart = startNewPart;
        vm.checkClickspell = true;
        vm.spellCheck = spellCheck;
        vm.UpdateLengths = UpdateLengths;
    	vm.txtInfoCountdown = "Begining in ";
    	vm.countdownRecording = 5;
    	vm.isRecording = false;
        vm.CharacterLength = 0;
        vm.WORDS_MAXIMUM = 10; // changeable
        vm.WordsLength=0;
        vm.Text = "";
        vm.countAudio = 3;
        vm.txtStatusAudio = 'Playing';
        vm.checkAudioSeconds = true;
        vm.checkStatusPlay = false;
    	vm.countdownTimebreak = 600;

    	vm.btnTxt = 'Next';

    	function startNewPart() {
    		initAnswer();

    		nextQuestion();
    	}

    	function stopRecording() {
    		// stop recording
	        audioRecorder.stop();
	        audioRecorder.getBuffers( gotBuffers );
	        vm.btnEnable = true;
    	}

    	function initPlayer() {
    		var audio = $("#player");
    		if (audio[0] != undefined) {
    			audio[0].addEventListener('ended', callBackAudioEnded);
    		}
    	}

    	function callBackAudioEnded() {
    		console.log('play audio ended!');
    		vm.showRecording = true;
            vm.txtStatusAudio = 'Completed';
    		vm.counter = 5

            // Beep sound
            $("#player1")[0].play();

    		var interval = setInterval(function() {
    			vm.counter--;
    		    // Display 'counter' wherever you want to display it.
    		    if (vm.counter == 0) {
    		        // Display a login box
    		        clearInterval(interval);
    		        vm.startRecording();
    		    }
    		}, 1000);
    	}

    	function playAudio(link, timeout) {
    		if (link == null || link == "") {
    			return;
    		}
            vm.checkAudioSeconds = false;
            vm.checkStatusPlay = true;
    		var audio = $("#player");
    		if (audio[0] != undefined) {
    			audio[0].addEventListener('ended', callBackAudioEnded);
        		console.log('Play audio: ' + link);
    			$("#mp3_src").attr("src", link); // https://storage.googleapis.com/pte-magic-2018/1523480876666_HCS1.mp3
                audio[0].pause();
                audio[0].load();

                $timeout(function(){
                	audio[0].play();
                }, timeout );
    		}

    	}

    	function initAnswer() {
    		// Stop audio
    		var audio = $("#player");
    		if (audio[0] != undefined) {
    			audio[0].pause();
    		}


  			vm.txtInfoCountdown = "Begining in ";
  	    	vm.isRecording = false;
    	}

    	angular.element(document).ready(function () {
//    		$timeout(function(){
//	    		// Load player
//	    		initPlayer();
//
//	    		// Load record audio
//	    		initAudio();
//    		}, 1000 );
        });

    	function initMockTest() {
    		if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A') {
    			vm.countdown = 110 * 60;
    			vm.countdown = 30;
    		} else if (vm.exam.examTypeDTO.type == 'MOCK_TEST_B') {
    			vm.countdown = 90 * 60;
    			vm.countdown = 50;
    		} else {
    			vm.countdown = 200 * 60;
    		}
    	}

    	// Init controller
  		(function initController() {
  			// instantiate base controller
  		    $controller('PteMagicBaseController', { vm: vm, $scope: $scope });

  			// Load player
    		initPlayer();

    		// Load record audio
    		initAudio();

        	// Init mocktest
        	// initMockTest();
        	
        	vm.initCountQuestion();

        	// Next question
        	nextQuestion();
  		})();

  		function answer() {
  			initAnswer();

  			// Upload if questionGroup == SPEAKING
  			if (vm.questionGroup == 'SPEAKING') {
  				stopRecording();
  				uploadRecording(vm.selectedQuestion.id);
  			} else {
  				console.log(vm.selectedQuestion);
  	  			// Get answer
  	  			vm.getUserAnswer();
  	  			console.log(vm.answers);

  	  			// Save answer
  	  			saveAnswer();
  			}

  			// Next question
  			nextQuestion();
  		}

  		$scope.$watch('vm.fileUpload', function (file) {
  			var file;
  			if (file) {
              file.upload = Upload.upload({
                  url: '/api/file/upload/answer',
                  data: {file: vm.fileUpload},
                  ignoreLoadingBar: true
              });

              file.upload.then(function (response) {
                  $timeout(function () {
                      file.result = response.data;
                  });
              }, function (response) {
                  if (response.status > 0)
                      $scope.errorMsg = response.status + ': ' + response.data;
              }, function (evt) {
                  file.progress = Math.min(100, parseInt(100.0 *
                                           evt.loaded / evt.total));
              });
          }
        });

  		function uploadRecording(selectedQuestionId) {
  			var blobUrl = $("#save").attr('href');
  			console.log(blobUrl);
  			var xhr = new XMLHttpRequest();
  			xhr.open('GET', blobUrl, true);
  			xhr.responseType = 'blob';
  			xhr.onload = function(e) {
  			  if (this.status == 200) {
  				  	var blob = this.response;
  			    	console.log(blob);
  			    	// myBlob is now the blob that the object URL pointed to.
  			    	var filename = "recording_" + vm.exam.examDTO.id + "_" + selectedQuestionId + ".wav";
  			    	vm.fileUpload = new File([blob], filename);

  			    	// save answer
  			    	saveAnswerSpeaking(selectedQuestionId, filename);
  			  }
  			};
  			xhr.send();
  		}
  		
  		vm.listeningTimerRunningFlag = false;
  		vm.readingTimerRunningFlag = true;
  		function setCountdownTimer() {
    		if (vm.selectedQuestion.type == 'TIME_BREAK') {
				vm.countdown = 10 * 60; 
				$scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
				return;
			}
    		
    		if (vm.exam.examTypeDTO.type == 'MOCK_TEST_A' || vm.exam.examTypeDTO.type == 'MOCK_TEST_B' || vm.exam.examTypeDTO.type == 'MOCK_TEST_FULL') {
    			if (vm.questionGroup == 'LISTENING') {
    				if (vm.selectedQuestion.type == 'LISTENING_SUMMARIZE_SPOKEN_TEXT') {
        				vm.countdown = 10 * 60; 
        				$scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
        			} else {
        				if (vm.listeningTimerRunningFlag == false) {
        					vm.countdown = 16 * 2 * 60 + 2 * 60; 
            				$scope.$broadcast('timer-set-countdown-seconds', vm.countdown );
            				vm.listeningTimerRunningFlag == true;
        				}
        			}
    			} else if (vm.questionGroup == 'WRITING') {
    				if (vm.selectedQuestion.type == 'WRITING_SUMMARIZE_WRITTEN_TEXT') {
        				vm.countdown = 10 * 60; 
        				$scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
        			} else if (vm.selectedQuestion.type == 'WRITING_ESSAY') {
    					vm.countdown = 20 * 60; 
        				$scope.$broadcast('timer-set-countdown-seconds', vm.countdown );
        			}
    			} else if (vm.questionGroup == 'READING') {
    				if (vm.listeningTimerRunningFlag == false) {
        				vm.countdown = 40 * 60; 
        				$scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
        				vm.readingTimerRunningFlag = true;
        			}
    			}
    		}
    	}

  		function nextQuestion() {
            $('#areaTextWriting').val("");
  			vm.selectedQuestion = vm.questions.shift();
  			vm.resetProgressStatus();
  			if (vm.selectedQuestion == null || vm.selectedQuestion == undefined) {
  				vm.isFinish = true;
  				$scope.$broadcast('timer-stop');
  				// Service finish exam
  				Exam.finishExam({
  	  				id: vm.exam.examDTO.id
  	            }, onSuccessFinish, onErrorFinish);
  	            function onSuccessFinish(data, headers) {

  	            	console.log('Finish exam');
  	            	return;
  	            }
  	            function onErrorFinish(error) {
  	            	console.log('Finish exam error');
  	            	return;
  	            }
  			} else {
  				// update button lable
  				if (vm.selectedQuestion.type == 'TIME_BREAK') {
  					vm.btnTxt = 'Skip Timebreak';
  					vm.initCountQuestion();
  				} else {
  					vm.btnTxt = 'Next';
  	  				// Count question
  	  				vm.currentQuestion++;
  				}
  				
  				// Get question group
  				vm.updateQuestionInfo(vm.selectedQuestion);

  				console.log(vm.selectedQuestion);

  				vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);
  				console.log(vm.questionGroup);
  				
				setCountdownTimer();

	    		// Load player
	    		initPlayer();

	    		// Load record audio
	    		initAudio();
	    		vm.countAudio = 3;
                var interval = setInterval(function() {
                    vm.countAudio--;
                    // Display 'counter' wherever you want to display it.
                    if (vm.countAudio == 0) {

                        playAudio(vm.selectedQuestion.audioLink, 1000);
                        clearInterval(interval);
                    }
                }, 1000);
	    		vm.countdownToRecording();
  			}
  		}

  		function saveAnswer() {
  			// Skip if time break
  			if (vm.selectedQuestion.type == 'TIME_BREAK') {
  				return;
  			}

  			// Save result
  			var answer = {};
  		    answer.examId = vm.exam.examDTO.id;
  		    answer.questionId = vm.selectedQuestion.id;
  		    answer.answer = vm.answers.join(',');
  		    if (vm.questionGroup == 'WRITING' || vm.questionGroup == 'SPEAKING') {
  		    	answer.status = 'MARKING';
  		    }
  		    // answer.audioLink;
  		    // answer.description;

  			Answer.save(answer, onSaveAnswerSuccess, onSaveAnswerError);

  			function onSaveAnswerSuccess() {
  	  		}

  	  		function onSaveAnswerError() {
  	  		}
  		}

  		function saveAnswerSpeaking(selectedQuestionId, audioLink) {
  			var answer = {};
  		    answer.examId = vm.exam.examDTO.id;
  		    answer.questionId = selectedQuestionId;
  		    answer.audioLink = audioLink;
  		 	answer.status = 'MARKING';
  		    // answer.description;

  			Answer.save(answer, onSaveAnswerSuccess, onSaveAnswerError);

  			function onSaveAnswerSuccess() {
  	  		}

  	  		function onSaveAnswerError() {
  	  		}
  		}

        function spellCheck() {
            if(vm.checkClickspell == true){
                document.getElementById("areaTextWriting").setAttribute("spellcheck", "true");
                vm.checkClickspell = false;
            }else {
                document.getElementById("areaTextWriting").removeAttribute("spellcheck");
                vm.checkClickspell = true;
            }
        }

        function UpdateLengths($event) {
            vm.CharacterLength = vm.Text.length;
            vm.WordsLength=0;
            if(vm.Text.length == 1 && vm.Text[0]!='')
            {
                vm.WordsLength = 1;
            }

            for( var i=1; i< vm.Text.length; i++)
            {
                if( vm.IsAlphabet(vm.Text[i])  && !vm.IsAlphabet(vm.Text[i-1]))
                {
                    vm.WordsLength++;
                    if(vm.WordsLength == vm.WORDS_MAXIMUM + 1)// WORDS_MAXIMUM = 10
                    {
                        vm.WordsLength--;
                        vm.Text = vm.Text.substring(0, i);
                        return;
                    }
                }else if (vm.IsAlphabet(vm.Text[i]) && vm.IsAlphabet(vm.Text[i-1]) )
                {
                    if(vm.WordsLength==0)
                    {
                        vm.WordsLength=1;
                    }
                }else if(!vm.IsAlphabet(vm.Text[i]) && !vm.IsAlphabet(vm.Text[i-1]))
                {
                    continue;
                }else if(!vm.IsAlphabet(vm.Text[i]) && vm.IsAlphabet(vm.Text[i-1]))
                {
                    continue;
                }
            }
        }
        vm.IsAlphabet = function(character)
        {
            var numeric_char = character.charCodeAt(character);

            if(numeric_char>64 && numeric_char<91)// A-Z
            {
                return true;
            }
            if(numeric_char>96 && numeric_char<123)// a-z
            {
                return true;
            }
            return false;
        }
    }
})();
