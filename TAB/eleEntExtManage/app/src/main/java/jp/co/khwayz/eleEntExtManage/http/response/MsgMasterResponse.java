package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * メッセージマスタ取得Response
 */
public class MsgMasterResponse {
    @SerializedName("status")
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @SerializedName("errorCode")
    private String errorCode;
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    @SerializedName("data")
    private ResultData data;
    public ResultData getData() {
        return data;
    }
    public void setData(ResultData data) {
        this.data = data;
    }

    public static class ResultData {
        @SerializedName("messageList")
        private ArrayList<ResponseBody> list;
        public ArrayList<ResponseBody> getList() {
            return list;
        }
        public void setList(ArrayList<ResponseBody> list) {
            this.list = list;
        }
    }

    public static class ResponseBody {
        @SerializedName("num")
        public String num;
        @SerializedName("messageKubun")
        public String messageKbn;
        @SerializedName("message")
        public String message;
        public String getNum() {
            return num;
        }
        public void setNum(String num) {
            this.num = num;
        }
        public String getMessageKbn() {
            return messageKbn;
        }
        public void setMessageKbn(String messageKbn) {
            this.messageKbn = messageKbn;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
