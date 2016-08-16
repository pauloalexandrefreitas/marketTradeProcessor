(function() {
	'use strict';

	angular.module('marketTradeProcessorApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider', 'ChartJsProvider' ];

	function stateConfig($stateProvider, ChartJsProvider) {

		// Configure all charts
		ChartJsProvider.setOptions({
			chartColors : [ '#97BBCD', '#F7464A', '#46BFBD', '#A80000', '#FDB45C', '#949FB1', '#4D5360' ],
			responsive : true
		});

		$stateProvider.state('app', {
			abstract : true,
			views : {
				'navbar@' : {
					templateUrl : 'app/layouts/navbar/navbar.html',
					controller : 'NavbarController',
					controllerAs : 'vm'
				}
			},
			resolve : {
				authorize : [ 'Auth', function(Auth) {
					return Auth.authorize();
				} ],
				translatePartialLoader : [ '$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
					$translatePartialLoader.addPart('global');
				} ]
			}
		});
	}
})();
