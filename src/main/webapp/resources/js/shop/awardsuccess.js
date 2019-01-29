$(function () {
    var url='/o2o/shopadmin/getawardbyid';

    getAwardNameandPoint();
    function getAwardNameandPoint() {
        $.getJSON(url,function (data) {
            if(data.success){
                $('#award-name').html(data.awardName);

            }
        })
    }
})