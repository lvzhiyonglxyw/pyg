app.controller("cartController", function ($scope, $window, $location, orderItemService, cartService, addressService, orderService) {

    //////////////////////////////////////////////////////////////使用redis///////////////////////////////////////////////////////////
// 加减商品数目
    $scope.addItemCartList = function (itemId, num) {
        cartService.addItemToCartList(itemId, num).success(function (response) {
            if (response.success) {
                $scope.findRedisCart();
            } else {
                alert(response.message);
            }
        })
    }
    // 显示购物车数据
    $scope.findRedisCart = function () {
        cartService.findCartListFromRedis().success(function (response) {
            $scope.cartList = response;
            sum();
        })
    }

        // 求商品总数量和总金额
        // $scope.cart = {orderItemsList: []};
        sum = function() {
            $scope.totalNum = 0;// 总数量
            $scope.totalMoney = 0;// 总金额
            for (var i = 0; i < $scope.cartList.length; i++) {
                var cart = $scope.cartList[i];
                for (var j = 0; j < cart.tbOrderItems.length; j++) {
                    $scope.totalNum += cart.tbOrderItems[j].num;// 总数量
                    $scope.totalMoney += cart.tbOrderItems[j].totalFee;// 总金额
                }

            }
        }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////使用LocalStorage///////////////////////////////////////////////////////////////////////////////////
    /* var storage = $window.localStorage;
     var cartList = storage.getItem("cartList");
     if (cartList == null) {
         cartList = "[]";
     }
     var itemId = $location.search()['itemId'];
     var num = $location.search()['num'];

     //查询购物车信息
     $scope.findRedisCart = function () {
         var cartList = storage.getItem("cartList");
         if (cartList == null) {
             cartList = "[]";
         }
         cartService.findRedisCart(JSON.parse(cartList)).success(
             function (response) {
                 $scope.cartList = response;
                 sun();
             }
         )
     };


     //加减购物车商品数量
     $scope.addItemCartList = function (itemId, num) {

         var cartList = storage.getItem("cartList");
         if (cartList == null) {
             cartList = "[]";
         }
         cartService.addItemCartList(JSON.parse(cartList), itemId, num).success(
             function (response) {
                 if (response.success) {
                     //把购物车集合放在LocalStorage 中
                     storage.setItem("cartList", response.message);
                     $scope.findRedisCart();

                 } else {
                     alert(response.message);
                 }
             }
         )
     };

     if (num > 0) {
         $scope.addItemCartList(itemId, num);
     }

     // $scope.cart = {tbOrderItems: []};

     sun=function(){
         $scope.totalNum = 0;
         $scope.totalMoney = 0;

         var cartList=storage.getItem("cartList");
         if (cartList == null) {
             cartList = "[]";
         }
         for (var i = 0; i <JSON.parse(cartList).length; i++) {
             //从购物车集合中获取购物车
             var cart = JSON.parse(cartList)[i];
             //从购物车中的商品明细集合中获取数量和总金额
             for (var j = 0; j < cart.tbOrderItems.length; j++) {
                 $scope.totalNum += cart.tbOrderItems[j].num;
                 $scope.totalMoney += cart.tbOrderItems[j].totalFee;
             }
         }
     };*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //查询收货地址
    $scope.findByUserId = function () {
        addressService.findByUserId().success(
            function (response) {
                $scope.addressList = response;
                for (var i = 0; i < response.length; i++) {
                    //循环一遍地址，发现默认地址直接赋值到选择后的$scope.address
                    if (response[i].isDefault == '1') {
                        $scope.address = response[i];
                    }
                }
            }
        )
    }

        //用户选择的地址
        $scope.selectAddress = function (address) {
            $scope.address = address;   //打算将用户选择的地址保存到前端$scope.address变量中
        }

        //是否当前选择的地址
        $scope.isSelectAddress = function (address) {
            if ($scope.address == address) {
                return true;
            }
            return false;
        }
        $scope.order = {paymentType: "1"};
        //支付类型，1、在线支付，2、货到付款
        $scope.selectPaymentType = function (value) {
            $scope.order.paymentType = value;
            /*alert($scope.order.paymentType);*/
        }


        $scope.selectIds = [];//选中的ID集合
        //更新复选
        $scope.updateSelection = function ($event, id) {
            if ($event.target.checked) {//如果是被选中,则增加到数组
                $scope.selectIds.push(id);
            } else {
                var idx = $scope.selectIds.indexOf(id);
                $scope.selectIds.splice(idx, 1);//删除
            }
        }


        //添加订单
        $scope.addCartOrder = function () {
            $scope.order.receiverAreaName = $scope.address.address;  //收货地址
            $scope.order.receiverMobile = $scope.address.mobile;//手机号
            $scope.order.receiver = $scope.address.contact;//联系人
            orderService.addCartOrder($scope.order, $scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        location.href = "pay.html";
                    } else {
                        alert(response.message);
                    }
                }
            )
        }
        //显示用户信息
        $scope.showName = function () {
            cartService.showName().success(
                function (response) {
                    $scope.loginName = response.name;
                }
            )
        }
    }
)