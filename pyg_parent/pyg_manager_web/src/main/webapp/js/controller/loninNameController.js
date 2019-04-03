app.controller("loninNameController", function ($scope, loninNameService) {

    $scope.showName = function () {
        loninNameService.showName().success(
            function (respose) {
                $scope.showName = respose;
            }
        )
    }
})