'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('shop', {
                parent: 'entity',
                url: '/shops',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.shop.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shop/shops.html',
                        controller: 'ShopController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shop');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('shop.detail', {
                parent: 'entity',
                url: '/shop/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.shop.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/shop/shop-detail.html',
                        controller: 'ShopDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('shop');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Shop', function($stateParams, Shop) {
                        return Shop.get({id : $stateParams.id});
                    }]
                }
            })
            .state('shop.new', {
                parent: 'shop',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/shop/shop-dialog.html',
                        controller: 'ShopDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    address: null,
                                    city: null,
                                    phone: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('shop', null, { reload: true });
                    }, function() {
                        $state.go('shop');
                    })
                }]
            })
            .state('shop.edit', {
                parent: 'shop',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/shop/shop-dialog.html',
                        controller: 'ShopDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Shop', function(Shop) {
                                return Shop.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shop', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('shop.delete', {
                parent: 'shop',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/shop/shop-delete-dialog.html',
                        controller: 'ShopDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Shop', function(Shop) {
                                return Shop.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('shop', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
