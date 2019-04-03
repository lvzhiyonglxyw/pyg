//服务层
app.service('sellerServices', function ($http) {

    //excel导入
    this.dataFromExcel=function () {
        var formData=new FormData();//上传文件的数据模型
        formData.append("uploadFile",file.files[0]);//文件上传框的id必须是append的第二个参数一致
        return $http({
            method:"post",
            url:"../sellerPoiImportController/dataFromExcel.do",
            data:formData,
            headers:{"Content-Type":undefined},//上传文件必须是这个类型，默认text/plain
            transformRequest:angular.inentity//对整个表单进行二进制系列化
        })
    }

    //显示当前登录用户名
    this.showName=function(){
        return $http.get("../logingNameController/showName.do")
    }
    //修改审核状态信息
    this.updateStatus=function(sellerId,start){
        return $http.get("../seller/updateStatus.do?sellerId="+sellerId+"&start="+start);
    }
    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../seller/findAll.do');
    }
    //分页
    this.findPage = function (page, rows) {
        return $http.get('../seller/findPage.do?page=' + page + '&rows=' + rows);
    }
    //查询实体
    this.findOne = function (id) {
        return $http.get('../seller/findOne.do?id=' + id);
    }
    //增加
    this.add = function (entity) {
        return $http.post('../seller/add.do', entity);
    }
    //修改
    this.update = function (entity) {
        return $http.post('../seller/update.do', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../seller/delete.do?ids=' + ids);
    }
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../seller/search.do?page=' + page + "&rows=" + rows, searchEntity);
    }
});
