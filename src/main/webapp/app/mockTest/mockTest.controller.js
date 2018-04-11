(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('MockTestController', MockTestController);

    MockTestController.$inject = ['$scope', '$window', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function MockTestController ($scope, $window, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
    	var vm = this;
        vm.showExamList = showExamList;
        vm.startTest = startTest;
        vm.examShowFlag = false;
        vm.partAExams = [];
        vm.partBExams = [];
        vm.fullExams = [];
        vm.writingExams = [];
        vm.selectedType;
        vm.selectedExams = [];

        vm.totalExamQuestionReading = 0;
        vm.totalExamQuestionListening = 0;
        vm.totalExamQuestionSpeaking = 0;
        vm.totalExamQuestionWriting = 0;
        
        vm.totalQuestion; 
        vm.duration; // 00:25:22

        function showExamList(type) {

            if(type == 'MOCK_TEST_A'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-mockTest-block")).removeClass("active");
                    angular.element('#pastA').addClass("active");
                });

                vm.selectedExams = vm.partAExams;
                vm.totalQuestion = 40; 
                vm.duration = '00:25:22';
            }else if(type == 'MOCK_TEST_B'){
            	$timeout(function (){
                    angular.element(document.getElementsByClassName("pte-mockTest-block")).removeClass("active");
                    angular.element('#pastB').addClass("active");
                  });

                vm.selectedExams = vm.partBExams;
                vm.totalQuestion = 60; 
                vm.duration = '00:60:00';
            }if(type == 'MOCK_TEST_FULL'){
            	$timeout(function (){
                    angular.element(document.getElementsByClassName("pte-mockTest-block")).removeClass("active");
                    angular.element('#fullTest').addClass("active");
                  });
                vm.selectedExams = vm.fullExams;
                vm.totalQuestion = 80; 
                vm.duration = '01:30:00';
            }
            vm.examShowFlag = true;
            vm.selectedType = type;
        }

        function startTest(examId) {
        	var url = '/#/fulltest?type=' + examId;
        	$window.open(url,"_blank", "toolbar=no,scrollbars=no, resizable=no, width=1200, height=700");
        }

        // Init controller
  		(function initController() {
  	        vm.totalExamQuestionReading = 0;
  	        vm.totalExamQuestionListening = 0;
  	        vm.totalExamQuestionSpeaking = 0;
  	        vm.totalExamQuestionWriting = 0;

  	        ExamType.getAllByType({type: 'MOCK_TEST_A'},
  					function(data, headers) {
  	        			if (data != null) {
  	        				vm.partAExams = data;

  			  				angular.forEach(data, function(value, key){
  		  	            		vm.totalExamQuestionListening += value.totalQuestion;
  			  	            });
  	        			}
  					},
  					function(error) {
  					});

  			ExamType.getAllByType({type: 'MOCK_TEST_B'},
  					function(data, headers) {
  						if (data != null) {
			  				vm.partBExams = data;

			  				angular.forEach(data, function(value, key){
		  	            		vm.totalExamQuestionReading += value.totalQuestion;
			  	            });
  						}
  					},
  					function(error) {
  					});

  			ExamType.getAllByType({type: 'MOCK_TEST_FULL'},
  					function(data, headers) {
  						if (data != null) {
			  				vm.fullExams = data;

			  				angular.forEach(data, function(value, key){
		  	            		vm.totalExamQuestionSpeaking += value.totalQuestion;
			  	            });
  						}
  					},
  					function(error) {
  					});

  		})();
    }
})();

