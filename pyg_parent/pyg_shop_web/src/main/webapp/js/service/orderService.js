app.service("orderServices", function ($http) {
    //分页条件查询
    this.findOrderList=function (searchEntity) {
        return $http.post("../orderController/findAll.do",searchEntity);
    }
})
