
package com.zp.mobile91q;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpUtil {
    public final static String POST = "POST";

    public final static String GET = "GET";

    private String url;

    private Map<String, List<String>> parameterMap;// 参数列表

    private Map<String, String> heardMap;// 参数列表

    private String requestMethod = GET;// 请求方式

    private String encode = "UTF-8";

    private String contentType = "text/html; charset=UTF-8";

    public static int connectTimeout = 8000;

    public static int readTimeout = 20000;

    private HttpURLConnection conn;
    
    private String TTAG = "QQ00ADRDQQYLMB02";	//BQ00ADRD0000MB01  EOS0MUJIHERNTV00  BQ00ADRDQQYLMB02

    /**
     * 设置url参数
     * 
     * @param url
     * @param parameter
     */
    public HttpUtil(String url, Map<String, List<String>> parameter) {
        parameterMap = parameter;
        this.url = url;
    }

    /**
     * 设置url 参数
     * 
     * @param url
     * @param parameter
     */
    public HttpUtil(String url, String parameter) {
        addParameter("", parameter);
        this.url = url;
    }

    /**
     * 设置url
     * 
     * @param url
     */
    public HttpUtil(String url) {
        this.url = url;
    }

    /**
     * 获取网页内容
     * 
     * @return
     * @throws IOException
     * @throws IOException
     * @throws IOException
     */
    public String getUrlContent() throws IOException, UnknownHostException {
        BufferedReader breader = null;
        try {
            InputStream in = getUrlInputStream();
            if ("gzip".equals(conn.getContentEncoding())) {
                in = new GZIPInputStream(in);
            }
            if (in == null) {
                return "";
            }
            breader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuilder content = new StringBuilder();
            String str;
            while ((str = breader.readLine()) != null) {
                content.append(str).append("\n");
            }
            if (content.length() > 0)
                content.deleteCharAt(content.length() - 1);
            return content.toString();
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (breader != null)
                try {
                    breader.close();
                } catch (IOException ie) {
                }
        }
    }

    /**
     * 下载文件到本地
     * 
     * @param filename 本地文件名
     */
    public void download(String filename) throws IOException {
        getHttpURLConnection();// 初始conn
        OutputStream os = null;
        InputStream is = null;
        try {
            is = getUrlInputStream();
            if (is == null)
                throw new IOException("UrlInputStream is null");
            File file = new File(filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            byte[] bs = new byte[1024];// 1K的数据缓区
            os = new FileOutputStream(filename);
            int len;
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException ie) {
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException ie) {
                }
        }
    }

    public void addHeardMap(String name, String value) {
        if (heardMap == null)
            heardMap = new HashMap<String, String>();
        heardMap.put(name, value);
    }

    /**
     * 添加参数
     * 
     * @param name
     * @param value
     */
    public void addParameter(String name, String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        addParameter(name, values);
    }

    /**
     * 添加参数
     * 
     * @param name
     * @param values
     */
    public void addParameter(String name, List<String> values) {
        if (parameterMap == null)
            parameterMap = new HashMap<String, List<String>>();

        parameterMap.put(name, values);
    }

    /**
     * 添加参数
     * 
     * @param parameterMap
     */
    public void addParameterMap(Map<String, List<String>> parameterMap) {
        if (parameterMap == null)
            this.parameterMap = parameterMap;
        else
            this.parameterMap.putAll(parameterMap);
    }

    /**
     * 获取参数
     * 
     * @return
     */
    public Map<String, List<String>> getParameterMap() {
        return parameterMap;
    }

    /**
     * 获取请求数据
     * 
     * @return
     */
    public Map<String, List<String>> getRequestProperty() {
        return conn.getRequestProperties();
    }

    /**
     * 获取url的流
     * 
     * @return
     * @throws IOException
     */
    private InputStream getUrlInputStream() throws IOException, UnknownHostException {
        getHttpURLConnection();// 初始conn

        if (heardMap != null && heardMap.size() > 0) {
            Iterator<String> keys = heardMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();

                conn.setRequestProperty(key, heardMap.get(key));
            }
            heardMap.clear();
        }
        // 设置超时
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setAllowUserInteraction(false);
        conn.addRequestProperty("Ttag", TTAG);
        conn.addRequestProperty("Tcip", System.currentTimeMillis() + "");//1573703457042
        conn.addRequestProperty("BBNO", "21010218");//SciflyKuSDK.getBBNO()

        if (POST.equals(requestMethod) && parameterMap != null
                && parameterMap.size() > 0) {
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            byte[] post = toString(parameterMap).getBytes();
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Content-Length", String.valueOf(post.length));
            conn.getOutputStream().write(post);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
        }
        conn.connect();

        try {
            return conn.getInputStream();
        } catch (IOException e) {
            return conn.getErrorStream();
        }
    }

    /**
     * 获取HttpURLConnection
     * 
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private HttpURLConnection getHttpURLConnection()
            throws MalformedURLException, IOException {
        if (conn == null) {
            String tempUrl = url;
            if (GET.equals(requestMethod) && parameterMap != null
                    && parameterMap.size() > 0) {
                tempUrl += toString(parameterMap);
            }
            conn = (HttpURLConnection) (new URL(tempUrl)).openConnection();
        }
        return conn;
    }

    /**
     * list 格式化成字符串
     * 
     * @param list
     * @return
     */
    private String toString(List<String> list) {
        if (list != null && list.size() > 0) {
            StringBuilder str = new StringBuilder();
            for (String s : list) {
                str.append(s).append("; ");
            }
            str.delete(str.length() - 2, str.length());
            return str.toString();
        }
        return "";
    }

    /**
     * map 格式化成字符串
     * 
     * @param map
     * @return
     */
    private String toString(Map<String, List<String>> map) {
        if (map != null) {
            StringBuilder str = new StringBuilder();

            Iterator<String> keys = map.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    if (key != null && !key.equals("")) {
                        str.append(key)
                                .append("=")
                                .append(URLEncoder.encode(
                                        toString(map.get(key)), encode))
                                .append("&");
                    } else {
                        str.append(toString(map.get(key))).append("&");
                    }
                } catch (UnsupportedEncodingException e) {
                    str.append(key).append("=").append(toString(map.get(key)))
                            .append("&");
                }
            }
            str.deleteCharAt(str.length() - 1);
            return str.toString();
        }
        return "";
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static String encoder(String str) {
        if (!isNull(str)) {
            try {
                str = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return str;
    }

    public static boolean isNull(String str) {
        return str == null || "".equals(str.trim());
    }

}
