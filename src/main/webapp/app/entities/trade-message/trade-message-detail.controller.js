(function() {
    'use strict';

    angular
        .module('marketTradeProcessorApp')
        .controller('TradeMessageDetailController', TradeMessageDetailController);

    TradeMessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TradeMessage'];

    function TradeMessageDetailController($scope, $rootScope, $stateParams, previousState, entity, TradeMessage) {
        var vm = this;

        vm.tradeMessage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('marketTradeProcessorApp:tradeMessageUpdate', function(event, result) {
            vm.tradeMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
