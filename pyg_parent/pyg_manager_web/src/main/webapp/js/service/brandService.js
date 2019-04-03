//服务层
app.service('brandService', function ($http) {

    //查询实体
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    }
    //增加
    this.add = function (entity) {
        return $http.post('../brand/add.do', entity);
    }
    //修改
    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../brand/delete.do?ids=' + ids);
    }
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../brand/search.do?page=' + page + "&rows=" + rows, searchEntity);
    }


    this.selectbrandList = function () {
        return $http.get("../brand/selectbrandList.do");
    }
});
