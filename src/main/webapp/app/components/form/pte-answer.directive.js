(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .directive('pteAnswerCheckbox', pteAnswerCheckbox)
    	.directive('disableRightClick', disableRightClick);
    
    function pteAnswerCheckbox () {
        var directive = {
            restrict: 'E',
            scope: {
            	idAnswer : '@',
            	expectAnswer : '=',
            	isShowAnswer : '='
            },
            templateUrl : 'app/partial/partial.pte-answer-checkbox.html',
            
            link: function(scope, elem, attr) {
            	scope.$watch("isShowAnswer", function() {
                    console.log("isShowAnswer Changed");
                    calculateAnswer();
                 }, true);

            	function calculateAnswer() {
            		scope.checkAnswer = false;
            		if (scope.isShowAnswer) {
            			let index = scope.expectAnswer.indexOf(scope.idAnswer);
            			if (index != -1) {
            				scope.checkAnswer = true;
            			}
            		}
            	}
            }
        };

        return directive;
    }

    function disableRightClick () {
        var directive = {
            restrict: 'A',
            link: function(scope, elem, attr) {
            	elem.bind('contextmenu', function(e){
                    e.preventDefault();
                  })
            }
        };

        return directive;
    }
})();
