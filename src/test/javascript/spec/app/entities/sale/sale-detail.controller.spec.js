'use strict';

describe('Controller Tests', function() {

    describe('Sale Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSale, MockTurn, MockPaymentMethod, MockTaxSummary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSale = jasmine.createSpy('MockSale');
            MockTurn = jasmine.createSpy('MockTurn');
            MockPaymentMethod = jasmine.createSpy('MockPaymentMethod');
            MockTaxSummary = jasmine.createSpy('MockTaxSummary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Sale': MockSale,
                'Turn': MockTurn,
                'PaymentMethod': MockPaymentMethod,
                'TaxSummary': MockTaxSummary
            };
            createController = function() {
                $injector.get('$controller')("SaleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:saleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
