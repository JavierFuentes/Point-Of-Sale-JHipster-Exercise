'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tax', {
                parent: 'entity',
                url: '/taxs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.tax.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tax/taxs.html',
                        controller: 'TaxController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tax');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tax.detail', {
                parent: 'entity',
                url: '/tax/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.tax.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tax/tax-detail.html',
                        controller: 'TaxDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tax');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Tax', function($stateParams, Tax) {
                        return Tax.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tax.new', {
                parent: 'tax',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tax/tax-dialog.html',
                        controller: 'TaxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    description: null,
                                    rate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tax', null, { reload: true });
                    }, function() {
                        $state.go('tax');
                    })
                }]
            })
            .state('tax.edit', {
                parent: 'tax',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tax/tax-dialog.html',
                        controller: 'TaxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Tax', function(Tax) {
                                return Tax.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tax', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tax.delete', {
                parent: 'tax',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tax/tax-delete-dialog.html',
                        controller: 'TaxDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Tax', function(Tax) {
                                return Tax.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tax', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
