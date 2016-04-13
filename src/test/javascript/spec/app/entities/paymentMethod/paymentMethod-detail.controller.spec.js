'use strict';

describe('Controller Tests', function() {

    describe('PaymentMethod Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPaymentMethod, MockSale;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPaymentMethod = jasmine.createSpy('MockPaymentMethod');
            MockSale = jasmine.createSpy('MockSale');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PaymentMethod': MockPaymentMethod,
                'Sale': MockSale
            };
            createController = function() {
                $injector.get('$controller')("PaymentMethodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:paymentMethodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
