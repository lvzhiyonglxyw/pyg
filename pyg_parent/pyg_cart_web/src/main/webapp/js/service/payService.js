app.service("payService",function ($http) {
    //显示用户信息
    this.showName = function () {
        return $http.get("payController/showName.do");
    }
    //生成支付二维码
    this.createNative=function () {
        return $http.get("payController/createNative.do");
    }
    //查询支付订单状态
    this.queryPayStatus=function (out_trade_no) {
        return $http.get("payController/queryPayStatus.do?out_trade_no="+out_trade_no);
    }
})