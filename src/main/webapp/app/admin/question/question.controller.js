(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .controller('QuestionController', QuestionController);

    QuestionController.$inject = ['$state', 'Question', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$rootScope', '$stateParams'];

    function QuestionController($state, Question, ParseLinks, AlertService, paginationConstants, pagingParams, $rootScope, $stateParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        if($stateParams.selectedSkill == null){
            vm.selectedSkill = 'SPEAKING';
        }else{
            vm.selectedSkill = $stateParams.selectedSkill;
        }

        loadAll();

        function loadAll () {

        	Question.queryBySkill({
//            Question.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                skill: vm.selectedSkill
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.questions = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function searchAllTransition () {
        	Question.queryBySkill({
                  page: vm.page - 1,
                  size: vm.itemsPerPage,
                  sort: sort(),
                  skill: vm.selectedSkill
              }, onSuccess, onError);
              function sort() {
                  var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                  if (vm.predicate !== 'id') {
                      result.push('id');
                  }
                  return result;
              }
              function onSuccess(data, headers) {
                  vm.questions = data;
              }
              function onError(error) {
                  AlertService.error(error.data.message);
              }
        }
        
        function transition() {
        	console.log('transition query, skill:' + vm.selectedSkill);
        	searchAllTransition();
//            $state.transitionTo($state.$current, {
//                page: vm.page,
//                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
//                skill: vm.selectedSkill,
//                search: vm.currentSearch
//            });
        }
    }
})();
