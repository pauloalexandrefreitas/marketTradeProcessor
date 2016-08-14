(function () {
    'use strict';

    angular
        .module('marketTradeProcessorApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
