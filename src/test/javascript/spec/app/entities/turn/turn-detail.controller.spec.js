'use strict';

describe('Controller Tests', function() {

    describe('Turn Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTurn, MockPointofsale, MockSale, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTurn = jasmine.createSpy('MockTurn');
            MockPointofsale = jasmine.createSpy('MockPointofsale');
            MockSale = jasmine.createSpy('MockSale');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Turn': MockTurn,
                'Pointofsale': MockPointofsale,
                'Sale': MockSale,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("TurnDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pOSDesignPatternsExerciseApp:turnUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
