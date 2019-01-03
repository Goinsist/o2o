
function changeVerifyCode(img){
    img.src="../Kaptcha?"+Math.floor(Math.random()*100);
}
function getQueryString(name) {
    var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)");
    var r=window.location.search.substr(1).match(reg);
    if(r!=null){
        return decodeURIComponent(r[2]);
    }
    return '';

    /**
     * 获取项目的ContextPath以便修正图片路由让其正常显示
     */

}

function  getContextPath() {
    return "/o2o/";
}
