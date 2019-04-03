app.service("addressService",function ($http){

    //查询收货地址
    this.findByUserId=function () {
        return $http.get("addressController/findByUserId.do");
    }
})