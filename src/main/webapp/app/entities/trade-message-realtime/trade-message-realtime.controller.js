(function() {
	'use strict';

	angular.module('marketTradeProcessorApp').controller('TradeMessageRealTimeController', TradeMessageRealTimeController);

	TradeMessageRealTimeController.$inject = [ '$scope', '$state', 'TradeMessage', 'TradeMessageRealTimeService', ];

	function TradeMessageRealTimeController($scope, $state, TradeMessage, TradeMessageRealTimeService) {
		var vm = this;

		vm.tradeMessages = [];

		vm.currencyFromLabels = [];
		vm.currencyFromSeries = [ 'Sell', 'Buy' ];
		vm.currencyFromData = {};
		vm.currencyToLabels = [];
		vm.currencyToSeries = [ 'Sell', 'Buy' ];
		vm.currencyToData = {};

		vm.chartOptions = {
			maintainAspectRatio : false,
			scales : {
				yAxes : [ {
					display : true,
					ticks : {
						suggestedMin : 0
					}
				} ]
			}
		};

		TradeMessageRealTimeService.receive().then(null, null, function(tradeMessage) {
			showTradeMessage(tradeMessage);
		});

		function showTradeMessage(tradeMessage) {
			vm.tradeMessages.push(tradeMessage);

			if (vm.currencyFromLabels.indexOf(tradeMessage.currencyFrom) === -1) {
				vm.currencyFromLabels.push(tradeMessage.currencyFrom);
				vm.currencyFromData[tradeMessage.currencyFrom] = [ [], [] ];
			}
			if (vm.currencyToLabels.indexOf(tradeMessage.currencyTo) === -1) {
				vm.currencyToLabels.push(tradeMessage.currencyTo);
				vm.currencyToData[tradeMessage.currencyTo] = [ [], [] ];
			}

			vm.currencyFromData[tradeMessage.currencyFrom][0].push(tradeMessage.amountSell);
			vm.currencyFromData[tradeMessage.currencyFrom][1].push(tradeMessage.amountBuy);
			vm.currencyToData[tradeMessage.currencyTo][0].push(tradeMessage.amountSell);
			vm.currencyToData[tradeMessage.currencyTo][1].push(tradeMessage.amountBuy);

		}
	}
})();
