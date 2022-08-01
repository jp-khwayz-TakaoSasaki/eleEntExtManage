package jp.co.khwayz.eleEntExtManage.instr_cfm;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingInstructionsInfo {
    /* No */
    @SerializedName("lineNo")
    private String no;
    /* 受注番号 */
    @SerializedName("jyuchuNo")
    private String orderNo;
    /* 出荷指示欄 */
    @SerializedName("ushotSyukkaSiji")
    private String shippingInstructions;
    /* コンストラクタ */
    public ShippingInstructionsInfo() { }
}
