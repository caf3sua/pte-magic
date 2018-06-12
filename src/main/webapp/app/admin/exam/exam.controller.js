(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('ExamController', ExamController);

    ExamController.$inject = ['$state', 'Exam', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ExamController($state, Exam, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.selectExam = selectExam;
        vm.deleteAll = deleteAll;
        
        vm.exams = [];
    	vm.examsInit = [];
    	
    	vm.selectedExamIds = [];
    	
        loadAll();
        
        function deleteAll() {
        	if (confirm("Do you want to delete exams!")) {
        		var obj = {
        			ids : vm.selectedExamIds
        		};
        		Exam.removeAll(obj, onSuccess, onError);
        		function onSuccess(data, headers) {
        			loadAll();
                }
                function onError(error) {
                    AlertService.error(error.data.message);
                }
        	}
        }
        
        function selectExam(id) {
        	if (vm.selectedExamIds.indexOf(id) == -1) {
        		vm.selectedExamIds.push(id);
        	} else {
        		vm.selectedExamIds.splice( vm.selectedExamIds.indexOf(id), 1 );
        	}
        	
        	console.log(vm.selectedExamIds);
        }

        function loadAll () {
        	Exam.getAll({
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.exams = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
