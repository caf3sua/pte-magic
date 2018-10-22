(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('QuestionBankMainController', QuestionBankMainController);

    QuestionBankMainController.$inject = ['$scope', '$window', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function QuestionBankMainController ($scope, $window, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
    	var vm = this;
        vm.showExamList = showExamList;
        vm.startTest = startTest;
        vm.examShowFlag = false;
        vm.selectedType;
        vm.selectedExams = [];
        vm.listeningExams = [];
        vm.readingExams = [];
        vm.speakingExams = [];
        vm.writingExams = [];

        $timeout(function (){
            angular.element(document.getElementById("content")).removeClass("background-color-222d32");
        });
        function showExamList(type) {
            if(type == 'LISTENING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#listening').addClass("active");
                    angular.element('#listeningMobile').addClass("activeMobile");
                });

                vm.selectedExams = vm.listeningExams;
            }else if(type == 'READING'){
                angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                angular.element('#reading').addClass("active");
                angular.element('#readingMobile').addClass("activeMobile");

                vm.selectedExams = vm.readingExams;
            }if(type == 'WRITING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#writing').addClass("active");
                    angular.element('#writingMobile').addClass("activeMobile");
                });
                vm.selectedExams = vm.writingExams;
            }
            if(type == 'SPEAKING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#speaking').addClass("active");
                    angular.element('#speakingMobile').addClass("activeMobile");
                });
                vm.selectedExams = vm.speakingExams;
            }
            vm.examShowFlag = true;
            vm.selectedType = type;
            console.log(vm.selectedType);
        }

        function startTest(exam) {
        	var url = '/#/question-bank?type=' + exam.type;
        	$window.open(url,"_blank", "toolbar=no,scrollbars=no, resizable=no, width=1200, height=700");
        }

        // Init controller
  		(function initController() {
  			vm.speakingExams = [
  			                    {id: 0, type: "SPEAKING_READ_ALOUD", name : "Read aloud [RA]"}
  			                    , {id: 1, type: "SPEAKING_REPEAT_SENTENCE", name : "Repeat scentence [RS]"}
	  			                , {id: 2, type: "SPEAKING_DESCRIBE_IMAGE", name : "Describe image [DI]"}
	  			                , {id: 3, type: "SPEAKING_RETELL_LECTURE", name : "Re-tell lecture [RL]"}
	  			                , {id: 4, type: "SPEAKING_ANSWER_SHORT_QUESTION", name : "Answer short question [ASQ]"}
            ];
  			vm.writingExams = [
								{id: 5, type: "WRITING_SUMMARIZE_WRITTEN_TEXT", name : "Summarize witten text [SWT]"}
								  , {id: 6, type: "WRITING_ESSAY", name : "Write essay [WE]"}
  			];
  			vm.readingExams = [
								{id: 7, type: "READING_FIB_R_W", name : "R&W: Fill in the blanks [RWFIB]"}
								  , {id: 8, type: "READING_FIB_R", name : "R: Fill in the blanks [RFIB]"}
								  , {id: 9, type: "READING_RE_ORDER_PARAGRAPH", name : "Re-order paragraphs"}
								  , {id: 10, type: "READING_MCQ_R_SINGLE_ANSWER", name : "MC, choose single answer"}
								  , {id: 11, type: "READING_MCQ_R_MULTIPLE_ANSWER", name : "MC, choose multiple answers"}
  			];
  			vm.listeningExams = [
  	                             {id: 12, type: "LISTENING_SUMMARIZE_SPOKEN_TEXT", name : "Summarize spoken text"}
								  , {id: 13, type: "LISTENING_FIB_L", name : "Fill in the blanks [LFIB]"}
								  , {id: 14, type: "LISTENING_MCQ_L_SINGLE_ANSWER", name : "MC, choose single answer"}
								  , {id: 15, type: "LISTENING_MCQ_L_MULTIPLE_ANSWER", name : "MC, choose multiple answers"}
								  , {id: 16, type: "LISTENING_HIGHLIGHT_CORRECT_SUMMARY", name : "Highlight correct summary"}
								  , {id: 17, type: "LISTENING_SELECT_MISSING_WORD", name : "Select missing words"}
								  , {id: 18, type: "LISTENING_HIGHLIGHT_INCORRECT_WORD", name : "Highlight incorect words"}
								  , {id: 19, type: "LISTENING_DICTATION", name : "Write from dictation [WFD]"}
                             ];
  		})();
    }
})();
