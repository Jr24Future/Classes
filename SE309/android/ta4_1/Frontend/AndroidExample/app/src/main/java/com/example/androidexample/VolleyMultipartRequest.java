package com.example.androidexample;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private Map<String, DataPart> mDataPartParams;

    public VolleyMultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        mParams = new HashMap<>();
        mDataPartParams = new HashMap<>();
        mHeaders = new HashMap<>();
    }

    public void addFormField(String key, String value) {
        mParams.put(key, value);
    }

    public void addFilePart(String parameterName, DataPart dataPart) {
        mDataPartParams.put(parameterName, dataPart);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Log each part being built
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                Log.d("MultipartRequest", "Adding text field: " + entry.getKey());
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }

            // Ensure that getByteData() is properly integrated
            Map<String, DataPart> dataParts = getByteData();
            for (Map.Entry<String, DataPart> entry : dataParts.entrySet()) {
                Log.d("MultipartRequest", "Adding file field: " + entry.getKey());
                buildDataPart(dos, entry.getKey(), entry.getValue());
            }

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Log the request body for debugging
            byte[] bytes = bos.toByteArray();
            Log.d("MultipartRequest", "Complete Request Body: " + new String(bytes));
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private void buildDataPart(DataOutputStream dos, String parameterName, DataPart dataPart) throws IOException {
        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"; filename=\"" + dataPart.getFileName() + "\"" + lineEnd);
        dos.writeBytes("Content-Type: " + dataPart.getType() + lineEnd);
        dos.writeBytes(lineEnd);

        byte[] fileData = dataPart.getContent();
        if (fileData != null && fileData.length > 0) {
            dos.write(fileData);
            dos.writeBytes(lineEnd);
        } else {
            Log.d("MultipartRequest", "No data for " + parameterName);
        }
    }

    protected abstract Map<String, DataPart> getByteData() throws AuthFailureError;

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }


    }
}
