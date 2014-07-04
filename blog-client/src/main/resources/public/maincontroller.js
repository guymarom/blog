app.controller("mainController", function ($scope, $http) {
    var BASE_URL = '/blogposts/';

    function loadLatestTenEntries() {
        $http.get(BASE_URL + '?page=0&size=10&sort=id,desc')
            .success(function (result) {
                $scope.blogEntries = result._embedded.blogposts;
            });
    }

    function populateEntryData(data) {
        $scope.entryAuthorEmail = data.authorEmail;
        $scope.entryTitle = data.title;
        $scope.entryContent = data.content;
    }

    function populateAndDisplayEntry(entry) {
        populateEntryData(entry);
        setHideButtonText('Hide');
        hideSaveButton();
        showEntryDiv();
    }

    $scope.loadEntry = function loadEntry(entry) {
        var url = entry._links.self.href;
        $http.get(url)
            .success(function test(entry) {
                populateAndDisplayEntry(entry);
            });
    };

    function showSaveButton() {
        $scope.showSaveButton = true;
    }

    function hideSaveButton() {
        $scope.showSaveButton = false;
    }

    function showEntryDiv() {
        $scope.displayEntry = true;
    }

    function hideEntryDiv() {
        $scope.displayEntry = false;
    }

    function init() {
        hideEntryDiv();
        loadLatestTenEntries();
    }

    function resetEntry() {
        populateEntryData({
            authorEmail: '',
            title      : '',
            content    : ''
        });
    }

    function hideAndResetEntry() {
        hideEntryDiv();
        resetEntry();
    }

    $scope.hideAndResetEntry = hideAndResetEntry;

    function setHideButtonText(text) {
        $scope.hideButtonText = text;
    }

    $scope.showCreateEntryDialog = function showCreateEntryDialog() {
        setHideButtonText('Cancel');
        showSaveButton();
        showEntryDiv();
    };

    function getEntryFromDialog() {
        return {
            authorEmail: $scope.entryAuthorEmail,
            title: $scope.entryTitle,
            content: $scope.entryContent
        };
    }

    function entryAddedSuccessfully(data) {
        hideAndResetEntry();
        loadLatestTenEntries();
    }

    function errorAddingEntry(data, status, headers, config) {
        alert(data);
    }

    $scope.createNewEntry = function createNewEntry() {
        var entry = getEntryFromDialog();
        $http.post(BASE_URL, entry)
            .success(entryAddedSuccessfully)
            .error(errorAddingEntry);
    };

    init();

});