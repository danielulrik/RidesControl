package com.ulrik.rides.service;

import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Leonardo Neves
 * Date: 27/03/2015
 * Time: 15:09
 */
public class HttpClientRest extends AsyncTask<String, String, InputStream> {

    private final ProgressDialog progressDialog;
    private final OnFinish listener;
    private Exception error;
    private AndroidHttpClient client;

    private HttpClientRest(ProgressDialog progressDialog, OnFinish listener) {
        this.progressDialog = progressDialog;
        this.listener = listener;
    }

    public static void doGet(String url, ProgressDialog progressDialog, OnFinish listener) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.GET.name());
    }

    public static void doPost(String url, ProgressDialog progressDialog, OnFinish listener) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.POST.name());
    }

    public static void doPost(String url, ProgressDialog progressDialog, OnFinish listener, String content) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.POST.name(), content);
    }

    public static void doPost(String url, ProgressDialog progressDialog, OnFinish listener, String content, String contentCharset) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.POST.name(), content, contentCharset);
    }

    public static void doPost(String url, ProgressDialog progressDialog, OnFinish listener, String content, String contentCharset, String contentType) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.POST.name(), content, contentCharset, contentType);
    }

    public static void doPut(String url, ProgressDialog progressDialog, OnFinish listener) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.PUT.name());
    }

    public static void doPut(String url, ProgressDialog progressDialog, OnFinish listener, String content) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.PUT.name(), content);
    }

    public static void doPut(String url, ProgressDialog progressDialog, OnFinish listener, String content, String contentCharset) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.PUT.name(), content, contentCharset);
    }

    public static void doPut(String url, ProgressDialog progressDialog, OnFinish listener, String content, String contentCharset, String contentType) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.PUT.name(), content, contentCharset, contentType);
    }

    public static void doDelete(String url, ProgressDialog progressDialog, OnFinish listener) {
        HttpClientRest clientRest = new HttpClientRest(progressDialog, listener);
        clientRest.execute(url, HTTP_METHOD.DELETE.name());

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    protected HttpRequest makeRequest(String... params) throws UnsupportedEncodingException {
        HTTP_METHOD method = HTTP_METHOD.valueOf(params[1]);
        HttpRequest request = null;
        switch (method) {
            case GET:
                request = new HttpGet(params[0]);
                break;
            case DELETE:
                request = new HttpDelete(params[0]);
                break;
            case POST:
                HttpPost postReq = new HttpPost(params[0]);
                if (params.length > 2) {
                    StringEntity entity = new StringEntity(params[2], (params.length > 3 ? params[3] : "UTF-8"));
                    postReq.setEntity(entity);
                }
                if (params.length > 4) {
                    postReq.addHeader("Content-type", params[4]);
                } else {
                    postReq.addHeader("Content-type", "application/json");
                }
                request = postReq;
                break;
            case PUT:
                HttpPut putReq = new HttpPut(params[0]);
                if (params.length > 2) {
                    StringEntity entity = new StringEntity(params[2], (params.length > 3 ? params[3] : "UTF-8"));
                    putReq.setEntity(entity);
                    if (params.length > 4) {
                        putReq.addHeader("Content-type", params[4]);
                    } else {
                        putReq.addHeader("Content-type", "application/json");
                    }
                }
                request = putReq;
                break;
        }
        request.addHeader("Accept", "application/json, text/plain, application/xml");
        return request;
    }


    @Override
    protected InputStream doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpHost host = new HttpHost(url.getHost(), url.getPort());
            client = AndroidHttpClient.newInstance(getClass().getName());
            HttpRequest request = makeRequest(params);
            HttpResponse response = client.execute(host, request);
            InputStream is = null;
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 299) {
                if (response.getEntity() != null) {
                    is = response.getEntity().getContent();
                }
            } else {
                throw new ResponseException(error);
            }
            if (listener != null) {
                listener.onGetResponse(is);
            }
        } catch (Exception e) {
            error = e;
            if (listener != null) {
                listener.onGetError(error);
            }
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(InputStream s) {
        super.onPostExecute(s);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public enum HTTP_METHOD {GET, POST, PUT, DELETE}

    public interface OnFinish {
        void onGetResponse(InputStream response);
        void onGetError(Exception erro);
    }
}
