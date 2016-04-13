'use strict';

describe('Controller Tests', function() {

    describe('Discount Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDiscount, MockProduct, MockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDiscount = jasmine.createSpy('MockDiscount');
            MockProduct = jasmine.createSpy('MockProduct');
            MockItem = jasmine.createSpy('MockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Discount': MockDiscount,
                'Product': MockProduct,
                'Item': MockItem
            };
            createController = function() {
                $injector.get('$controller')("DiscountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:discountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
