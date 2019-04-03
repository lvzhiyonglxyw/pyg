app.service("uploadService",function ($http) {
    this.uploadFile=function () {
        var formData=new FormData();//上传文件的数据模型s
        formData.append("file1",file.files[0]);//文件上传框的id必须是append的第二个参数一致
        return $http({
            method:"post",
            url:"../uploadController/uplad.do",
            data:formData,
            headers:{"Content-Type":undefined},//上传文件必须是这个类型，默认text/plain
            transformRequest:angular.inentity//对整个表单进行二进制系列化
        })
    }
})