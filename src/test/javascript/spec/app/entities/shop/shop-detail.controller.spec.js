'use strict';

describe('Controller Tests', function() {

    describe('Shop Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockShop, MockCatalog, MockPointofsale;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockShop = jasmine.createSpy('MockShop');
            MockCatalog = jasmine.createSpy('MockCatalog');
            MockPointofsale = jasmine.createSpy('MockPointofsale');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Shop': MockShop,
                'Catalog': MockCatalog,
                'Pointofsale': MockPointofsale
            };
            createController = function() {
                $injector.get('$controller')("ShopDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:shopUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
