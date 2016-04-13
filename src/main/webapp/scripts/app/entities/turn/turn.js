'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('turn', {
                parent: 'entity',
                url: '/turns',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.turn.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/turn/turns.html',
                        controller: 'TurnController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('turn');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('turn.detail', {
                parent: 'entity',
                url: '/turn/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pOSDesignPatternsExerciseApp.turn.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/turn/turn-detail.html',
                        controller: 'TurnDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('turn');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Turn', function($stateParams, Turn) {
                        return Turn.get({id : $stateParams.id});
                    }]
                }
            })
            .state('turn.new', {
                parent: 'turn',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/turn/turn-dialog.html',
                        controller: 'TurnDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    activated: null,
                                    start: null,
                                    end: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('turn', null, { reload: true });
                    }, function() {
                        $state.go('turn');
                    })
                }]
            })
            .state('turn.edit', {
                parent: 'turn',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/turn/turn-dialog.html',
                        controller: 'TurnDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Turn', function(Turn) {
                                return Turn.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('turn', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('turn.delete', {
                parent: 'turn',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/turn/turn-delete-dialog.html',
                        controller: 'TurnDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Turn', function(Turn) {
                                return Turn.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('turn', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
