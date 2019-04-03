app.service("timeStatisticsSellerService", function ($http) {

    //统计一年销售额（销售量的趋势图）
    this.findByYear = function () {
        return $http.get("../statisticsOfAnnualSalesController/findByYear.do");
    }
})