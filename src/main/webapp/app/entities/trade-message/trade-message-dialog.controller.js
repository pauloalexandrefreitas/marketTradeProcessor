(function() {
    'use strict';

    angular
        .module('marketTradeProcessorApp')
        .controller('TradeMessageDialogController', TradeMessageDialogController);

    TradeMessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TradeMessage'];

    function TradeMessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TradeMessage) {
        var vm = this;

        vm.tradeMessage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tradeMessage.id !== null) {
                TradeMessage.update(vm.tradeMessage, onSaveSuccess, onSaveError);
            } else {
                TradeMessage.save(vm.tradeMessage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('marketTradeProcessorApp:tradeMessageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.timePlaced = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
