//服务层
app.service('itemCatService', function ($http) {

    //查询全部商品分类信息
    this.findAll=function () {
        return $http.get('../itemCat/findAll.do');
    }


    //根据parent_id查询
    this.findNextMun = function (id) {
        return $http.get('../itemCat/findPage.do?id=' + id);
    }
    //查询实体
    this.findOne = function (id) {
        return $http.get('../itemCat/findOne.do?id=' + id);
    }
    //增加
    this.add = function (entity) {
        return $http.post('../itemCat/add.do', entity);
    }
    //修改
    this.update = function (entity) {
        return $http.post('../itemCat/update.do', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../itemCat/delete.do?ids=' + ids);
    }

});
