'use strict';

describe('Controller Tests', function() {

    describe('Item Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItem, MockDiscount, MockTax, MockCatalog;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItem = jasmine.createSpy('MockItem');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockTax = jasmine.createSpy('MockTax');
            MockCatalog = jasmine.createSpy('MockCatalog');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Item': MockItem,
                'Discount': MockDiscount,
                'Tax': MockTax,
                'Catalog': MockCatalog
            };
            createController = function() {
                $injector.get('$controller')("ItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:itemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
