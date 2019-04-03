app.controller("contentController",function ($scope,contentService) {

    //根据广告分类id查询信息
    $scope.categoryIdFindAll=function (id) {
        contentService.contextList(id).success(
            function (response) {
                $scope.contentList=response;
            }
        )
    }
    //根据状态查询
    $scope.findStatus = function () {
        contentService.findStatus().success(
            function (response) {
                $scope.promotionList = response;
            }
        );
    }
})