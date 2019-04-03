app.service("cartService", function ($http) {

    //显示用户信息
    this.showName = function () {
        return $http.get("cartController/showName.do");
    }
    //使用redis
// 显示购物车信息
    this.findCartListFromRedis = function () {
        return $http.get("cartController/findCartListFromRedis.do");
    }
    //使用LocalStorage
    //查询购物车信息   
    /* this.findRedisCart = function (cartList) {
         //使用redis
        // return $http.post("cartController/findRedisCart.do");
           return $http({
               url : 'cartController/findRedisCart.do',
               method : 'POST',
               data : {
                   "cartList": cartList
               },
               contentType: "application/json",
               dataType : 'json'
           });
     }
 */
    //使用redis
    // 加减商品数目
    this.addItemToCartList = function (itemId, num) {
        return $http.get("cartController/addItemToCartList.do?num=" + num
            + "&itemId=" + itemId);
    }
    //使用LocalStorage
    /* //加减购物车商品数量
     this.addItemCartList = function (cartList,itemId, num) {

         // return $http.post('cartController/addItemCartList.do?itemId=' + itemId + "&num=" + num , cartList);
         //return $http.get("cartController/addItemCartList.do?itemId=" + itemId + "&num=" + num);
         return $http({
             url : 'cartController/addItemCartList.do',
             method : 'POST',
             data : {
                 "cartList":cartList,
                 "itemId": itemId,
                 "num": num
             },
             contentType: "application/json",
             dataType : 'json'
         });
     }*/


})