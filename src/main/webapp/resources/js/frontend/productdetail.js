$(function() {
    function  getContextPath() {
        return "/o2o/";
    }
    var productId = getQueryString('productId');
    //获取商品信息的URL
    var productUrl = '/o2o/shopadmin/getproductbyid?productId='
        + productId;
    function  getContextPath() {
        return "/o2o/";
    }
//访问后台获取该商品的信息并渲染
    $.getJSON(

            productUrl,
            function(data) {
                if (data.success) {
                    //获取商品信息
                    var product = data.product;
                    //给商品信息相关HTML控件赋值
                    //商品缩略图
                    $('#product-img').attr('src',getContextPath()+product.imgAddr);
                    //商品更新时间
                    $('#product-time').text(
                        new Date(product.lastEditTime)
                            .Format("yyyy-MM-dd"));
                    if(product.point!=undefined){
$('#product-point').text('购买可得'+product.point+'积分');

                    }
                    //商品名称
                    $('#product-name').text(product.productName);
                    //商品简介
                    $('#product-desc').text(product.productDesc);
                    var imgListHtml = '';
                    //遍历商品详情图列表，并生成批量img标签
                    product.productImgList.map(function(item, index) {
                        imgListHtml += '<div> <img src="' +getContextPath()+item.imgAddr + '" width="100%"/></div>';
                    });
                    // if(data.needQRCode) {
                    //     // 生成购买商品的二维码供商家扫描
                    //     imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
                    //         + product.productId + '" width="100%"/></div>';
                    // }
                    $('#imgList').html(imgListHtml);
                }
            });
    $('#me').click(function() {
        $.openPanel('#panel-right-demo');
    });
    $.init();
});
