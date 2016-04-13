/**
 * Created by javier on 24/01/16.
 */
'use strict';

angular.module( 'pOSDesignPatternsExerciseApp' )
    .config( function ( $stateProvider ) {
        $stateProvider
            .state( 'pos', {
                parent: 'site',
                url: '/pos',
                data: {
                    authorities: [ 'ROLE_CASHIER' ]
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/pos/pos.html',
                        controller: 'PosController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: [ '$translate', '$translatePartialLoader', function ( $translate, $translatePartialLoader ) {
                        $translatePartialLoader.addPart( 'pos' );
                        return $translate.refresh();
                    } ],
                    posService: 'PosService'
                }
            } )
            .state( 'pos.summary', {
                parent: 'pos',
                url: '/summary',
                data: {
                    authorities: [ 'ROLE_CASHIER' ]
                },

                //// Summary in full page mode
                //// (remove $uibModalInstance from summary.controller.js is necessary)
                //views: {
                //    'content@': {
                //        templateUrl: 'scripts/app/pos/summary/summary.html',
                //        controller: 'PosSummaryController'
                //    }
                //},
                //resolve: {
                //    posService: 'PosService'
                //}

                //// Summary in model page mode
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/pos/summary/summary.html',
                        controller: 'PosSummaryController',
                        size: 'lg',
                        resolve: {
                            posService: 'PosService'
                        }
                    }).result.then(function(result) {
                        $state.go('pos', null, { reload: true });
                    }, function() {
                        $state.go('pos');
                    })
                }]
            } );
    } );
