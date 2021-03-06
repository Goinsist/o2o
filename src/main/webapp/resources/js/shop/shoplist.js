$(function() {
  getlist();
    function getlist(e) {
        $.ajax({
            url: "/o2o/shopadmin/getshoplist",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    handleUser(data.user);
                    handleList(data.shopList);

                }
            }


        });

    }
    function handleUser(data) {
        $('#user-name').text(data.name);
    }

    function handleList(data) {
        var html = '';
        data.map(function (item, index) {
            html += '<div class="row row-shop"><div class="col-40">'+ item.shopName +'</div><div class="col-40" >'+shopStatus(item.enableStatus) +'</div><div class="col-20">'+ goShop(item.enableStatus, item.shopId) +'</div></div>';

        });
        $('.shop-wrap').html(html);
    }

    function goShop(status, id) {
        if (status != 0 && status != -1) {
            return '<a href="/o2o/shopadmin/shopmanagement?shopId='+ id +'">进入</a>';
        } else {
            return '';
        }
    }

    function shopStatus(status) {
        if (status == 0) {

            return '<span id="shop-status" style="color:#EEB422">审核中</span>';
        } else if (status == -1) {

            return '<span id="shop-status" style="color:#EE0000">店铺非法</span>';
        } else {

            return '<span id="shop-status" style="color:#007947">审核通过</span>';
        }
    }

});