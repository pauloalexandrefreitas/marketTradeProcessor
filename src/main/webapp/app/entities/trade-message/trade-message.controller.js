(function() {
	'use strict';

	angular.module('marketTradeProcessorApp').controller('TradeMessageController', TradeMessageController);

	TradeMessageController.$inject = [ '$scope', '$state', '$filter', 'TradeMessage', 'TradeMessageStats', 'TradeMessageSearch', 'ParseLinks', 'AlertService',
			'pagingParams', 'paginationConstants' ];

	function TradeMessageController($scope, $state, $filter, TradeMessage, TradeMessageStats, TradeMessageSearch, ParseLinks, AlertService, pagingParams,
			paginationConstants) {
		var vm = this;

		vm.loadPage = loadPage;
		vm.predicate = pagingParams.predicate;
		vm.reverse = pagingParams.ascending;
		vm.transition = transition;
		vm.itemsPerPage = paginationConstants.itemsPerPage;
		vm.clear = clear;
		vm.search = search;
		vm.loadAll = loadAll;
		vm.searchQuery = pagingParams.search;
		vm.currentSearch = pagingParams.search;
		vm.loadCharts = loadCharts;
		vm.updateCurrentMarketData = updateCurrentMarketData;
		vm.dateRange = null;
		vm.marketData = [];

		loadAll();

		function loadAll() {
			if (pagingParams.search) {
				TradeMessageSearch.query({
					query : pagingParams.search,
					page : pagingParams.page - 1,
					size : vm.itemsPerPage,
					sort : sort()
				}, onSuccess, onError);
			} else {
				TradeMessage.query({
					page : pagingParams.page - 1,
					size : vm.itemsPerPage,
					sort : sort()
				}, onSuccess, onError);
			}
			function sort() {
				var result = [ vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc') ];
				if (vm.predicate !== 'id') {
					result.push('id');
				}
				return result;
			}
			function onSuccess(data, headers) {
				vm.links = ParseLinks.parse(headers('link'));
				vm.totalItems = headers('X-Total-Count');
				vm.queryCount = vm.totalItems;
				vm.tradeMessages = data;
				vm.page = pagingParams.page;
			}
			function onError(error) {
				AlertService.error(error.data.message);
			}
		}

		function loadPage(page) {
			vm.page = page;
			vm.transition();
		}

		function transition() {
			$state.transitionTo($state.$current, {
				page : vm.page,
				sort : vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
				search : vm.currentSearch
			});
		}

		function search(searchQuery) {
			if (!searchQuery) {
				return vm.clear();
			}
			vm.links = null;
			vm.page = 1;
			vm.predicate = '_score';
			vm.reverse = false;
			vm.currentSearch = searchQuery;
			vm.transition();
		}

		function clear() {
			vm.links = null;
			vm.page = 1;
			vm.predicate = 'id';
			vm.reverse = true;
			vm.currentSearch = null;
			vm.transition();
		}

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

		$scope.$watch(function(scope) {
			return vm.dateRange;
		}, function(newVal, oldVal) {
			vm.loadCharts();
		});

		function loadCharts() {
			loadMarketAndRateCharts();
			loadCountryChart();
		}

		function loadMarketAndRateCharts() {
			var params = {};
			if (vm.dateRange !== null) {
				var arr = vm.dateRange.split(',');
				params.fromDate = moment().subtract(arr[0], arr[1]).toISOString();
				params.toDate = moment().toISOString();
			}
			vm.marketData = {
				'labels' : [],
				'data' : [ [], [] ],
				'series' : [ 'Total Sell', 'Total Buy' ]
			};

			vm.marketRateData = [];

			TradeMessageStats.byMarket(params, function(data) {
				angular.forEach(data, function(v, k) {
					vm.marketData.labels.push(v[0]);
					vm.marketData.data[0].push(v[1]);
					vm.marketData.data[1].push(v[2]);
					vm.marketRateData.push(v[3]);
				});
			});
		}

		function updateCurrentMarketData() {
			if (angular.isDefined(vm.currentMarket)) {
				var params = {
					market : vm.currentMarket
				};
				if (vm.dateRange !== null) {
					var arr = vm.dateRange.split(',');
					params.fromDate = moment().subtract(arr[0], arr[1]).toISOString();
					params.toDate = moment().toISOString();
				}
				vm.currentMarketData = {
					'labels' : [ 'Total Sell', 'Total Buy' ],
					'data' : []
				};

				TradeMessageStats.specificMarket(params, function(data) {
					if (data.length === 1) {
						var v = data[0];
						vm.currentMarketData.data.push(v[1]);
						vm.currentMarketData.data.push(v[2]);
					}
				});
			}
		}

		function loadCountryChart() {
			var params = {};
			if (vm.dateRange !== null) {
				var arr = vm.dateRange.split(',');
				params.fromDate = moment().subtract(arr[0], arr[1]).toISOString();
				params.toDate = moment().toISOString();
			}
			vm.countryData = {
				'labels' : [],
				'data' : [],
			};

			TradeMessageStats.byCountry(params, function(data) {
				angular.forEach(data, function(v, k) {
					vm.countryData.labels.push(v[0]);
					vm.countryData.data.push(v[1] + v[2]);
				});
			});
		}

	}
})();
