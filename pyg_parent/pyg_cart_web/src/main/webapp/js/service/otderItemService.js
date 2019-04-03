app.service("orderItemService", function ($http) {
    //根据商家id从redis中获取数据在根据itemId查询
    this.findBySellerIdAndItemId=function (itemIdArray,sellerIds) {
        return $http.get("orderItemController/findBySellerIdAndItemId.do?sellerId="+sellerIds+"&itemId="+itemIdArray)
    }
})