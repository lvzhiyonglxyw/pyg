app.service('seckillService',function($http){

	//获取当前用户名
    this.showName = function(){
        return $http.get('seckillGoodsController/showName.do');
    }
    //查询全部秒杀商品
	this.findSeckillList = function(){
		return $http.get('seckillGoodsController/findSeckillList.do');
	}

	//根据秒杀商品id查询商品
	this.findOne = function(id){
		return $http.get('seckillGoodsController/findOne.do?id='+id);
	}
    //秒杀下单
	this.saveSeckillOrder = function(id){
		return $http.get('seckillGoodsController/saveSeckillOrder.do?id='+id);
	}
})