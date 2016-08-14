(function() {
    'use strict';

    angular
        .module('marketTradeProcessorApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trade-message', {
            parent: 'entity',
            url: '/trade-message?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'marketTradeProcessorApp.tradeMessage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trade-message/trade-messages.html',
                    controller: 'TradeMessageController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tradeMessage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('trade-message-detail', {
            parent: 'entity',
            url: '/trade-message/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'marketTradeProcessorApp.tradeMessage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trade-message/trade-message-detail.html',
                    controller: 'TradeMessageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tradeMessage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TradeMessage', function($stateParams, TradeMessage) {
                    return TradeMessage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trade-message',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trade-message-detail.edit', {
            parent: 'trade-message-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-message/trade-message-dialog.html',
                    controller: 'TradeMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TradeMessage', function(TradeMessage) {
                            return TradeMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trade-message.new', {
            parent: 'trade-message',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-message/trade-message-dialog.html',
                    controller: 'TradeMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                currencyFrom: null,
                                currencyTo: null,
                                amountSell: null,
                                amountBuy: null,
                                rate: null,
                                timePlaced: null,
                                originatingCountry: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trade-message', null, { reload: true });
                }, function() {
                    $state.go('trade-message');
                });
            }]
        })
        .state('trade-message.edit', {
            parent: 'trade-message',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-message/trade-message-dialog.html',
                    controller: 'TradeMessageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TradeMessage', function(TradeMessage) {
                            return TradeMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trade-message', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trade-message.delete', {
            parent: 'trade-message',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-message/trade-message-delete-dialog.html',
                    controller: 'TradeMessageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TradeMessage', function(TradeMessage) {
                            return TradeMessage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trade-message', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
