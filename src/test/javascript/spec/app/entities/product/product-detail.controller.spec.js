'use strict';

describe('Controller Tests', function() {

    describe('Product Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProduct, MockDiscount, MockTax, MockCatalog;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProduct = jasmine.createSpy('MockProduct');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockTax = jasmine.createSpy('MockTax');
            MockCatalog = jasmine.createSpy('MockCatalog');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Product': MockProduct,
                'Discount': MockDiscount,
                'Tax': MockTax,
                'Catalog': MockCatalog
            };
            createController = function() {
                $injector.get('$controller')("ProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:productUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
