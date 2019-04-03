app.controller("seachItenController", function (seachItenService, $scope) {


    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,   //当前页
        totalItems: 10,  //总记录数
        itemsPerPage: 10, //每页记录数
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.searchMap.pageNo = $scope.paginationConf.currentPage; //需要重新赋值
            $scope.searchMap.pageSize = $scope.paginationConf.itemsPerPage;
            $scope.search();//重新加载
        }
    };

    //封装搜索对象
    $scope.searchMap = {keywords:'三星',spec:{},sort:"ASC",pageNo:$scope.paginationConf.currentPage,pageSize:$scope.paginationConf.itemsPerPage};

    //关键字搜索
    $scope.search = function () {
        seachItenService.seach($scope.searchMap).success(
            function (response) {
                $scope.list = response.content;
                //alert(JSON.stringify($scope.list));
                //返回了总记录数
                $scope.paginationConf.totalItems = response.total; //设置回分页控件
            }
        )
    }

    //将分类条件添加到searchMap中
    $scope.addFilterCondition = function (key, value) {

        if(key == 'brand' || key == 'category'|| key == 'price'){ //品牌和分类和价格
            $scope.searchMap[key] = value;
        }else{ //如果选的是规格
            $scope.searchMap.spec[key] = value; //{'brand':'三星',spec:{屏幕尺寸:'40寸'}}
        }
        //再调用查询方法即可
        $scope.search();
    }

    //排序点击事件
    $scope.sortSearch = function (value) {
        $scope.searchMap.sort = value;
        $scope.search();
    }

    //移除查询条件
    $scope.removeSearchItem = function (key) {
        if(key == 'brand' || key == 'category'|| key == 'price'){
            // delete 可以删除掉前端map中的key
            delete $scope.searchMap[key];
        }else{
            delete $scope.searchMap.spec[key];
        }

        $scope.search();
    }


})

//angularjs的过滤器'$sce安全规格的指令，名字是trustHtml
app.filter('trustHtml',['$sce',function($sce){
    return function(data){
        return $sce.trustAsHtml(data);  //将页面数据放行，不再过滤
    }
}]);