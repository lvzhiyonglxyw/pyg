app.service("seckillGoodsService", function ($http) {

    //修改秒杀商品审核状态s
    this.updateStatus = function (ids, status) {
        return $http.get("../seckillGoodsController/updateStatus.do?ids=" + ids + "&status=" + status);
    }
    //删除秒杀商品信息
    this.delete = function (ids) {
        return $http.get("../seckillGoodsController/delete.do?ids=" + ids);
    }
    //添加秒杀商品信息
    this.add = function (entity) {
        return $http.post("../seckillGoodsController/add.do", entity);
    }
    //修改秒杀商品信息
    this.update = function (entity) {
        return $http.post("../seckillGoodsController/update.do", entity);
    }
    //根据秒杀商品id查询
    this.findOne = function (id) {
        return $http.get("../seckillGoodsController/findOne.do?id=" + id);
    }
    //运营商查询未审核秒杀商品
    this.findManagerAll = function (pageNum, pageSize) {
        return $http.get("../seckillGoodsController/findManagerAll.do?pageNum=" + pageNum + "&pageSize=" + pageSize);
    }
    //根据当前商家id查询秒杀商品
    this.findAll = function () {
        return $http.get("../seckillGoodsController/findAll.do");
    }
})