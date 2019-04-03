//服务层
app.service('seckillOrderService',function($http){
	    	

	//查询实体
	this.findById=function(id){
		return $http.get('../seckillOrder/findById.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../seckillOrder/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../seckillOrder/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../seckillOrder/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows){
		return $http.post('../seckillOrder/search.do?pageNum='+page+"&pageSize="+rows);
	}

});
