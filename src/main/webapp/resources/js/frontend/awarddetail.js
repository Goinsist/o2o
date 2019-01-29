$(function () {

    function  getContextPath() {
        return "/o2o";
    }
    var awardId=getQueryString('awardId');

    var userAwardId=getQueryString('userAwardId');
    var url='/o2o/shopadmin/getawardbyid?awardId='+awardId+'&userAwardId='+userAwardId;
    getAwardDetail();
    function getAwardDetail() {
        $.getJSON(url,function (data) {
            if(data.success){

                $('.title').text(data.awardName);
                $('#award-img').attr('src',getContextPath()+data.awardImg);
                $('#create-time').text(new Date(data.createTime).Format('yyyy-MM-dd HH:mm:ss'));
                $('#award-desc').text(data.awardDesc);
                $('#award-point').text('兑换需要'+data.point+'点积分');
                var imgListHtml='';
                if(data.needQRCode) {
                    // 若顾客已登录,生成购买商品的二维码供商家扫描
                    imgListHtml += '<p><span style="color:#EE0000">请让店员扫码后领取'+data.awardName+'</span></p><div > <img src="/o2o/frontend/generateqrcode4award?userAwardId='
                        + data.userAwardId + '" width="60%" /></div>';
                }
                $('#imgList').html(imgListHtml);
            }
        })
    }

    //侧边栏按钮事件绑定
    $('#me').click(function() {
        $.openPanel('#panel-left-demo');
    });
})