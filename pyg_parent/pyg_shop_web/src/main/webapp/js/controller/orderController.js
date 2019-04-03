app.controller('orderController', function ($scope,$controller,orderServices) {

       $controller('baseController', {$scope: $scope});//继承

    $scope.status=["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];
   //查询订单
    $scope.searchEntity = {};//定义搜索对象

    $scope.time=function () {
        $scope.searchEntity.createTime=document.getElementById("test1").value;
    }

    //搜索
    $scope.findAll = function () {
        orderServices.findOrderList($scope.searchEntity).success(
            function (response) {
                $scope.orderList = response;
            }
        );
    }

});