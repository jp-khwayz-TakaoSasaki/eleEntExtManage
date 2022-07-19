package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.common.models.OuterInfo;
import jp.co.khwayz.eleEntExtManage.common.models.OverPackKonpoShizaiInfo;

/**
 * オーバーパック梱包資材情報受信Response
 */
public class OverPackPackingMaterialInfoGetResponse {
    // ステータス
    @SerializedName("status")
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    // エラーコード
    @SerializedName("errorCode")
    private String errorCode;
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    // 結果リスト
    @SerializedName("data")
    private ResultData data;
    public ResultData getData() {
        return data;
    }
    public void setData(ResultData data) {
        this.data = data;
    }

    public static class ResultData {
        @SerializedName("packingMaterialList")
        private ArrayList<OverPackKonpoShizaiInfo> list;
        public ArrayList<OverPackKonpoShizaiInfo> getList() {
            return list;
        }
        public void setList(ArrayList<OverPackKonpoShizaiInfo> list) {
            this.list = list;
        }
    }
}
