(function() {
    'use strict';
    angular
        .module('marketTradeProcessorApp')
        .factory('TradeMessageStats', TradeMessageStats);

    TradeMessageStats.$inject = ['$resource'];

    function TradeMessageStats ($resource) {
        var resourceUrl =  'api/trade-messages-stats';

        return $resource(resourceUrl, {}, {
            'currencyFrom': { url: 'api/trade-messages-stats/currencyFrom', method: 'GET', isArray: true},
            'currencyTo': { url: 'api/trade-messages-stats/currencyTo', method: 'GET', isArray: true}
        });
    }
})();
