(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FullTestController', FullTestController);

    FullTestController.$inject = ['$controller', '$scope', '$window', '$stateParams', 'Principal', 'LoginService', '$state'
        , '$rootScope', '$timeout', 'ExamType', 'Exam', 'Answer', 'Upload', '$sce', 'entity', '$interval', 'PTE_SETTINGS'];

    function FullTestController ($controller, $scope, $window, $stateParams, Principal, LoginService, $state
        , $rootScope, $timeout, ExamType, Exam, Answer, Upload, $sce, entity, $interval, PTE_SETTINGS) {

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
            // Load player
//            initPlayer();

            // Load record audio
            initAudio();

            // Init mocktest
            // initMockTest();

            vm.initCountQuestion();

            // Next question
            nextQuestion();
        })();

        function answer() {
        	console.log('Answer: ' + vm.selectedQuestion.id);

            initAnswer();

            if (vm.selectedQuestion.type != 'TIME_BREAK') {
                // Upload if questionGroup == SPEAKING
                if (vm.questionGroup == 'SPEAKING') {
                	var questionId = vm.selectedQuestion.id;
                	stopRecording();
                	$timeout(function(){
                		vm.uploadRecording(questionId);
                	}, 2000 );
                } else {
                    console.log(vm.selectedQuestion);
                    // Get answer
                    vm.getUserAnswer();
                    console.log(vm.answers);

                    // Save answer
                    vm.saveAnswer();
                }
            }

            // Next question
            nextQuestion();
        }

        function setCountdownTimer() {
        	console.log('setCountdownTimer');
            if (vm.selectedQuestion.type == 'TIME_BREAK') {
                vm.countdown = PTE_SETTINGS.COUNT_DOWN_TIME_BREAK;
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
                            vm.listeningTimerRunningFlag = true;
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
                    if (vm.readingTimerRunningFlag == false) {
                        vm.countdown = 40 * 60;
                        $scope.$broadcast('timer-set-countdown-seconds', vm.countdown);
                        vm.readingTimerRunningFlag = true;
                    }
                }
            }
        }

        function nextQuestion() {
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
    }
})();
