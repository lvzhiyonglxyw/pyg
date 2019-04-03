//服务层
app.service('userService', function ($http) {
    //获取登录用户名
    this.showName = function () {
        return $http.get('tbUserController/showName.do');
    }
    //增加
    this.add = function (entity,code) {
        return $http.post('tbUserController/save.do?code='+code, entity);
    }
    //发送短信
    this.sendSms = function (phone) {
        return $http.get("http://localhost:9106/tbUserController/sendSms.do?phone=" + phone,{'withCredentials':true});
    }
});
