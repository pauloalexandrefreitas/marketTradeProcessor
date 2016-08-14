(function() {
    'use strict';
    angular
        .module('marketTradeProcessorApp')
        .factory('TradeMessage', TradeMessage);

    TradeMessage.$inject = ['$resource', 'DateUtils'];

    function TradeMessage ($resource, DateUtils) {
        var resourceUrl =  'api/trade-messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timePlaced = DateUtils.convertDateTimeFromServer(data.timePlaced);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
