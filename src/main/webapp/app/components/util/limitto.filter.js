/**
 * author: Cuong V. Le
 */
(function() {
    'use strict';

    angular
        .module('pteMagicApp')
        .filter('lmto', lmto);

    function lmto() {
        return function(text){
            var args = Array.prototype.slice.call(arguments);
            if (args.length == 1) {
                return text;
            }

            text = String(text).trim();
            var rtText = "";

            if (text.length > parseInt(args[1])) {
                rtText = text.substr(0, parseInt(args[1])) + "...";
            } else {
                return text;
            }

            return rtText;
        };
    }
})();
