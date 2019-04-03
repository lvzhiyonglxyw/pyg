//控制层
app.controller('seckillOrderController', function ($scope,$controller,seckillGoodsService,seckillOrderService) {

    $controller('baseController', {$scope: $scope});//继承

    //查询秒杀商品
    $scope.seckill = [];
    $scope.findAll = function () {
        seckillGoodsService.findAll().success(
            function (response) {
                $scope.seckillGoodsList = response;
                for (var i = 0; i < response.length; i++) {
                    $scope.seckill[response[i].id] = response[i].title;
                }
            }
        );
    }
    $scope.status = ["未支付", "已支付"];

    //获取支付时间
    $scope.saveTime = function () {
        $scope.entity.payTime = document.getElementById('test3').value;
    }
    //查询实体
    $scope.findById = function (id) {
        seckillOrderService.findById(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存

    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID

            serviceObject = seckillOrderService.update($scope.entity); //修改
        } else {
            serviceObject = seckillOrderService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        seckillOrderService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }
    //搜索
    $scope.search = function (pageNum, pageSize) {
        seckillOrderService.search(pageNum, pageSize).success(
        function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;//更新总记录数
        }
    )
}
});	
