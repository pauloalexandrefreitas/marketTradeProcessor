(function() {
	'use strict';
	/* globals SockJS, Stomp */

	angular.module('marketTradeProcessorApp').factory('TradeMessageRealTimeService', TradeMessageRealTimeService);

	TradeMessageRealTimeService.$inject = [ '$rootScope', '$window', '$cookies', '$http', '$q' ];

	function TradeMessageRealTimeService($rootScope, $window, $cookies, $http, $q) {
		var stompClient = null;
		var subscriber = null;
		var listener = $q.defer();
		var connected = $q.defer();
		var alreadyConnectedOnce = false;

		var service = {
			connect : connect,
			disconnect : disconnect,
			receive : receive,
			subscribe : subscribe,
			unsubscribe : unsubscribe
		};

		return service;

		function connect() {
			// building absolute path so that websocket doesnt fail when deploying with a context path
			var loc = $window.location;
			var url = '//' + loc.host + loc.pathname + 'websocket/tracker';
			var socket = new SockJS(url);
			stompClient = Stomp.over(socket);
			var headers = {};
			headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
			stompClient.connect(headers, function() {
				connected.resolve('success');
			});
		}

		function disconnect() {
			if (stompClient !== null) {
				stompClient.disconnect();
				stompClient = null;
			}
		}

		function receive() {
			return listener.promise;
		}

		function subscribe() {
			connected.promise.then(function() {
				subscriber = stompClient.subscribe('/topic/trading', function(data) {
					listener.notify(angular.fromJson(data.body));
				});
			}, null, null);
		}

		function unsubscribe() {
			if (subscriber !== null) {
				subscriber.unsubscribe();
			}
			listener = $q.defer();
		}
	}
})();
