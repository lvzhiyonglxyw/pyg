app.service("loninNameService", function ($http) {

    this.showName = function () {
        return $http.get("../loninNameController/showName.do");
    }
})