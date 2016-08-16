(function() {
	'use strict';
	angular.module('marketTradeProcessorApp').factory('TradeMessageStats', TradeMessageStats);

	TradeMessageStats.$inject = [ '$resource' ];

	function TradeMessageStats($resource) {
		var resourceUrl = 'api/trade-messages-stats';

		return $resource(resourceUrl, {}, {
			'currencyFrom' : {
				url : 'api/trade-messages-stats/currencyFrom',
				method : 'GET',
				isArray : true
			},
			'currencyTo' : {
				url : 'api/trade-messages-stats/currencyTo',
				method : 'GET',
				isArray : true
			},
			'byMarket' : {
				url : 'api/trade-messages-stats/byMarket',
				method : 'GET',
				isArray : true
			},
			'specificMarket' : {
				url : 'api/trade-messages-stats/specificMarket',
				method : 'GET',
				isArray : true
			},
			'byCountry' : {
				url : 'api/trade-messages-stats/byCountry',
				method : 'GET',
				isArray : true
			},
		});
	}
})();
