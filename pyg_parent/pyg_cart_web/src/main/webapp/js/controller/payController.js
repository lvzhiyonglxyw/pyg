app.controller("payController", function ($scope, payService) {
    //显示用户信息
    $scope.showName = function () {
        payService.showName().success(
            function (response) {
                $scope.loginName = response.name;
            }
        )
    }
    //生成二维码
    $scope.createNative = function () {
        payService.createNative().success(
            function (response) {
                $scope.out_trade_no=response.out_trade_no;//订单号
                $scope.total_fee=response.total_fee;//支付金额
                var qr = window.qr = new QRious({
                    element: document.getElementById('qrious'),
                    size: 250,
                    value: response.code_url, //支付地址
                    level: 'H'
                });
                queryPayStatus(response.out_trade_no);
            }
        )
    }
    //查询订单状态
    queryPayStatus = function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(
            function (response) {
                if (response.success) {
                    location.href="paysuccess.html";
                }else{
                    if (response.message=="timeout"){
                        //连接超时,重新生成一个支付二维码
                        $scope.createNative();
                    }else{
                        location.href = "payfail.html";
                    }
                }
            }
        )
    }
})