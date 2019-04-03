app.service('skuService',function ($http) {
    this.findGoodsSku=function () {
        return $http.get('../sku/findGoodsSku.do');
    }
})