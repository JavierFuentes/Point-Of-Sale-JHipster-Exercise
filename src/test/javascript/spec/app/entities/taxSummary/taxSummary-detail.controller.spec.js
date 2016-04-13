'use strict';

describe('Controller Tests', function() {

    describe('TaxSummary Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTaxSummary, MockTax, MockSale;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTaxSummary = jasmine.createSpy('MockTaxSummary');
            MockTax = jasmine.createSpy('MockTax');
            MockSale = jasmine.createSpy('MockSale');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TaxSummary': MockTaxSummary,
                'Tax': MockTax,
                'Sale': MockSale
            };
            createController = function() {
                $injector.get('$controller')("TaxSummaryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:taxSummaryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
