(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('FreeSampleController', FreeSampleController);

    FreeSampleController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$rootScope', '$timeout', 'ExamType'];

    function FreeSampleController ($scope, Principal, LoginService, $state, $rootScope, $timeout, ExamType) {
        var vm = this;
        vm.showExamList = showExamList;
        vm.examShowFlag = false;
        vm.exams = [];
        vm.selectedType;
        vm.filterExams = [];
        
        vm.totalExamReading = 0;
        vm.totalExamListening = 0;
        vm.totalExamQuestionReading = 0;
        vm.totalExamQuestionListening = 0;
        
        function showExamList(type) {
            $timeout(function (){
                angular.element('#listening').addClass("active");
                angular.element('#listeningMobile').addClass("activeMobile");
            });
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
