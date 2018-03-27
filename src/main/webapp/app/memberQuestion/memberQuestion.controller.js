(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('MemberQuestionController', MemberQuestionController);

    MemberQuestionController.$inject = ['$scope', '$window', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function MemberQuestionController ($scope, $window, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
        var vm = this;
        vm.showExamList = showExamList;
        vm.startTest = startTest;
        vm.examShowFlag = false;
        vm.exams = [];
        vm.selectedType;
        vm.filterExams = [];

        vm.totalExamReading = 0;
        vm.totalExamListening = 0;
        vm.totalExamQuestionReading = 0;
        vm.totalExamQuestionListening = 0;

        function showExamList(type) {
            if(type == 'LISTENING'){
                $timeout(function (){
                    angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                    angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                    angular.element('#listening').addClass("active");
                    angular.element('#listeningMobile').addClass("activeMobile");
                });
            }else if(type == 'READING'){
                angular.element(document.getElementsByClassName("pte-freeSample-block")).removeClass("active");
                angular.element(document.getElementsByClassName("pte-free-sample-mobile-icon")).removeClass("activeMobile");
                angular.element('#reading').addClass("active");
                angular.element('#readingMobile').addClass("activeMobile");
            }
            vm.examShowFlag = true;
            vm.selectedType = type;
            console.log(vm.selectedType);

            // LISTENING
            vm.filterExams = [];
            angular.forEach(vm.exams, function(value, key){
            	if (type == 'LISTENING' && value.numberQuestionListening > 0) {
            		value.numberQuestion = value.numberQuestionListening;
            		vm.filterExams.push(value);
                } else if (type == 'READING' && value.numberQuestionReading > 0) {
                	value.numberQuestion = value.numberQuestionReading;
                	vm.filterExams.push(value);
                }
            });
        }

        function startTest(examId) {
        	var url = '/#/test?type=' + examId;
        	$window.open(url,"_blank", "toolbar=no,scrollbars=no, resizable=no, width=1200, height=700");
        }

//        $scope.$watch('vm.exams', function(newVal, oldVal){
//        });

        // Init controller
  		(function initController() {
  			ExamType.getAllByType({type: 'FREE_EXAM'}, onSuccess, onError);

  			function onSuccess(data, headers) {
  				console.log(data);
  				vm.exams = data;

  				angular.forEach(data, function(value, key){
  	            	if (value.numberQuestionListening > 0) {
  	            		vm.totalExamListening++;
  	            		vm.totalExamQuestionListening = vm.totalExamQuestionListening + value.numberQuestionListening;
  	                } else if (value.numberQuestionReading > 0) {
  	                	vm.totalExamReading++;
  	            		vm.totalExamQuestionReading = vm.totalExamQuestionReading + value.numberQuestionReading;
  	                }
  	            });
  			}
  			function onError(error) {

  			}
  		})();
    }
})();
