describe("The main controller", function () {

    var BASE_URL = "/blogposts/"

    var mainController;
    var $httpBackend;
    var $scope;

    var entry = {
        authorEmail: "guy@marom.com",
        title      : "title",
        content    : "content",
        _links     : {
            self: {
                href: "specific-entry"
            }
        }
    };

    beforeEach(module("app"));

    function initHttpBackend($httpBackend) {
        $httpBackend.whenGET(BASE_URL + "?page=0&size=10&sort=created,desc")
            .respond({
                _embedded: {
                    blogposts: [entry]
                }
            });

        $httpBackend.whenGET("specific-entry")
            .respond(entry);
    }

    beforeEach(inject(
            function ($injector) {
                var $rootScope = $injector.get("$rootScope");
                $scope = $rootScope.$new();
                var $http = $injector.get("$http")
                $httpBackend = $injector.get("$httpBackend");
                var $controller = $injector.get("$controller");

                initHttpBackend($httpBackend);

                mainController = $controller("mainController", {
                        "$scope": $scope,
                        "$http" : $http
                    }
                );

                $httpBackend.flush();
            })
    );

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    it("should load all the entries on startup", function () {
        var actualEntry = $scope.blogEntries[0];
        expect(actualEntry.authorEmail).toBe(entry.authorEmail);
        expect(actualEntry.title).toBe(entry.title);
        expect(actualEntry.content).toBe(entry.content);
        expect(actualEntry._links.self.href).toBe(entry._links.self.href);
    });

    //Uses the $scope.loadEntry() method defined in the controller
    function loadEntry() {
        $scope.loadEntry(entry);
        $httpBackend.flush();
    }

    it("should load a specific entry when requested", function() {
        loadEntry();
        expect($scope.entryAuthorEmail).toBe(entry.authorEmail);
        expect($scope.entryTitle).toBe(entry.title);
        expect($scope.entryContent).toBe(entry.content);
    });

    it("should unhide the entry div", function() {
        loadEntry();
        expect($scope.displayEntry).toBe(true);
    });
})
;
