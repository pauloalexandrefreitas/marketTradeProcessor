(function() {
    'use strict';

    angular
        .module('marketTradeProcessorApp')
        .factory('TradeMessageSearch', TradeMessageSearch);

    TradeMessageSearch.$inject = ['$resource'];

    function TradeMessageSearch($resource) {
        var resourceUrl =  'api/_search/trade-messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
