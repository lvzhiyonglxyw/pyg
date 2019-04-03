//控制层
app.controller('userController', function ($scope, $controller, userService) {

    $controller('baseController', {$scope: $scope});//继承
    //获取登录用户名
    $scope.response={};
    $scope.showName=function(){
        userService.showName().success(
            function (response) {
                $scope.loginName=response.name;
            }
        )
    }
    //注册用户信息
    $scope.save = function () {
        if ($scope.entity.password==$scope.password){
            userService.add($scope.entity,$scope.smsCode).success(
                function (response) {
                    alert(response.message);
                }
            );
        } else{
            alert("两次输入的密码不一致");
        }
    }
    //发送短信验证码
    $scope.entity={};
    $scope.sendSms=function(){
        if ($scope.entity.phone!=null){
            userService.sendSms($scope.entity.phone).success(
                function (response) {
                    if (response.success){
                        alert(response.message);
                        location.href="login.html"
                    }else{
                        alert(response.message);
                    }
                }
            )
        }else {
            alert("手机号码不能为空！");
        }
    }
});	
