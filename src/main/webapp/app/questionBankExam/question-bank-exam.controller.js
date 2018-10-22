(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('QuestionBankExamController', QuestionBankExamController);

    QuestionBankExamController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
        , '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce', '$interval', 'PTE_SETTINGS', 'Question'
        , 'pagingParams', 'ParseLinks', 'paginationConstants'];

    function QuestionBankExamController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
        , $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce, $interval, PTE_SETTINGS, Question
        , pagingParams, ParseLinks, paginationConstants) {

        var vm = this;

        // Function
        vm.questionType = $stateParams.type;
        vm.answer = answer;
        vm.trustAsHtml = $sce.trustAsHtml;
        vm.itemsPerPage = 1;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;

        // Variable, flag
        vm.examTypeId;
//        vm.exam = entity;
//        vm.questions = entity.questions;
        vm.selectedQuestion;
        vm.answers = [];
        vm.isFinish = false;
        vm.listItemAnswer = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'];
        vm.audio;
        vm.toggleRecording = toggleRecording;
        vm.startNewPart = startNewPart;

        vm.txtInfoCountdown = "Begining in ";
        vm.countdownRecording = 5;
        vm.isRecording = false;
        vm.checkAudioSeconds = true;
        vm.checkStatusPlay = false;
        vm.countdownTimebreak = 600;

        vm.callBackAnswer = callBackAnswer;
        
        vm.btnTxt = 'Next';
        
        function callBackAnswer() {
        	console.log('WARNING: call back finish');
        	vm.answer();
        }

        function startNewPart() {
            initAnswer();

            nextQuestion();
        }

        function stopRecording() {
            // stop recording
            audioRecorder.stop();
            audioRecorder.getBuffers( gotBuffers );
            clearInterval(vm.intervalProgress);
        }

        function initAnswer() {
            // Stop audio
            var audio = $("#player");
            if (audio[0] != undefined && audio[0].duration > 0 && !audio[0].paused) {
                audio[0].pause();
            }


            vm.txtInfoCountdown = "Begining in ";
            vm.isRecording = false;
        }

        angular.element(document).ready(function () {
        });

        // Init controller
        (function initController() {
            // instantiate base controller
            $controller('PteMagicBaseController', { vm: vm, $scope: $scope });
            $timeout(function (){
                angular.element(document.getElementById("content")).removeClass("background-color-222d32");
            });

            Question.queryByType({
                  page: pagingParams.page - 1,
                  size: vm.itemsPerPage,
                  sort: sort(),
                  type: vm.questionType
              }, onSuccess, onError);
              function onSuccess(data, headers) {
                  vm.links = ParseLinks.parse(headers('link'));
                  vm.totalItems = headers('X-Total-Count');
                  vm.queryCount = vm.totalItems;
                  vm.questions = data;
                  vm.selectedQuestion = data[0];
                  vm.page = pagingParams.page;
                  
                  // Load record audio
                  initAudio();

//                  vm.initCountQuestion();

                  // Next question
                  nextQuestion();
              }
              function onError(error) {
                  console.log(error.data.message);
              }
        })();

        function answer() {
        	console.log('Answer: ' + vm.selectedQuestion.id);

            initAnswer();

            if (vm.selectedQuestion.type != 'TIME_BREAK') {
                // Upload if questionGroup == SPEAKING
                if (vm.questionGroup == 'SPEAKING') {
//                	var questionId = vm.selectedQuestion.id;
                	stopRecording();
//                	$timeout(function(){
//                		vm.uploadRecording(questionId);
//                	}, 2000 );
                } else {
//                    console.log(vm.selectedQuestion);
//                    // Get answer
//                    vm.getUserAnswer();
//                    console.log(vm.answers);
//
//                    // Save answer
//                    vm.saveAnswer();
                }
            }

            // Next question
//            nextQuestion();
            vm.timeup();
        }

        function setCountdownTimer() {
        	console.log('setCountdownTimer');
            if (vm.selectedQuestion.type == 'TIME_BREAK') {
                vm.countdown = PTE_SETTINGS.COUNT_DOWN_TIME_BREAK;
                $scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
                return;
            }
        }

        function nextQuestion() {
        	vm.isShowAnswer = false;
            vm.Text = "";
            $('#areaTextWriting').val("");
            $('#areaTextWriting').html('');
            vm.WordsLength = 0;
            vm.selectedQuestion = vm.questions.shift();
            vm.audioProgressing = 0;
            vm.resetProgressStatus();
            if(vm.intervalAudio) {
        		$interval.cancel(vm.intervalAudio);
            }
        	if (vm.intervalProgress) {
        		$interval.cancel(vm.intervalProgress);
        	}
        	if (vm.intervalCounter) {
        		$interval.cancel(vm.intervalCounter);
        	}
        	if (vm.intervalToRecording) {
        		$interval.cancel(vm.intervalToRecording);
        	}
        	
        	console.log(vm.intervalAudio);
        	console.log(vm.intervalProgress);
        	console.log(vm.intervalCounter);
        	console.log(vm.intervalToRecording);
            
            // enable button
            vm.enableNextButton();

            // Timeout
            $timeout(function(){
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
                    
                    console.log('======== Next question, questionId :' + vm.selectedQuestion.id + ', type: ' + vm.selectedQuestion.type + ' ============');
                    console.log('======== currentSKill :' + vm.currentSKill + ',' + vm.currentQuestion + '/' + vm.totalQuestion + ' ============');
                    
                    // Get question group
                    vm.updateQuestionInfo(vm.selectedQuestion);
                    vm.questionGroup = getQuestionGroup(vm.selectedQuestion.type);

                    setCountdownTimer();

                    // Load record audio
                    initAudio();
                    
                    vm.checkAudioSeconds = true;
    	            vm.checkStatusPlay = false;
                    vm.txtStatusAudio = 'Playing';
                    
                    // Check
                    if (vm.selectedQuestion.audioLink != null && vm.selectedQuestion.audioLink != undefined) {
                    	if(vm.intervalAudio) {
                    		$interval.cancel(vm.intervalAudio);
                        }
                        vm.countAudio = 3;
                        vm.intervalAudio = $interval(function() {
                        	console.log("vm.countAudio: " + vm.countAudio);
                        	if (vm.countAudio > 0) {
                        		vm.countAudio--;
                        	} else if (vm.countAudio == 0) {
                        		vm.playAudio(vm.selectedQuestion.audioLink, 1000);
                        		$interval.cancel(vm.intervalAudio);
                        	}
                        }, 1000);
                    }
                    
                    vm.countdownToRecording();
                }
        	});
        }
        
        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }
        
        function searchAllTransition () {
              Question.queryByType({
                  page: vm.page - 1,
                  size: vm.itemsPerPage,
                  sort: sort(),
                  type: vm.questionType
              }, onSuccess, onError);
              function onSuccess(data, headers) {
                  vm.links = ParseLinks.parse(headers('link'));
                  vm.totalItems = headers('X-Total-Count');
                  vm.queryCount = vm.totalItems;
                  vm.questions = data;
                  vm.selectedQuestion = data[0];
                  
                  // Load record audio
                  initAudio();

                  // Next question
                  nextQuestion();
              }
              function onError(error) {
                  console.log(error.data.message);
              }
        }
        
        function transition() {
        	console.log('transition query, skill:' + vm.selectedSkill);
        	searchAllTransition();
        }
    }
})();

