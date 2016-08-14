(function() {
	'use strict';

	angular.module('marketTradeProcessorApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('trade-message-realtime', {
			parent : 'entity',
			url : '/trade-message-realtime',
			data : {
				authorities : [ 'ROLE_USER' ],
				pageTitle : 'marketTradeProcessorApp.tradeMessage.home.title'
			},
			views : {
				'content@' : {
					templateUrl : 'app/entities/trade-message-realtime/trade-message-realtime.html',
					controller : 'TradeMessageRealTimeController',
					controllerAs : 'vm'
				}
			},
			resolve : {
				translatePartialLoader : [ '$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
					$translatePartialLoader.addPart('tradeMessage');
					$translatePartialLoader.addPart('global');
					return $translate.refresh();
				} ]
			},
			onEnter : [ 'TradeMessageRealTimeService', function(TradeMessageRealTimeService) {
				TradeMessageRealTimeService.subscribe();
			} ],
			onExit : [ 'TradeMessageRealTimeService', function(TradeMessageRealTimeService) {
				TradeMessageRealTimeService.unsubscribe();
			} ]
		});
	}

})();
