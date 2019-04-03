app.service("seachItenService", function ($http) {

    this.seach = function (seachMap) {
        return $http.post("seachItenController/seach.do",seachMap);
    }
})