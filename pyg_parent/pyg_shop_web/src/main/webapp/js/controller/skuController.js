//控制层
app.controller('skuController' ,function($scope,skuService){
    //读取列表数据绑定到表单中
    $scope.findSku=function(){
        skuService.findGoodsSku().success(
            function(response){
                $scope.list=response;
                //alert(JSON.stringify($scope.list.length))
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));
                // 指定图表的配置项和数据
                //app.title = '嵌套环形图';
                option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        data:[]
                    },
                    series: [
                        {
                            name:'访问来源',
                            type:'pie',
                            radius: ['50%', '70%'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:[]
                        }
                    ]
                };
                console.log($scope.list);
                var list = this.option.series[0].data;
                for(var i =0;i<$scope.list.length;i++){
                    //加第一个样式
                    this.option.legend.data.push($scope.list[i].title);
                    var obj = {};
                    obj.value = $scope.list[i].num;
                    obj.name =$scope.list[i].title;
                    //加第二个样式
                    this.option.series[0].data.push(obj);
                }
                myChart.setOption(option);
            }
        );
    }
});