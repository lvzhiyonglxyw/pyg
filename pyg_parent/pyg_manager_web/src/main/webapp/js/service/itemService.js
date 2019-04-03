//服务层
app.service('itemServices', function ($http) {

    //根据spuid查询sku信息
    this.findByGoodsId = function (id) {
        return $http.get('../itemController/findByGoodsId.do?id=' + id);
    }

});
