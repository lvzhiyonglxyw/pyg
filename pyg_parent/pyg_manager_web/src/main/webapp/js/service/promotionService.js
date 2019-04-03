app.service('promotionService',function ($http) {

    //查询实体
    this.findOne=function(id){
        return $http.get('../promotionController/findOne.do?id='+id);
    }
    //增加 
    this.add=function(entity){
        return  $http.post('../promotionController/add.do',entity );
    }
    //修改 
    this.update=function(entity){
        return  $http.post('../promotionController/update.do',entity );
    }
    //删除
    this.dele=function(ids){
        return $http.get('../promotionController/delete.do?ids='+ids);
    }
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../promotionController/search.do?pageNum='+page+"&pageSize="+rows, searchEntity);
    }

})

