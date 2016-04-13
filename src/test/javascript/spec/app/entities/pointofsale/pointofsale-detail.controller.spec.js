'use strict';

describe('Controller Tests', function() {

    describe('Pointofsale Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPointofsale, MockShop, MockTurn;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPointofsale = jasmine.createSpy('MockPointofsale');
            MockShop = jasmine.createSpy('MockShop');
            MockTurn = jasmine.createSpy('MockTurn');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pointofsale': MockPointofsale,
                'Shop': MockShop,
                'Turn': MockTurn
            };
            createController = function() {
                $injector.get('$controller')("PointofsaleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:pointofsaleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
