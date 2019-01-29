$(function () {
    var url='/o2o/shopadmin/getuserproductbyid';

    getProductNameandPoint();
    function getProductNameandPoint() {
        $.getJSON(url,function (data) {
            if(data.success){
                $('#product-name').html(data.productName);
                $('#product-point').html(data.point);
            }
        })
    }
})