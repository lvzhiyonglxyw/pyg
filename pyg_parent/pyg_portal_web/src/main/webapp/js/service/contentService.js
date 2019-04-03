app.service("contentService",function ($http) {
    //根据广告分类id查询信息
    this.contextList=function (id) {
        return $http.get("content/categoryIdFindAll.do?id="+id);
    }
    //根据状态查询
    this.findStatus = function () {
        return $http.get("promotionController/findStatus.do");
    }
})