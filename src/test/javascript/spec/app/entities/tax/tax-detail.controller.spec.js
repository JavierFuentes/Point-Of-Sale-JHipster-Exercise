'use strict';

describe('Controller Tests', function() {

    describe('Tax Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTax, MockProduct, MockTaxSummary, MockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTax = jasmine.createSpy('MockTax');
            MockProduct = jasmine.createSpy('MockProduct');
            MockTaxSummary = jasmine.createSpy('MockTaxSummary');
            MockItem = jasmine.createSpy('MockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tax': MockTax,
                'Product': MockProduct,
                'TaxSummary': MockTaxSummary,
                'Item': MockItem
            };
            createController = function() {
                $injector.get('$controller')("TaxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:taxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
