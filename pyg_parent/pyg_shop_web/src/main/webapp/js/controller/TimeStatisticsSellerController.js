app.controller("timeStatisticsSellerController", function ($scope, timeStatisticsSellerService) {

    //统计一年销售额（销售量的趋势图）
    $scope.findByYear = function () {
        timeStatisticsSellerService.findByYear().success(
            function (response) {
                $scope.list = response;
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));
                // 指定图表的配置项和数据
                var option = {
                    color: ['#3398DB'],
                    tooltip : {
                        trigger: 'axis',
                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis : [
                        {
                            type : 'category',
                            data : [],
                            axisTick: {
                                alignWithLabel: true
                            }
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value'
                        }
                    ],
                    series : [
                        {
                            name:'直接访问',
                            type:'bar',
                            barWidth: '60%',
                            data:[]
                        }
                    ]
                };
                console.log($scope.list);
                var list = this.option.series[0].data;
                for(var i =0;i<$scope.list.length;i++){
                    //加第一个样式
                    this.option.legend.data.push($scope.list[i].createTime);
                    var obj = {};
                    obj.name =$scope.list[i].num;
                    //加第二个样式
                    this.option.series[0].data.push(obj);
                }
                myChart.setOption(option);
            }
        )
    }
})