app.controller("itemController", function ($scope, $http,$window) {

    $scope.num = 1; //购买的数量

    //数量加减
    $scope.addNum = function (x) {
        $scope.num += x;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
        if ($scope.num > 99) {
            $scope.num = 99;
        }
    }


    $scope.specification = {};  //将选择的规格选项保存到该变量
    //将用户选择的规格选项进行保存
    $scope.selectSpecification = function (key, value) {
        $scope.specification[key] = value; //给一个map赋值
        searchSku(); //选择后进行查询匹配
    }

    //判断当前的规格及规格选项是否已经勾选了
    $scope.isSelectSpec = function (key, value) {
        if ($scope.specification[key] == value) {
            return true;
        } else {
            return false;
        }
    }

    $scope.sku = {}; //定义了sku对象
    //将默认的sku对象设置到已选择的$scope.specification
    $scope.loadSku = function () {
        $scope.sku = skuList[0];  //取得第一个
        $scope.specification = JSON.parse(JSON.stringify($scope.sku.spec)); //将列表中的sku对象的spec属性赋值给specification
    }

    //将页面上选择的规格对比skuList查找确定是哪个sku对象
    searchSku = function () {
        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec, $scope.specification)) {
                $scope.sku = skuList[i];
                return;
            }
        }
    }

    //匹配两个map对象是否相等，需要个循环一遍
    matchObject = function (map1, map2) {
        for (var k in map1) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        for (var k in map2) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        return true;
    }
//添加购物车的
   /* $scope.addCartItem=function(){
        alert('itemId:' + $scope.sku.id);
        location.href="http://localhost:9107/cart.html#?itemId="+$scope.sku.id+"&num="+$scope.num;
    }*/
        //使用redis
    	$scope.addCartItem=function ()  {
            $http.get(
                "http://localhost:9107/cartController/addItemCartList.do?itemId="+$scope.sku.id+"&num="+$scope.num,{'withCredentials':true}
                ).success(
                function (response) {
                    if (response.success){
                        location.href="http://localhost:9107";
                    } else{
                        alert(response.message);
                    }
                }
            );
        }
});