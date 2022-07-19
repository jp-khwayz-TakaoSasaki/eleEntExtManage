package jp.co.khwayz.eleEntExtManage.http.task;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class HttpTaskBase<T> {
    protected final String TAG = this.getClass().getSimpleName();
    protected boolean mShowProgress = true;
    protected final Callback<T> mCallback;

    /**
     * POST戻り値
     */
    protected static class ExecuteResponse {
        /** Httpステータスコード */
        private int httpStatusCode;
        public int getHttpStatusCode() {
            return httpStatusCode;
        }
        public void setHttpStatusCode(int httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
        }
        /** エラーメッセージ */
        private int errorMsgId;
        public int getErrorMsgId() {
            return errorMsgId;
        }
        public void setErrorMsgId(int errorMsgId) {
            this.errorMsgId = errorMsgId;
        }
        /** 取得したJSON */
        private String responseJson = null;
        public String getResponseJson() {
            return responseJson;
        }
        public void setResponseJson(String responseJson) {
            this.responseJson = responseJson;
        }
        /** エラー判定 */
        boolean errorFlag = false;
        public boolean isError() {
            return errorFlag;
        }
        public void setErrorFlag(boolean errorFlag) {
            this.errorFlag = errorFlag;
        }
    }

    private class AsyncRunnable implements Runnable {
        final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void run() {
            try {
                // メイン処理
                T result = doInBackground();
                // 事後処理
                handler.post(() -> onPostExecute(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface Callback<T> {
        void onPreExecute(boolean showProgress);
        void onTaskFinished(T response);
        void onError(int httpResponseStatusCode, int messageId);
    }

    public HttpTaskBase(@NonNull Callback<T> callback) {
        mCallback = callback;
    }

    /**
     * ProgressDialogを表示するフラグ
     * @param showProgress : true -> ProgressDialogを表示する
     */
    public HttpTaskBase<T> setShowProgress(boolean showProgress) {
        mShowProgress = showProgress;
        return this;
    }

    public void execute() {
        // 事前処理
        mCallback.onPreExecute(mShowProgress);
        // thread開始
        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.submit(new AsyncRunnable());
    }

    protected abstract T doInBackground();

    protected abstract void onPostExecute(T response);

    /**
     * onError callbackを起動する
     * @param httpStatusCode : Http status code
     * @param msgId : エラーメッセージID
     */
    protected void raiseOnError(int httpStatusCode, int msgId) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> mCallback.onError(httpStatusCode, msgId));
    }

    /**
     * Http Post実行
     * @param url : APIのURL
     * @param jsonParam : JSONパラメータ
     * @return 取得したJSON
     */
    protected ExecuteResponse httpPostExecute(String url, String jsonParam, boolean addAuthToken) {
        String responseBody = null;
        ExecuteResponse result = new ExecuteResponse();

        /* リクエストを生成 */
        RequestBody requestBody;
        Request request;
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        // JSONパラメータ有り
        if (jsonParam != null) {
            requestBody = RequestBody.create(Constants.MEDIA_TYPE_JSON, jsonParam);
            builder.post(requestBody);
        }
        // 認証トークン必要な場合
        if (addAuthToken) {
            builder.addHeader("Authorization", Application.userInfo.getAuthToken());
        }
        request = builder.build();

        /* Httpクライアント生成 */
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                .build();
        Response response = null;

        try {
            //HTTPリクエスト
            response = client.newCall(request).execute();
            if (response.body() != null){
                responseBody = response.body().string();
            }
            // 戻り値にセット
            result.setResponseJson(responseBody);
            Application.log.d(TAG,"Response Json:" + responseBody);

            // httpステータスコード取得
            int responseCode = response.code();
            // 戻り値にセット
            result.setHttpStatusCode(responseCode);
            Application.log.d(TAG, "HTTP_RESPONSE_STATUS_CODE：" + responseCode);

            // httpエラー
            if (responseCode != Constants.HTTP_RESPONSE_STATUS_CODE_OK) {
                // エラーを返却する
                result.setErrorFlag(true);
                switch (responseCode) {
                    case Constants.HTTP_RESPONSE_STATUS_CODE_NOT_FOUND:
                        // 404(NOT FOUND)
                        result.setErrorMsgId(R.string.err_message_E9003);
                        break;
                    case Constants.HTTP_RESPONSE_STATUS_CODE_INTERNAL_SERVER_ERROR:
                        // server error
                        result.setErrorMsgId(R.string.err_message_E9001);
                        break;
                    default:
                        // server communication error
                        result.setErrorMsgId(R.string.err_message_E9004);
                        break;
                }
            }
            return result;
        } catch (SocketTimeoutException e){
            e.printStackTrace();
            Application.log.e(TAG, e);
            // 408(Request Timeout)
            result.setErrorFlag(true);
            result.setHttpStatusCode(Constants.HTTP_RESPONSE_STATUS_CODE_REQUEST_TIMEOUT);
            result.setErrorMsgId(R.string.err_message_E9002);
            return result;
        } catch (final IOException | JsonSyntaxException e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            result.setErrorFlag(true);
            result.setHttpStatusCode(Constants.HTTP_OTHER_ERROR);
            result.setErrorMsgId(R.string.err_message_E9000);
            return result;
        } finally {
            if(response != null && response.body() != null) {
                response.body().close();
            }
        }
    }
}
