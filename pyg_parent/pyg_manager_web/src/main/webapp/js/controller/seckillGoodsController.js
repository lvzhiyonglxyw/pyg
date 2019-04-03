app.controller("seckillGoodsController", function ($scope, $controller, uploadServices, goodsService, seckillGoodsService, itemServices) {

    //假继承
    $controller("baseController", {$scope: $scope});
    $scope.entity = {};
    //修改审核状态
    $scope.updateStatus = function (status) {
        seckillGoodsService.updateStatus($scope.selectIds, status).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];//选中的ID集合
                } else {
                    alert(response.message);
                }
            })
    }
    //删除秒杀商品信息
    $scope.delete = function () {
        seckillGoodsService.delete($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];//选中的ID集合
                } else {
                    alert(response.message);
                }
            })
    }
    //根据秒杀商品di查询信息
    $scope.findOne = function (id) {
        seckillGoodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    }
    $scope.saveTime = function () {
        $scope.entity.startTime = document.getElementById('test3').value;
        $scope.entity.endTime = document.getElementById('test4').value;

    }

    //保存秒杀信息
    $scope.save = function () {
        var obj;
        if ($scope.entity.id == null) {
            //添加
            obj = seckillGoodsService.add($scope.entity);
        } else {
            //修改
            obj = seckillGoodsService.update($scope.entity);
        }
        obj.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];//选中的ID集合
                } else {
                    alert(response.message);
                }
            }
        )
    }
    //图片上传

    $scope.uploadFile = function () {
        uploadServices.uploadFile().success(
            function (response) {
                $scope.entity.smallPic = response.message;
            }
        )
    }

    //查询spu商品表
    $scope.findSpuAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.goodsList = response;
            }
        )
    }

    $scope.$watch("entity.goodsId", function (newValue, oldValue) {

        itemServices.findByGoodsId(newValue).success(
            function (response) {
                $scope.itemList = response;
            }
        )
    })
    //审核状态0已提交审核，1已审核，2未提交审核
    $scope.status = ["已提交审核", "已审核", "未提交审核"];
    //查询秒杀商品
    $scope.search = function (pageNum, pageSize) {
        seckillGoodsService.findManagerAll(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    }
})