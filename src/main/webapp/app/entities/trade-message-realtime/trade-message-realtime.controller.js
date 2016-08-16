(function() {
	'use strict';

	angular.module('marketTradeProcessorApp').controller('TradeMessageRealTimeController', TradeMessageRealTimeController);

	TradeMessageRealTimeController.$inject = [ '$scope', '$state', 'TradeMessage', 'TradeMessageRealTimeService', ];

	function TradeMessageRealTimeController($scope, $state, TradeMessage, TradeMessageRealTimeService) {
		var vm = this;

		vm.tradeMessages = [];
		vm.marketData = {
			'labels' : [],
			'data' : [ [], [] ],
			'series' : [ 'Total Sell', 'Total Buy' ]
		};
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
			var market = tradeMessage.currencyFrom + '->' + tradeMessage.currencyTo;
			var index = vm.marketData.labels.indexOf(market);
			if (index === -1) {
				vm.marketData.labels.push(market);
				vm.marketData.data[0].push(tradeMessage.amountSell);
				vm.marketData.data[1].push(tradeMessage.amountBuy);
			} else {
				vm.marketData.data[0][index] += tradeMessage.amountSell;
				vm.marketData.data[1][index] += tradeMessage.amountBuy;
			}
		}
	}
})();
