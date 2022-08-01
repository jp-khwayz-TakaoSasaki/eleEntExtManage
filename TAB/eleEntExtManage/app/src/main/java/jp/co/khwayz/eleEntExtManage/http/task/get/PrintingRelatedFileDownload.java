package jp.co.khwayz.eleEntExtManage.http.task.get;

import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import jp.co.khwayz.eleEntExtManage.R;
import jp.co.khwayz.eleEntExtManage.application.Application;
import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.http.task.HttpTaskBase;
import jp.co.khwayz.eleEntExtManage.instr_cfm.InvoiceTempInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrintingRelatedFileDownload extends HttpTaskBase<String> {
    private InvoiceTempInfo mInvoiceTempInfo;
    private String mInvoiceNo;

    public PrintingRelatedFileDownload(@NonNull Callback<String> callback, String invoiceNo, InvoiceTempInfo info) {
        super(callback);
        mInvoiceNo = invoiceNo;
        mInvoiceTempInfo = info;
    }

    @Override
    protected String doInBackground() {
        RequestBody requestBody;
        Response response = null;
        try {
            // JSONパラメータ作成
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("invoiceNo", mInvoiceNo);
            jsonObject.addProperty("tenpNo", mInvoiceTempInfo.getTempNo());

            // URL
            String url = Application.apiUrl + Constants.HTTP_SERVICE_NAME + "/syukko/entry/download/";
            /* リクエストを生成 */
            Request request;
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            // JSONパラメータ有り
            requestBody = RequestBody.create(Constants.MEDIA_TYPE_JSON, jsonObject.toString());
            builder.post(requestBody);
            // 認証トークン必要
            builder.addHeader("X-elematec-auth", Application.userInfo.getAuthToken());
            request = builder.build();

            /* Httpクライアント生成 */
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                    .writeTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                    .readTimeout(Constants.TIMEOUT_SEC, TimeUnit.SECONDS)
                    .build();

            //HTTPリクエスト
            response = client.newCall(request).execute();
            // httpステータスコード取得
            int responseCode = response.code();
            Application.log.d(TAG, "HTTP_RESPONSE_STATUS_CODE：" + responseCode);

            // httpエラー
            if (responseCode != Constants.HTTP_RESPONSE_STATUS_CODE_OK) {
                // エラーを返却する
                switch (responseCode) {
                    case Constants.HTTP_RESPONSE_STATUS_CODE_NOT_FOUND:
                        // 404(NOT FOUND)
                        raiseOnError(Constants.HTTP_RESPONSE_STATUS_CODE_NOT_FOUND, R.string.err_message_E9003);
                        return null;
                    case Constants.HTTP_RESPONSE_STATUS_CODE_INTERNAL_SERVER_ERROR:
                        // server error
                        raiseOnError(Constants.HTTP_RESPONSE_STATUS_CODE_INTERNAL_SERVER_ERROR, R.string.err_message_E9001);
                        return null;
                    default:
                        // server communication error
                        raiseOnError(responseCode, R.string.err_message_E9004);
                        return null;
                }
            }

            // Downloadフォルダに「アプリ名\InvoiceNoフォルダを作成する
            String downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/eleEntExtManage/" + mInvoiceNo;
            File dir = new File(downloadDir);
            if (!dir.exists()) { dir.mkdirs(); }
            // ファイル作成
            File file = new File(dir, mInvoiceTempInfo.getFileName());
            if (file.exists()) { file.delete(); }
            // responseからInputStreamを取得
            InputStream is = response.body().byteStream();
            // ファイル作成
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 FileOutputStream fos = new FileOutputStream(file)){
                byte[] buf = new byte[1024];
                int current = 0;
                while ((current = bis.read(buf)) != -1) {
                    fos.write(buf,0, current);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Application.log.e(TAG, e);
                // -1(その他エラー)
                raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
                return null;
            }
            // ファイル名を返却して終了
            return file.getAbsolutePath();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // 408(Request Timeout)
            raiseOnError(Constants.HTTP_RESPONSE_STATUS_CODE_REQUEST_TIMEOUT, R.string.err_message_E9002);
            return null;
        } catch (final IOException | JsonSyntaxException e) {
            e.printStackTrace();
            Application.log.e(TAG, e);
            // -1(その他エラー)
            raiseOnError(Constants.HTTP_OTHER_ERROR, R.string.err_message_E9000);
            return null;
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }

    @Override
    protected void onPostExecute(String response) {
        // エラー終了の場合
        if (response == null) { return; }
        // 処理終了通知
        mCallback.onTaskFinished(response);
    }
}
