/**
 * Created by javier on 16/01/16.
 */

angular.module( 'pOSDesignPatternsExerciseApp' )
    .factory( 'PosService', function ( $rootScope, $q, Sale ) {

        var currentSale = {};
        var itemList = [];

        return {

            // Obsolete
            //load: function ( id ) {
            //
            //    return Sale.get( { id: id } )
            //        .$promise
            //        .then( function ( result ) {
            //            console.log( 'load: ', result );
            //
            //            currentSale = result;
            //            itemList = currentSale.items;
            //            return currentSale;
            //        } );
            //
            //},

            getSale: function () {

                return Sale.getLastUncompletedSale()
                    .$promise
                    .then( function ( result ) {
                        console.log( 'getLastUncompletedSale: ', result.id );

                        currentSale = result;
                        itemList = currentSale.items;
                        return currentSale;
                    } );

            },

            checkBarcode: function ( barcode ) {
                var deferred = $q.defer();

                console.log( 'Checking barcode ' + barcode + '...' );
                deferred.notify( 'Checking barcode ' + barcode + '...' );

                // Check barcode format
                var result = true;

                if ( result == true )
                    if ( !barcode )
                        result = false;

                if ( result == true )
                    if ( (barcode.length < 5) || (barcode.length > 13) )
                        result = false;

                for ( var i = 0; (result == true) && (i <= barcode.length - 1); i++ ) {
                    var digit = barcode.substring( i, i + 1 );

                    if ( ( digit < '0') || (digit > '9') ) {
                        result = false;
                    }
                }

                // Resolve o Reject
                if ( result ) {

                    console.log( 'Barcode ' + barcode + ' format is valid.' );
                    deferred.resolve( barcode );

                } else {

                    var error = {
                        status: 'Length Check Error.',
                        statusText: 'Invalid format: Barcode length must be between 6 and 13'
                    };

                    deferred.reject( error );

                }

                return deferred.promise;
            },

            // REST Service: "api/sales/{id}/add/{barcode}/{qty}"
            doAddToCart: function ( barcode, quantity ) {

                return this.checkBarcode( barcode )
                    .then( function () {
                        console.log( 'Saving ' + quantity + ' x ' + barcode + ' in Sale Id ' + currentSale.id );

                        return Sale.addItem(
                            {
                                id: currentSale.id,
                                barcode: barcode.toString(),
                                qty: quantity
                            }
                        ).$promise;
                    } );

            },

            // REST Service: "api/sales/{id}/summary"
            getSaleSummary: function () {
                return Sale.getSaleSummaryById(
                    {
                        id: currentSale.id
                    }
                ).$promise;
            },

            // REST Service: "api/sales/:id/pay/:payedamount/:paymentmethod"
            paySale: function ( payedamount, paymentmethod ) {
                return Sale.paySaleByPaymentMethod(
                    {
                        id: currentSale.id,
                        payedamount: payedamount,
                        paymentmethod: paymentmethod
                    }
                ).$promise;
            }

        };

    } );
