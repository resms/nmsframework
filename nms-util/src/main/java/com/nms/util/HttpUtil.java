package com.nms.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sam on 15-9-2.
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public void GET(String url)
    {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String resultEchoStr = EntityUtils.toString(responseEntity);

                    logger.info(resultEchoStr);
                } else {
                    logger.info(response.getStatusLine().toString());
                }

            }  finally {
                response.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void POST(String syncUrl,Map<String,String> parameters)
    {
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(syncUrl);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            for (Map.Entry<String,String> entry : parameters.entrySet())
            {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity syncResponseEntity = response.getEntity();
                    // do something useful with the response body
                    // and ensure it is fully consumed

                    String resultEchoStr = EntityUtils.toString(syncResponseEntity);
                    logger.info(resultEchoStr);
                    if ("OK".equals(resultEchoStr)) {
                        //TODO
                    }
                }
                else
                {
                    logger.info("code:" + response.getStatusLine().getStatusCode() + ",phrase:" + response.getStatusLine().getReasonPhrase());
                }
            } finally {
                response.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
