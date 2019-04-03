//控制层
/*app.controller('goodsController', function ($scope,$location,$controller, uploadService, goodsService, itemCatsService, typeTemplateService) {*/
app.controller("goodsController", function ($scope, $location, $controller, goodsService, typeTemplateService, itemCatsService, uploadService) {
    $controller('baseController', {$scope: $scope});//继承
    //////////////////////////////////////////////////////////////////////////////////////
    //商品数据回显
    //根据商品id查询商品信息
    //查询实体

    $scope.abc = [];
    $scope.findOneGoods = function (id) {
        id = $location.search()['id'];
        if (id != null) { //新增不做查询
            //$location可以获取浏览器地址  angularJS
            //alert($location.search()['id']);
            goodsService.findOne(id).success(
                function (response) {
                    $scope.entity = response;
                    editor.html($scope.entity.tbGoodsDesc.introduction);
                    $scope.entity.tbGoodsDesc.itemImages = JSON.parse($scope.entity.tbGoodsDesc.itemImages);
                    $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
                    $scope.abc = $scope.entity.tbGoodsDesc.customAttributeItems ;
                   /* for (var i = 0; i < $scope.entity.tbGoodsDesc.customAttributeItems.length; i++) {
                        alert($scope.entity.tbGoodsDesc.customAttributeItems[i].text);
                        alert($scope.entity.tbGoodsDesc.customAttributeItems[i].value);
                    }*/
                    //读取规格
                    $scope.entity.tbGoodsDesc.specificationItems = JSON.parse($scope.entity.tbGoodsDesc.specificationItems);

                    //准备SKU信息
                    for (var i = 0; i < $scope.entity.itemList.length; i++) {
                        $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                    }
                }
            );
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////
    //修改商品状态信息
    $scope.updateStatus = function (status) {
        goodsService.updateStatus($scope.selectIds, status).success(
            function (respose) {
                if (respose.success) {
                    $scope.reloadList();
                    $scope.selectIds = [];
                } else {
                    alert("操作失败");
                }
            }
        );
    }
    //商品列表显示
    $scope.searchEntity = {};//定义搜索对象
    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    $scope.status = ["未申请", "申请中", "审核通过", "审核未通过", "商品上架", "商品下架", "商品关闭"];
    //查询商品分类信息
    $scope.itemList = [];
    $scope.findAll = function () {
        itemCatsService.findAll().success(
            function (respose) {
                for (var i = 0; i < respose.length; i++) {
                    $scope.itemList[respose[i].id] = respose[i].name;
                }
            }
        );
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //商品添加

    //将图片封装到对象中
    $scope.image_entity = {tbGoodsDesc: []}
    $scope.addImage = function () {
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
    }
    //将图片移除对象
    $scope.delImage = function ($index) {
        $scope.entity.tbGoodsDesc.itemImages.splice($index, 1);
    }
    //文件上传
    $scope.image_entity = {};//初始化图片对象
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (respose) {
                if (respose.success) {
                    $scope.image_entity.url = respose.message;
                }
            }
        )
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//entity赋值后，才能对.后面的属性进行赋值
    $scope.entity = {tbGoodsDesc: {customAttributeItems: [], itemImages: [], specificationItems: []}, itemList: []};

    //根据规格名称和选项名称返回是否被勾选
    $scope.checkAttributeValue = function (specName, optionName) {
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        var object = searchObjectByKey(items, 'attributeName', specName);
        if (object == null) {
            return false;
        } else {
            if (object.attributeValue.indexOf(optionName) >= 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    //name规格名  value规格选项名  $event是checkbox
    $scope.updateSpecAttribute = function ($event, name, value) {

        var object = searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems, "attributeName", name);

        //object是{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}
        if (object != null) {


            //还需要判断当前checkbox的状态
            if ($event.target.checked) { //如果是勾选直接push规格选项
                object.attributeValue.push(value); //如果找到该规格，直接push到attributeValue的集合里即可
            } else {
                //取消勾选需要删除规格选项
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);

                //需要判断移除该规格下的所有规格选项
                if (object.attributeValue.length < 1) {
                    //将当前的object从商品.specificationItems的集合中移除该对象
                    $scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(object), 1);
                }
            }

        } else { //如果没从$scope.entity.tbGoodsDesc.specificationItems找到该规格，直接push新对象
            $scope.entity.tbGoodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
        }
    }

    //list ,  key是attributeName   value是规格的名称
    searchObjectByKey = function (list, key, value) {
        //循环list
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == value) {
                return list[i];
            }
        }
        return null;
    }


    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];// 初始
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) { //用户勾选的规格选项
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    }

    //参数1：[ {spec : {},price : 0,num : 99999,status : '0',isDefault : '0'} ],规格名称,规格选项集合
    addColumn = function (list, columnName, conlumnValues) {
        var newList = [];// 新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];  //获取出当前行的内容 {spec:{},price:'0.01',num:'99999',status:'0',isDefault:'0'}
            for (var j = 0; j < conlumnValues.length; j++) {//循环attributeValue数组的内容
                var newRow = JSON.parse(JSON.stringify(oldRow));// 深克隆,根据attributeValue的数量
                newRow.spec[columnName] = conlumnValues[j];//{spec:{"网络制式":"移动4G"},price:'0.01',num:'99999',status:'0',isDefault:'0'}
                newList.push(newRow);
            }
        }
        return newList;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //根据parent_id查询
    $scope.findNextMun = function () {
        itemCatsService.findNextMun(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        );
    }
    //通过选择一级分类进行二级分类显示
    $scope.$watch("entity.tbGoods.category1Id", function (newValue, oldValue) {
        itemCatsService.findNextMun(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        );
    })
    //通过选择二级分类进行三级分类显示
    $scope.$watch("entity.tbGoods.category2Id", function (newValue, oldValue) {

        itemCatsService.findNextMun(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        );
    })
    //使用watch判断上一级变化的时候进行查询模板
    //通过选择三级分类进行模板类型显示
    $scope.$watch("entity.tbGoods.category3Id", function (newValue, oldValue) {

        itemCatsService.findOne(newValue).success(
            function (response) {
                $scope.entity.tbGoods.typeTemplateId = response.typeId;
            }
        );
    })

    //使用watch判断上一级变化的时候进行查询品牌信息
    //通过模板类型进行品牌显示
    $scope.$watch("entity.tbGoods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                $scope.typeTemplate = response;//讲对象保存到typeTemplate中
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
                //将模板中自定义属性赋值到封装对象中
                $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
            }
        );
        typeTemplateService.findSpecList(newValue).success(
            function (response) {
                $scope.specList = response; //将返回的List<Map>封装到前端对象$scope.specList
            }
        )
    })


    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //保存
    $scope.save = function () {
        $scope.entity.tbGoodsDesc.introduction = editor.html();//从富文本框中取值
        var serviceObject;//服务层对象
        if ($scope.entity.tbGoods.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {

            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    location.href = "goods.html"
                } else {
                    alert(response.message);
                }
            }
        );
    }
    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }
});	
