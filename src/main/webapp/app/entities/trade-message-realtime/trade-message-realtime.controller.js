(function() {
	'use strict';

	angular.module('marketTradeProcessorApp').controller('TradeMessageRealTimeController', TradeMessageRealTimeController);

	TradeMessageRealTimeController.$inject = [ '$scope', '$state', 'TradeMessage', 'TradeMessageRealTimeService', ];

	function TradeMessageRealTimeController($scope, $state, TradeMessage, TradeMessageRealTimeService) {
		var vm = this;

		vm.tradeMessages = [];

		TradeMessageRealTimeService.receive().then(null, null, function(tradeMessage) {
			showTradeMessage(tradeMessage);
		});

		function showTradeMessage(tradeMessage) {
			vm.tradeMessages.push(tradeMessage);
		}
	}
})();
