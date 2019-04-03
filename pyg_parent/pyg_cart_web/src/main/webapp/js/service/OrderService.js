app.service("orderService", function ($http) {
    //添加订单
    this.addCartOrder = function (order) {
        return $http.post("orderController/addCartOrder.do", order);
    }
})