(function() {
    'use strict';

    angular
        .module('marketTradeProcessorApp')
        .controller('TradeMessageDeleteController',TradeMessageDeleteController);

    TradeMessageDeleteController.$inject = ['$uibModalInstance', 'entity', 'TradeMessage'];

    function TradeMessageDeleteController($uibModalInstance, entity, TradeMessage) {
        var vm = this;

        vm.tradeMessage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TradeMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
