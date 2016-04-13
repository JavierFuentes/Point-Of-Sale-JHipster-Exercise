'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('taxSummary', {
                parent: 'entity',
                url: '/taxSummarys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.taxSummary.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taxSummary/taxSummarys.html',
                        controller: 'TaxSummaryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taxSummary');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('taxSummary.detail', {
                parent: 'entity',
                url: '/taxSummary/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.taxSummary.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/taxSummary/taxSummary-detail.html',
                        controller: 'TaxSummaryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('taxSummary');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaxSummary', function($stateParams, TaxSummary) {
                        return TaxSummary.get({id : $stateParams.id});
                    }]
                }
            })
            .state('taxSummary.new', {
                parent: 'taxSummary',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/taxSummary/taxSummary-dialog.html',
                        controller: 'TaxSummaryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    taxbase: null,
                                    taxrate: null,
                                    taxamount: null,
                                    totalamount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('taxSummary', null, { reload: true });
                    }, function() {
                        $state.go('taxSummary');
                    })
                }]
            })
            .state('taxSummary.edit', {
                parent: 'taxSummary',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/taxSummary/taxSummary-dialog.html',
                        controller: 'TaxSummaryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TaxSummary', function(TaxSummary) {
                                return TaxSummary.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taxSummary', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('taxSummary.delete', {
                parent: 'taxSummary',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/taxSummary/taxSummary-delete-dialog.html',
                        controller: 'TaxSummaryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TaxSummary', function(TaxSummary) {
                                return TaxSummary.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('taxSummary', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
