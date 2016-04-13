'use strict';

describe('Controller Tests', function() {

    describe('Catalog Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCatalog, MockProduct, MockShop, MockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCatalog = jasmine.createSpy('MockCatalog');
            MockProduct = jasmine.createSpy('MockProduct');
            MockShop = jasmine.createSpy('MockShop');
            MockItem = jasmine.createSpy('MockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Catalog': MockCatalog,
                'Product': MockProduct,
                'Shop': MockShop,
                'Item': MockItem
            };
            createController = function() {
                $injector.get('$controller')("CatalogDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:catalogUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
