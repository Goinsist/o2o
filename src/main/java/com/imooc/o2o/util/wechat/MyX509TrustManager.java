/**
 * FileName: MyX509TrustManager
 * Author:   gongyu
 * Date:     2018/6/14 11:52
 * Description:
 */
package com.imooc.o2o.util.wechat;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/14
 * @since 1.0.0
 */

/**
 * 证书信任管理器（用于https请求）
 */
public class MyX509TrustManager implements X509TrustManager {

@Override
public void checkClientTrusted(X509Certificate[] chain, String authType)throws CertificateException{

}

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
