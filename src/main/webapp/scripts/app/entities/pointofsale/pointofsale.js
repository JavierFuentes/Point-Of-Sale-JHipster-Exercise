'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pointofsale', {
                parent: 'entity',
                url: '/pointofsales',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.pointofsale.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pointofsale/pointofsales.html',
                        controller: 'PointofsaleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pointofsale');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pointofsale.detail', {
                parent: 'entity',
                url: '/pointofsale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.pointofsale.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pointofsale/pointofsale-detail.html',
                        controller: 'PointofsaleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pointofsale');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pointofsale', function($stateParams, Pointofsale) {
                        return Pointofsale.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pointofsale.new', {
                parent: 'pointofsale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pointofsale/pointofsale-dialog.html',
                        controller: 'PointofsaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    activated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pointofsale', null, { reload: true });
                    }, function() {
                        $state.go('pointofsale');
                    })
                }]
            })
            .state('pointofsale.edit', {
                parent: 'pointofsale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pointofsale/pointofsale-dialog.html',
                        controller: 'PointofsaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pointofsale', function(Pointofsale) {
                                return Pointofsale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pointofsale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pointofsale.delete', {
                parent: 'pointofsale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pointofsale/pointofsale-delete-dialog.html',
                        controller: 'PointofsaleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Pointofsale', function(Pointofsale) {
                                return Pointofsale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pointofsale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
