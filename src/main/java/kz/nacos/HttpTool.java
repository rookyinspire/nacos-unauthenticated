package kz.nacos;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpTool {
    private static int Timeout = 10000;

    private static String DefalutEncoding = "UTF-8";

    public static String httpRequest(String requestUrl, int timeOut, String requestMethod, String contentType, String postString, String encoding) throws Exception {
        if ("".equals(encoding) || encoding == null)
            encoding = DefalutEncoding;
        URLConnection httpUrlConn = null;
        HttpsURLConnection hsc = null;
        HttpURLConnection hc = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(requestUrl);
            if (requestUrl.startsWith("https")) {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] tm = { new MyCERT() };
                sslContext.init(null, tm, new SecureRandom());
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                hsc = (HttpsURLConnection)url.openConnection();
                hsc.setSSLSocketFactory(ssf);
                hsc.setHostnameVerifier(allHostsValid);
                hsc.setRequestMethod(requestMethod);
                httpUrlConn = hsc;
            } else {
                hc = (HttpURLConnection)url.openConnection();
                hc.setRequestMethod(requestMethod);
                httpUrlConn = hc;
            }
            httpUrlConn.setConnectTimeout(timeOut);
            httpUrlConn.setReadTimeout(timeOut);
            if (contentType != null && !"".equals(contentType))
                httpUrlConn.setRequestProperty("Content-Type", contentType);
            httpUrlConn.setConnectTimeout(timeOut);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.connect();
            if (null != postString && !"".equals(postString)) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(postString.getBytes(encoding));
                outputStream.flush();
                outputStream.close();
            }
            inputStream = httpUrlConn.getInputStream();
            String result = readString(inputStream, encoding);
            return result;
        } catch (IOException ie) {
            if (hsc != null)
                return readString(hsc.getErrorStream(), encoding);
            if (hc != null)
                return readString(hc.getErrorStream(), encoding);
            return "";
        } catch (Exception e) {
            throw e;
        } finally {
            if (hsc != null)
                hsc.disconnect();
            if (hc != null)
                hc.disconnect();
        }
    }

    public static HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public static String readString(InputStream inputStream, String encoding) throws IOException {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new BufferedInputStream(inputStream);
            baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] arr = new byte[1];
            while ((len = bis.read(arr)) != -1)
                baos.write(arr, 0, len);
            return baos.toString(encoding);
        } catch (IOException e) {
            throw e;
        } finally {
            if (baos != null) {
                baos.flush();
                baos.close();
            }
            if (bis != null)
                bis.close();
            if (inputStream != null)
                inputStream.close();
        }
    }

    public static String getHttpReuest(String requestUrl, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, "GET", null, "", encoding);
    }
    public static String postHttpReuest(String requestUrl,String poststring, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, "POST", null,poststring , encoding);
    }
}