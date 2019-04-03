//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService,typeTemplatesService) {

    $controller('baseController', {$scope: $scope});//继承

    //查询全部商品分类
    $scope.itemList={}
    $scope.findAll = function () {
        typeTemplatesService.findAll().success(
            function (respose) {
                for (var i = 0; i < respose.length; i++) {
                    $scope.itemList[respose[i].id] = respose[i].name;
                }
            }
        )
    }

    //实现面包屑跳转
    $scope.grade = 1;//当前等级
    $scope.setGrade = function (value) {
        //每点击查询下一级一次grade+1
        $scope.grade = value;
    }
    $scope.selectType = function (p_entity) {
        $scope.parentId = p_entity.id;//保存parentId
        //1第一级，顶级的时候让后面跟着的两个名称为空
        if ($scope.grade == 1) {
            //entity_1是第二级
            $scope.entity_1 = null;
            ////entity_2是第三级
            $scope.entity_2 = null;
        }
        //2第二级
        if ($scope.grade == 2) {
            //entity_1是第二级
            $scope.entity_1 = p_entity;
            //entity_2是第三级
            $scope.entity_2 = null;
        }
        //3第三级
        if ($scope.grade == 3) {
            $scope.entity_2 = p_entity;
        }
        $scope.findNextMun($scope.parentId);
    }

    //根据parent_id查询
    $scope.findNextMun = function (id) {
        itemCatService.findNextMun(id).success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        $scope.entity.parentId = $scope.parentId; //将保存的父分类id封装到entity对象
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            serviceObject = itemCatService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.findNextMun($scope.parentId);
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.findNextMun($scope.parentId);
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象


});	
