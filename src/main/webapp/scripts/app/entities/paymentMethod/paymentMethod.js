'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('paymentMethod', {
                parent: 'entity',
                url: '/paymentMethods',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.paymentMethod.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paymentMethod/paymentMethods.html',
                        controller: 'PaymentMethodController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paymentMethod');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('paymentMethod.detail', {
                parent: 'entity',
                url: '/paymentMethod/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.paymentMethod.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paymentMethod/paymentMethod-detail.html',
                        controller: 'PaymentMethodDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paymentMethod');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PaymentMethod', function($stateParams, PaymentMethod) {
                        return PaymentMethod.get({id : $stateParams.id});
                    }]
                }
            })
            .state('paymentMethod.new', {
                parent: 'paymentMethod',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paymentMethod/paymentMethod-dialog.html',
                        controller: 'PaymentMethodDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('paymentMethod', null, { reload: true });
                    }, function() {
                        $state.go('paymentMethod');
                    })
                }]
            })
            .state('paymentMethod.edit', {
                parent: 'paymentMethod',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paymentMethod/paymentMethod-dialog.html',
                        controller: 'PaymentMethodDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PaymentMethod', function(PaymentMethod) {
                                return PaymentMethod.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('paymentMethod', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('paymentMethod.delete', {
                parent: 'paymentMethod',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paymentMethod/paymentMethod-delete-dialog.html',
                        controller: 'PaymentMethodDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PaymentMethod', function(PaymentMethod) {
                                return PaymentMethod.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('paymentMethod', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
