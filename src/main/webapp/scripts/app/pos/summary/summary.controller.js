/**
 * Created by javier on 16/01/16.
 */
angular.module('pOSDesignPatternsExerciseApp')
    .controller("PosSummaryController", function ($scope, $rootScope, $uibModalInstance, AlertService, $translate, PosService) {

        // Sale Summary state
        $scope.errorMessage = "";

        $scope.saleData = {};                   // complete JSON from Server
        $scope.itemList = [];                   // array of Items
        $scope.taxSummaryList = [];             // array of grouped Taxes calculations
        $scope.pointofsale = {};                // info about point of sale where Sale is been registered
        $scope.shopData = {};                   // shop contact information
        $scope.cashier = {};                    // info about cashier (user) who is registering the Sale

        $scope.totalQty = 0;
        $scope.totalWODiscount = 0;
        $scope.totalBase = 0;
        $scope.totalDiscount = 0;
        $scope.totalQuota = 0;
        $scope.totalAmount = 0;

        $scope.paymentMethod = "";              // payment method used by customer to pay the sale
        $scope.payedAmount = "";                // amount payed
        $scope.amountToReturn = 0;              // difference between payedAmount and totalAmount

        var splitSaleData = function () {
            if (typeof $scope.saleData.turn === "undefined") {
                // If user open Summary with 0 Item lines, Go Back
                $scope.goBack();
                return;
            }

            $scope.itemList = $scope.saleData.items;
            $scope.taxSummaryList = $scope.saleData.taxSummarys;
            $scope.pointofsale = $scope.saleData.turn.pointofsale;
            $scope.shopData = $scope.pointofsale.shop;
            $scope.cashier = $scope.saleData.turn.cashier;

            // Subtotals and other calculations
            for (var item of $scope.itemList) {
                $scope.totalQty += item.quantity;
                $scope.totalDiscount += item.discountamount;

                if (item.discount) {
                    item.discount.percentage = (-item.discount.percentage).toString() + "%";
                } else {
                    item.discount = {percentage: ""};
                }
            }

            for (var tax of $scope.taxSummaryList) {
                $scope.totalBase += tax.taxbase;
                $scope.totalQuota += tax.taxamount;
                $scope.totalAmount += tax.totalamount;
            }

            $scope.totalWODiscount = $scope.totalBase - $scope.totalDiscount;

            // Respect server grand total calculations
            if ($scope.totalAmount != $scope.saleData.totalamount) {
                $scope.totalAmount = $scope.saleData.totalamount;
            }

        };

        var loadSummary = function () {
            console.log("loadSummary: Getting Sale's " + $scope.saleData.id + " Summary.");

            // Gets all Summary of Sale after calculate and persist TaxSummary detail
            PosService.getSaleSummary()
                .then(
                    function (result) {
                        console.log("getSaleSummary: Sale " + $scope.saleData.id + " OK");

                        $scope.saleData = result;
                        splitSaleData();

                        $("#inputAmount").focus();
                    })
                .catch(
                    function (error) {
                        console.warn("getSaleSummary: Sale " + $scope.saleData.id + " ERROR. " + error.status + " " + error.statusText);

                        // Go Back if user reloads the page
                        $scope.goBack();
                        return ( error.status + ' ' + error.statusText );
                    })
                .finally(
                    function () {

                    }
                )

        };
        loadSummary();

        $scope.goBack = function () {

            if (typeof $uibModalInstance === "undefined") {

                $rootScope.back();

            } else {

                if (typeof $scope.saleData.payedamount === "undefined") {
                    $uibModalInstance.dismiss('cancel');
                } else {
                    $uibModalInstance.close($scope.saleData);
                }
            }
        };

        $scope.doPayment = function (paymentmethod) {

            $("#inputAmount").focus();
            $scope.errorMessage = "";

            if ($scope.totalAmount <= 0) {

                new Audio('assets/sounds/scan-error.mp3').play();

                AlertService.warning("pos.sale.pay.error", {
                    paymentMethod: paymentmethod,
                    saleId: $scope.saleData.id,
                    errorMessage: $translate.instant('pos.sale.pay.totalamountnegative')
                });
                console.warn('doPayment: Sale ' + $scope.saleData.id + ' has totalAmount not positive');

                $scope.goBack();
                return;

            } else {

                if ($scope.payedAmount < $scope.totalAmount) {
                    $scope.errorMessage = $translate.instant('pos.sale.pay.money.error', {
                        money: $scope.payedAmount === "" ? 0 : $scope.payedAmount,
                        totalAmount: $scope.totalAmount
                    });

                    new Audio('assets/sounds/scan-error.mp3').play();
                    return;
                }

            }

            // TODO: Automatic ticket printing
            PosService.paySale($scope.payedAmount, paymentmethod)
                .then(
                    function (result) {

                        $scope.saleData = result;

                        new Audio('assets/sounds/cash-register.mp3').play();

                        AlertService.success("pos.sale.pay.ok", {
                            saleId: $scope.saleData.id,
                            paymentMethod: paymentmethod,
                            paymentAuth: $scope.saleData.paymentauth
                        });
                        console.log('doPayment: Sale ' + $scope.saleData.id + ' paid by ' + paymentmethod + ' auth. ' + $scope.saleData.paymentauth);

                        return (result);
                    })
                .catch(
                    function (error) {

                        new Audio('assets/sounds/scan-error.mp3').play();

                        AlertService.error("pos.sale.pay.error", {
                            paymentMethod: paymentmethod,
                            saleId: $scope.saleData.id,
                            errorMessage: error.status + ' ' + error.statusText
                        });
                        console.warn('doPayment: Sale ' + $scope.saleData.id + ' error. ' + error.status + ' ' + error.statusText);

                        return ( error.status + ' ' + error.statusText );
                    })
                .finally(
                    function () {
                        $scope.goBack();
                    }
                )
        };

        var payedAmountBackup = "";
        $scope.onPayedChange = function () {

            // Because ngInclude's scope, must use this.payedAmount not $scope.payedAmount
            this.payedAmount = this.payedAmount.replace(",", ".");

            if (this.payedAmount == '.') {
                payedAmountBackup = this.payedAmount;
                return;
            }

            if (isNaN(this.payedAmount)) {
                this.payedAmount = payedAmountBackup;
            }

            if (Math.floor(this.payedAmount) != this.payedAmount) {
                this.payedAmount = Math.floor(this.payedAmount * 100) / 100;  // 2 Decimals only
            }

            // Set value from 'this' to 'scope' for use in other functions
            $scope.payedAmount = this.payedAmount;

            payedAmountBackup = $scope.payedAmount;

            $scope.amountToReturn = 0;

            if ($scope.totalAmount <= 0) {
                return;
            }

            if ($scope.payedAmount < $scope.totalAmount)
                return;

            $scope.amountToReturn = -($scope.payedAmount - $scope.totalAmount);
        };

        $scope.onInputAmountKeyUp = function ($event) {
            // Reference:
            //http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes

            $scope.errorMessage = "";

            var keyCode = $event.keyCode;

            // Supr
            if (keyCode == 46) {
                // because ngInclude's scope, must use this.payedAmount not $scope.payedAmount
                this.payedAmount = "";            // Clears the payedAmount
                $scope.payedAmount = this.payedAmount;
                $scope.onPayedChange();
            }

            // F8
            if (keyCode == 119) {
                $scope.doPayment('CREDITCARD');     // Credit Card Payment
            }

            // F9
            if ((keyCode == 120)) {
                $scope.doPayment('CASH');           // Cash Payment
            }

            // Esc
            if ((keyCode == 27)) {
                $scope.goBack();                    // Cancel Payment
            }

        }

    });
