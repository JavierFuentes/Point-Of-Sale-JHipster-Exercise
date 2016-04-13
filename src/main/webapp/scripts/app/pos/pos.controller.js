/**
 * Created by javier on 16/01/16.
 */
angular.module('pOSDesignPatternsExerciseApp')
    .controller("PosController", function ($scope, $rootScope, $state, AlertService, posService) {

        //$("#barcodeinput").focus();

        var navVisible = true;

        var urlBarcodeImageGenerator = [];
        urlBarcodeImageGenerator[0] = 'https://www.barcodesinc.com/generator_files/image.php?code=';
        urlBarcodeImageGenerator[1] = '&style=452&type=C128B&width=200&height=40&xres=1&font=1';

        // Point of Sale state
        $scope.signShowed = "+";
        $scope.displayValue = "";                   //current barcode digits displayed on screen
        $scope.totalValue = 0;                      //total value displayed on screen
        $scope.clearValue = true;                   //should value displayed on screen be cleared after new digit pressed?

        $scope.urlBarcodeImage = '';
        $scope.sale = {};
        $scope.itemList = [];

        var updateScope = function (saleData) {
            $scope.sale = saleData;

            $rootScope.sale = saleData;
            $rootScope.cashierName = $scope.sale.turn.cashier.firstName + ' ' + $scope.sale.turn.cashier.lastName;
            $rootScope.shopName = $scope.sale.turn.pointofsale.shop.name;
            $rootScope.posNumber = $scope.sale.turn.pointofsale.id;

            $scope.itemList = $scope.sale.items;
            $scope.totalValue = $scope.sale.totalamount;
            $scope.displayValue = "";
        };

        var loadSale = function () {
            posService
                .getSale()
                .then(function (result) {
                    updateScope(result);

                    if ($rootScope.previousStateName != "pos.summary") {

                        AlertService.info("pos.welcome", {
                            name: $rootScope.cashierName,
                            shopName: $rootScope.shopName,
                            posNumber: $rootScope.posNumber
                        }, "center");

                    }

                    console.log('loadSale: SaleId', $scope.sale.id);
                    console.log('loadSale: itemList: ', $scope.itemList);
                })
                .catch(function (error) {
                    AlertService.error("pos.sale.load.error", {
                        saleId: id,
                        errorMessage: error.message
                    });

                    console.warn('loadSale: ' + id + ' ' + error.message);
                })
        };
        loadSale();

        $scope.digitKeys = [
            {label: "7", value: "7"},
            {label: "8", value: "8"},
            {label: "9", value: "9"},
            {label: "4", value: "4"},
            {label: "5", value: "5"},
            {label: "6", value: "6"},
            {label: "1", value: "1"},
            {label: "2", value: "2"},
            {label: "3", value: "3"},
            {label: "0", value: "0"}
        ];

        $scope.operationKeys = [
            {
                label: '+/-',
                operation: function () {
                    // Switch sign in next Item added to Sale
                    $scope.signShowed = ($scope.signShowed === "+" ? "-" : "+");
                }
            },
            {
                label: 'C',
                operation: function () {
                    // Clear Operation
                    displayValueBackup = "";
                    $scope.signShowed = "+";
                    $scope.displayValue = '';
                }
            },
            {
                label: "Cart",
                operation: function () {
                    // Add a new Item to Cart. Quantity can be positive or negative.
                    displayValueBackup = "";
                    var barcode = $scope.displayValue;
                    var qty = eval(($scope.signShowed + "1"));

                    $scope.urlBarcodeImage = urlBarcodeImageGenerator[0] + barcode + urlBarcodeImageGenerator[1];

                    posService.doAddToCart(barcode, qty)
                        .then(
                            function (result) {
                                new Audio('assets/sounds/scan-reader.mp3').play();

                                updateScope(result);

                                AlertService.success("pos.sale.barcode.added", {
                                    barcode: barcode,
                                    saleId: result.id
                                });
                                console.log('doAddToCart: Barcode ' + barcode + ' added to Sale ' + result.id);

                                return (result);
                            })
                        .catch(
                            function (error) {
                                new Audio('assets/sounds/scan-error.mp3').play();

                                AlertService.warning("pos.sale.barcode.error", {
                                    barcode: barcode,
                                    errorMessage: error.status + ' ' + error.statusText
                                });

                                console.warn('doAddToCart: ' + error.status + ' ' + error.statusText);
                            })
                        .finally(
                            function () {
                                $scope.signShowed = "+";
                                $scope.displayValue = "";
                            }
                        )
                }
            },
            {
                label: 'getSaleSummary',
                operation: function () {
                    $rootScope.previousStateName = $rootScope.toState;
                    $rootScope.previousStateParams = $rootScope.toStateParams;

                    $state.go('pos.summary');
                }
            }

        ];


        // actions
        /**
         * When digit is clicked, it should be added to displayed value or replace displayed value.
         * @param digit what digitKey was clicked
         */
        $scope.digitClicked = function (digit) {
            //$("#barcodeinput").focus();

            new Audio('assets/sounds/key-press.mp3').play();

            if ($scope.clearValue == true) {

                $scope.displayValue = digit;
                $scope.clearValue = false;

            } else {

                $scope.displayValue = $scope.displayValue + '' + digit;

            }
        };

        /**
         * When operation key is clicked operation is executed
         * and next pushed digit should replace the displayed value
         * @param operation which operationKey was clicked
         */
        $scope.operationClicked = function (operation) {
            //$("#barcodeinput").focus();

            new Audio('assets/sounds/key-press.mp3').play();

            operation();

            $scope.clearValue = true;
        };

        var displayValueBackup = "";
        $scope.onBarcodeChange = function () {
            $scope.displayValue = $scope.displayValue.replace(",.", "");

            if (isNaN($scope.displayValue))
                $scope.displayValue = displayValueBackup;

            displayValueBackup = $scope.displayValue;

            if ($scope.displayValue.length == 13) {
                $scope.operationKeys[2].operation();   // addToCart
            }
        };

        $scope.onBarcodeKeyup = function ($event) {

            //Reference:
            //http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes

            var operationExecuted = false;
            var keyCode = $event.keyCode;

            // Intro
            if (keyCode == 13) {
                operationExecuted = true;
                $scope.operationKeys[2].operation();   // addToCart
            }

            // Plus or Minus
            if ((keyCode == 107) || (keyCode == 109)) {
                operationExecuted = true;
                $scope.operationKeys[0].operation();   // Change Sign
            }

            // Backspace, Supr, Esc, 'X'
            if ((keyCode == 8) || (keyCode == 46) || (keyCode == 27) || (keyCode == 88)) {
                operationExecuted = true;
                $scope.operationKeys[1].operation();   // Clears the barcode input
            }

            // Space Bar
            if ((keyCode == 32)) {
                operationExecuted = true;
                $scope.operationKeys[3].operation();   // getSummary to get Pay
            }

            // Any Number
            if (((keyCode >= 48) && (keyCode <= 57)) ||     // Numeric
                ((keyCode >= 96) && (keyCode <= 105)) )     // Pad
            {
                operationExecuted = true;
            }

            if (operationExecuted == true) {
                new Audio('assets/sounds/key-press.mp3').play();
            }

        }

        $scope.AlterShowNavigationBar = function () {

            if (navVisible == true) {
                navVisible = false;
                $('#header-wrap').filter(':not(:animated)').slideUp();
            } else {
                navVisible = true;
                $('#header-wrap').filter(':not(:animated)').slideDown();
            }

        }

    });
