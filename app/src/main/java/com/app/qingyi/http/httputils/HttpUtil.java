package com.app.qingyi.http.httputils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.utils.InternetUtil;
import com.app.qingyi.utils.SystemLog;
import com.app.qingyi.utils.Utils;

//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author cjq
 * @ClassName: HttpUtil
 */
public class HttpUtil {

    /**
     * 判断网络连接是否打开,包括移动数据连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                if (InternetUtil.getNetworkState(context).equals("None")) {
                    SystemLog.Log("网络未连接");
                } else {
                    SystemLog.Log("当前网络:" + InternetUtil.getNetworkState(context));
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static X509Certificate getX509Certificate(Context context) throws IOException, CertificateException {
        InputStream in = context.getAssets().open("mobipromo22.cer");
        CertificateFactory instance = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) instance.generateCertificate(in);
        return certificate;
    }

    public static String uploadData(BaseRequestParm parm) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(parm.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20 * 1000);
            conn.setReadTimeout(20 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setUseCaches(true);
            if (parm.getAuthorization() != null) {
                conn.setRequestProperty("Authorization", parm.getAuthorization());// 认证
            }
            conn.setRequestProperty("connection", "keep-alive"); // 客户端到服务器端的连接持续有效
            conn.setRequestProperty("Content-Type", parm.getContentType());
            conn.setRequestProperty("User-Agent", "CAN-Wallet-" + Utils.getVerName(parm.getContext()));
            conn.setRequestProperty("apiversion", Utils.getVerCode(parm.getContext()) + "android");
            conn.setRequestProperty("Cookie", parm.getmLoinConfig().getCookie());

            if (parm.getRequest().equals("GET")) {
                // GET方式
                // httpurlconnection.setDoOutput(true); 这一句不要
                // httpurlconnection.setRequestMethod("GET"); 这一句不要，缺省就是get
            } else if (parm.getRequest().equals("DELETE")) {
                conn.setRequestMethod(parm.getRequest());
            } else if (parm.getRequest().equals("POST") || parm.getRequest().equals("PUT")) {
                // Post方式
                conn.setRequestMethod(parm.getRequest()); // Post方式
                conn.setDoOutput(true);// 允许输出

                OutputStream outputStream = conn.getOutputStream();
                byte[] bytes;
                bytes = parm.getStringJsonData().getBytes();
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
            SystemLog.Log("code = " + parm.getUrl());
            SystemLog.Log("code = " + parm.getJsonData());
            SystemLog.Log("code = " + conn.getResponseCode());
            conn.connect();
            if (conn.getResponseCode() == 200) {
                List<String> cookies = conn.getHeaderFields().get("set-cookie");
                if (cookies != null) {
                    String sessionId = cookies.get(0).split(";")[0];
                    parm.getmLoinConfig().setCookie(sessionId);
                }
                InputStream in = conn.getInputStream();
                byte[] inputBbytes = InputStreamTOByte(in);
                String results;
                results = new String(inputBbytes, "utf-8");
                return results;
            } else if (conn.getResponseCode() == 401) {
                // return "用户认证失败";
                return "401";
            } else if (conn.getResponseCode() == 403) {
                // return "用户授权失败";
                return null;
            } else if (conn.getResponseCode() == 404) {
                // return "请求地址错误";
                return null;
            } else if (conn.getResponseCode() == 405) {
                // return "请求方法错误";
                return null;
            } else if (conn.getResponseCode() == 400) {
                // return "请求数据格式错误";
                InputStream in = conn.getErrorStream();

                byte[] inputBbytes = InputStreamTOByte(in);
                String results;

                results = new String(inputBbytes, "utf-8");

                BufferedReader bf = new BufferedReader(new InputStreamReader(in));

                return "code400" + results;
            } else if (conn.getResponseCode() == 500) {
                // return "系统内部错误";
                return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            SystemLog.Log(e.toString());
            return null;
        }
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024 * 4];
        int count = -1;
        while ((count = in.read(data, 0, 1024 * 4)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    // 从网络获取Bitmap
    public static Bitmap getHttpBitmap(BaseRequestParm parm) {
        String url = parm.getUrl();
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            List<String> cookies = conn.getHeaderFields().get("set-cookie");
            if (cookies != null) {
                String sessionId = cookies.get(0).split(";")[0];
                parm.getmLoinConfig().setCookie(sessionId);
            }
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase(Locale.CHINESE));
        }

        if (type == null || type.equals("")) {
            return "application/octet-stream";
        }
        return type;
    }

    public static String uploadFile(final BaseRequestParm parm,final RequestListener<BaseResponseBean> listener) {
        try {
            final OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("Authorization", parm.getAuthorization())
                                    .addHeader("apiversion", Utils.getVerCode(parm.getContext()) + "android")
                                    .addHeader("User-Agent", "CAN-Wallet-" + Utils.getVerName(parm.getContext()))
                                    .build();
                            return chain.proceed(request);
                        }

                    })
                    .build();
            File file = new File(parm.getFilePath());
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            MediaType MEDIA_TYPE_PNG = MediaType.parse(getMimeType(parm.getFilePath()));

            builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

            MultipartBody requestBody = builder.build();
            //构建请求
            final Request request = new Request.Builder()
                    .url(parm.getUrl())//地址
                    .post(requestBody)//添加请求体
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
                    listener.onFailed();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String back = response.body().string();
                    listener.onComplete(new BaseResponseBean(back));
                }
            });

        } catch (Exception e) {
            return null;
        }
        return null;
    }
}