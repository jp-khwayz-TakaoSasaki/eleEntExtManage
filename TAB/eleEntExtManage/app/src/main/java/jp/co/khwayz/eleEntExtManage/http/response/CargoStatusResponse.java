package jp.co.khwayz.eleEntExtManage.http.response;

import com.google.gson.annotations.SerializedName;

import jp.co.khwayz.eleEntExtManage.cargostatus.CargoStatusInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoStatusResponse {
    /** ステータス */
    @SerializedName("status")
    private String status;
    /** エラーコード */
    @SerializedName("errorCode")
    private String errorCode;
    /** エラー詳細 */
    @SerializedName("errorDetail")
    private String errorDetail;
    /** 結果リスト */
    @SerializedName("data")
    private CargoStatusInfo data;
}
