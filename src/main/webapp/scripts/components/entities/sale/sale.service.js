'use strict';

angular.module( 'pOSDesignPatternsExerciseApp' )
    .factory( 'Sale', function ( $resource, DateUtils ) {
        return $resource( 'api/sales/:id', {}, {

            'query': {
                method: 'GET', isArray: true
            },

            'get': {
                method: 'GET',
                transformResponse: function ( data ) {
                    data = angular.fromJson( data );
                    data.time = DateUtils.convertDateTimeFromServer( data.time );
                    return data;
                }
            },

            'update': {
                method: 'PUT'
            },

            //http://localhost:8080/api/lastuncompletedsale
            'getLastUncompletedSale': {
                method: 'GET',
                url: 'api/lastuncompletedsale'
            },

            //http://localhost:8080/api/sales/1/add/11111/1
            'addItem': {
                method: 'PUT',
                url: 'api/sales/:id/add/:barcode/:qty',
                params: {
                    id: '@id',
                    barcode: '@barcode',
                    qty: '@qty'
                }
            },

            //http://localhost:8080/api/sales/1/summary
            'getSaleSummaryById': {
                method: 'GET',
                url: 'api/sales/:id/summary',
                params: {
                    id: '@id'
                }
            },

            //http://localhost:8080/api/sales/:id/pay/:payedamount/:paymentmethod
            'paySaleByPaymentMethod': {
                method: 'PUT',
                url: 'api/sales/:id/pay/:payedamount/:paymentmethod',
                params: {
                    id: '@id',
                    payedamount: '@payedamount',
                    paymentmethod: '@paymentmethod'
                }
            }

        } );
    } );
