'use strict';

angular.module('pOSDesignPatternsExerciseApp')
    .controller('PaymentMethodDetailController', function ($scope, $rootScope, $stateParams, entity, PaymentMethod, Sale) {
        $scope.paymentMethod = entity;
        $scope.load = function (id) {
            PaymentMethod.get({id: id}, function(result) {
                $scope.paymentMethod = result;
            });
        };
        var unsubscribe = $rootScope.$on('pOSDesignPatternsExerciseApp:paymentMethodUpdate', function(event, result) {
            $scope.paymentMethod = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
