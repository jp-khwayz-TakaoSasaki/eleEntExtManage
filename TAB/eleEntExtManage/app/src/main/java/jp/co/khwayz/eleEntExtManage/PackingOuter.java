package jp.co.khwayz.eleEntExtManage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackingOuter {
    /* 表記用ケースマーク番号 */
    private String notationCno;
    /* 最終梱包荷姿 */
    private String finalPackingAppearance;
    /* L */
    private String length;
    /* W */
    private String width;
    /* H */
    private String height;
    /* 重量 */
    private String weight;
    /* インナーパッケージ */
    private int innerPackage;


    public PackingOuter(String pNotationCno, String pFinalPackingAppearance,
                        String pLength, String pWidth, String pHeight, String pWeight, int pInnerPackage) {
        notationCno = pNotationCno;
        finalPackingAppearance = pFinalPackingAppearance;
        length = pLength;
        width = pWidth;
        height = pHeight;
        weight = pWeight;
        innerPackage = pInnerPackage;
    }
}
