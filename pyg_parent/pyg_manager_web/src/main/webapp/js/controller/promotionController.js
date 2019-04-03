app.controller('promotionController', function ($controller,$location, $scope, uploadServices, promotionService) {
    $controller('baseController', {$scope: $scope});//继承
    $scope.entity = {};
    //图片上传
    $scope.uploadFile = function () {
        uploadServices.uploadFile().success(
            function (respose) {
                if (respose.success) {
                    $scope.entity.titleImg = respose.message;
                }
            }
        )
    }

    //获取时间
    $scope.time = function () {
        $scope.entity.startDate = document.getElementById("test1").value;
        $scope.entity.endDate = document.getElementById("test2").value;
    }


    //分页
    $scope.findPage = function (page, rows) {
        promotionService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    $scope.status=["未开始","开始"];
    //查询实体
    $scope.findOne = function () {

        id = $location.search()['id'];
        if (id != null) { //新增不做查询
            promotionService.findOne(id).success(
                function (response) {
                    $scope.entity = response;
                    editor.html($scope.entity.description);
                }
            );
        }
    }

    //保存
    $scope.save = function () {
        $scope.entity.description = editor.html();
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = promotionService.update($scope.entity); //修改
        } else {
            serviceObject = promotionService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                   location.href="promotion.html";
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        promotionService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        promotionService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

})