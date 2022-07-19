package jp.co.khwayz.eleEntExtManage.http.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 梱包情報登録Request
 */
public class PackingDataRegistRequest {
    // Invoice番号
    @SerializedName("invoiceNo")
    private String invoiceNo;
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    // 梱包保留フラグ
    @SerializedName("konpoHoldFlag")
    private String konpoHoldFlag;
    public String getKonpoHoldFlag() {
        return konpoHoldFlag;
    }
    public void setKonpoHoldFlag(String konpoHoldFlag) {
        this.konpoHoldFlag = konpoHoldFlag;
    }
    // オーバーパック梱包資材リスト
    @SerializedName("overPackPackingMaterialList")
    private ArrayList<OverPackRequestDetail> overPacklist = new ArrayList<>();
    public ArrayList<OverPackRequestDetail> getOverPacklist() {
            return overPacklist;
        }
    public void setOverPacklist(ArrayList<OverPackRequestDetail> overPacklist) {
            this.overPacklist = overPacklist;
        }
    // 梱包アウターリスト
    @SerializedName("outerList")
    private ArrayList<OuterRequestDetail> outerList = new ArrayList<>();
    public ArrayList<OuterRequestDetail> getOuterList() {
        return outerList;
    }
    public void setOuterList(ArrayList<OuterRequestDetail> outerList) {
        this.outerList = outerList;
    }
    // 梱包インナーリスト
    @SerializedName("innerList")
    private ArrayList<InnerRequestDetail> innerList = new ArrayList<>();
    public ArrayList<InnerRequestDetail> getInnerList() {
        return innerList;
    }
    public void setInnerList(ArrayList<InnerRequestDetail> innerList) {
        this.innerList = innerList;
    }
    // 出庫指示明細リスト
    @SerializedName("syukkoDetailList")
    private ArrayList<SyukkoDetailRequestDetail> syukkoList = new ArrayList<>();
    public ArrayList<SyukkoDetailRequestDetail> getSyukkoList() {
        return syukkoList;
    }
    public void setSyukkoList(ArrayList<SyukkoDetailRequestDetail> syukkoList) {
        this.syukkoList = syukkoList;
    }

    // オーバーパックリスト明細
    public static class OverPackRequestDetail {
        /** オーバーパック番号 */
        @SerializedName("overPackNo")
        private int overPackNo;
        public int getOverPackNo() {
            return overPackNo;
        }
        public void setOverPackNo(int overPackNo) {
            this.overPackNo = overPackNo;
        }
        /** 梱包資材番号 */
        @SerializedName("packingMaterialNo")
        private int packingMaterialNo;
        public int getPackingMaterialNo() {
            return packingMaterialNo;
        }
        public void setPackingMaterialNo(int packingMaterialNo) {
            this.packingMaterialNo = packingMaterialNo;
        }
        /** 梱包資材 */
        @SerializedName("packingMaterial")
        private String packingMaterial;
        public String getPackingMaterial() {
            return packingMaterial;
        }
        public void setPackingMaterial(String packingMaterial) {
            this.packingMaterial = packingMaterial;
        }
    }

    // アウターリスト明細
    public static class OuterRequestDetail {
        /**
         * ケースマーク番号
         */
        @SerializedName("csNumber")
        private int csNumber;

        public int getCsNumber() {
            return csNumber;
        }

        public void setCsNumber(int csNumber) {
            this.csNumber = csNumber;
        }

        /**
         * 表記用ケースマーク番号
         */
        @SerializedName("hyokiCsNumber")
        private String hyokiCsNumber;

        public String getHyokiCsNumber() {
            return hyokiCsNumber;
        }

        public void setHyokiCsNumber(String hyokiCsNumber) {
            this.hyokiCsNumber = hyokiCsNumber;
        }

        /**
         * アウター作業内容１
         */
        @SerializedName("outerSagyo1")
        private String outerSagyo1;

        public String getOuterSagyo1() {
            return outerSagyo1;
        }

        public void setOuterSagyo1(String outerSagyo1) {
            this.outerSagyo1 = outerSagyo1;
        }

        /**
         * アウター作業内容１使用量
         */
        @SerializedName("outerSagyo1Siyo")
        private double outerSagyo1Siyo;

        public double getOuterSagyo1Siyo() {
            return outerSagyo1Siyo;
        }

        public void setOuterSagyo1Siyo(double outerSagyo1Siyo) {
            this.outerSagyo1Siyo = outerSagyo1Siyo;
        }


        /**
         * アウター作業内容２
         */
        @SerializedName("outerSagyo2")
        private String outerSagyo2;

        public String getOuterSagyo2() {
            return outerSagyo2;
        }

        public void setOuterSagyo2(String outerSagyo2) {
            this.outerSagyo2 = outerSagyo2;
        }

        /**
         * アウター作業内容２使用量
         */
        @SerializedName("outerSagyo2Siyo")
        private double outerSagyo2Siyo;

        public double getOuterSagyo2Siyo() {
            return outerSagyo2Siyo;
        }

        public void setOuterSagyo2Siyo(double outerSagyo2Siyo) {
            this.outerSagyo2Siyo = outerSagyo2Siyo;
        }

        /**
         * アウター作業内容３
         */
        @SerializedName("outerSagyo3")
        private String outerSagyo3;

        public String getOuterSagyo3() {
            return outerSagyo3;
        }

        public void setOuterSagyo3(String outerSagyo3) {
            this.outerSagyo3 = outerSagyo3;
        }

        /**
         * アウター作業内容３使用量
         */
        @SerializedName("outerSagyo3Siyo")
        private double outerSagyo3Siyo;

        public double getOuterSagyo3Siyo() {
            return outerSagyo3Siyo;
        }

        public void setOuterSagyo3Siyo(double outerSagyo3Siyo) {
            this.outerSagyo3Siyo = outerSagyo3Siyo;
        }

        /**
         * アウター作業内容４
         */
        @SerializedName("outerSagyo4")
        private String outerSagyo4;

        public String getOuterSagyo4() {
            return outerSagyo4;
        }

        public void setOuterSagyo4(String outerSagyo4) {
            this.outerSagyo4 = outerSagyo4;
        }

        /**
         * アウター作業内容４使用量
         */
        @SerializedName("outerSagyo4Siyo")
        private double outerSagyo4Siyo;

        public double getOuterSagyo4Siyo() {
            return outerSagyo4Siyo;
        }

        public void setOuterSagyo4Siyo(double outerSagyo4Siyo) {
            this.outerSagyo4Siyo = outerSagyo4Siyo;
        }

        /**
         * ブルーアイス使用量
         */
        @SerializedName("blueiceSiyo")
        private double blueiceSiyo;

        public double getBlueiceSiyo() {
            return blueiceSiyo;
        }

        public void setBlueiceSiyo(double blueiceSiyo) {
            this.blueiceSiyo = blueiceSiyo;
        }

        /**
         * ドライアイス使用量
         */
        @SerializedName("dryiceSiyo")
        private double dryiceSiyo;

        public double getDryiceSiyo() {
            return dryiceSiyo;
        }

        public void setDryiceSiyo(double dryiceSiyo) {
            this.dryiceSiyo = dryiceSiyo;
        }

        /**
         * ラベル枚数
         */
        @SerializedName("labelSu")
        private int labelSu;

        public int getLabelSu() {
            return labelSu;
        }

        public void setLabelSu(int labelSu) {
            this.labelSu = labelSu;
        }

        /**
         * 梱包数
         */
        @SerializedName("konpoSu")
        private int konpoSu;

        public int getKonpoSu() {
            return konpoSu;
        }

        public void setKonpoSu(int konpoSu) {
            this.konpoSu = konpoSu;
        }

        /**
         * アウター長さ(L)
         */
        @SerializedName("outerLength")
        private double outerLength;

        public double getOuterLength() {
            return outerLength;
        }

        public void setOuterLength(double outerLength) {
            this.outerLength = outerLength;
        }

        /**
         * アウター幅(W)
         */
        @SerializedName("outerWidth")
        private double outerWidth;

        public double getOuterWidth() {
            return outerWidth;
        }

        public void setOuterWidth(double outerWidth) {
            this.outerWidth = outerWidth;
        }

        /**
         * アウター高さ(H)
         */
        @SerializedName("outerHeight")
        private double outerHeight;

        public double getOuterHeight() {
            return outerHeight;
        }

        public void setOuterHeight(double outerHeight) {
            this.outerHeight = outerHeight;
        }

        /**
         * NW(net weight)
         */
        @SerializedName("netWeight")
        private double netWeight;

        public double getNetWeight() {
            return netWeight;
        }

        public void setNetWeight(double netWeight) {
            this.netWeight = netWeight;
        }

        /**
         * GW(gross weight)
         */
        @SerializedName("grossWeight")
        private double grossWeight;

        public double getGrossWeight() {
            return grossWeight;
        }

        public void setGrossWeight(double grossWeight) {
            this.grossWeight = grossWeight;
        }

        /**
         * 最終梱包荷姿
         */
        @SerializedName("saisyuKonpoNisugata")
        private String saisyuKonpoNisugata;

        public String getSaisyuKonpoNisugata() {
            return saisyuKonpoNisugata;
        }

        public void setSaisyuKonpoNisugata(String saisyuKonpoNisugata) {
            this.saisyuKonpoNisugata = saisyuKonpoNisugata;
        }

        /**
         * 備考
         */
        @SerializedName("biko")
        private String biko;

        public String getBiko() {
            return biko;
        }

        public void setBiko(String biko) {
            this.biko = biko;
        }

        /**
         * 箱数
         */
        @SerializedName("cartonSu")
        private int cartonSu;

        public int getCartonSu() {
            return cartonSu;
        }

        public void setCartonSu(int cartonSu) {
            this.cartonSu = cartonSu;
        }

        /**
         * 荷札数
         */
        @SerializedName("nifudaSu")
        private int nifudaSu;

        public int getNifudaSu() {
            return nifudaSu;
        }

        public void setNifudaSu(int nifudaSu) {
            this.nifudaSu = nifudaSu;
        }
    }

    // インナーリスト明細
    public static class InnerRequestDetail {
        /** 連番 */
        @SerializedName("renban")
        private int renban;
        public int getRenban() {
            return renban;
        }
        public void setRenban(int renban) {
            this.renban = renban;
        }

        /** 行番号 */
        @SerializedName("lineNo")
        private int lineNo;
        public int getLineNo() {
            return lineNo;
        }
        public void setLineNo(int lineNo) {
            this.lineNo = lineNo;
        }

        /** インナー作業内容１ */
        @SerializedName("innerSagyo1")
        private String innerSagyo1;
        public String getInnerSagyo1() {
            return innerSagyo1;
        }
        public void setInnerSagyo1(String innerSagyo1) {
            this.innerSagyo1 = innerSagyo1;
        }

        /** インナー作業内容１使用量 */
        @SerializedName("innerSagyo1Siyo")
        private double innerSagyo1Siyo;
        public double getInnerSagyo1Siyo() {
            return innerSagyo1Siyo;
        }
        public void setInnerSagyo1Siyo(double innerSagyo1Siyo) {
            this.innerSagyo1Siyo = innerSagyo1Siyo;
        }

        /** インナー作業内容２ */
        @SerializedName("innerSagyo2")
        private String innerSagyo2;
        public String getInnerSagyo2() {
            return innerSagyo2;
        }
        public void setInnerSagyo2(String innerSagyo2) {
            this.innerSagyo2 = innerSagyo2;
        }

        /** インナー作業内容２使用量 */
        @SerializedName("innerSagyo2Siyo")
        private double innerSagyo2Siyo;
        public double getInnerSagyo2Siyo() {
            return innerSagyo2Siyo;
        }
        public void setInnerSagyo2Siyo(double innerSagyo2Siyo) {
            this.innerSagyo2Siyo = innerSagyo2Siyo;
        }

        /** インナー作業内容３ */
        @SerializedName("innerSagyo3")
        private String innerSagyo3;
        public String getInnerSagyo3() {
            return innerSagyo3;
        }
        public void setInnerSagyo3(String innerSagyo3) {
            this.innerSagyo3 = innerSagyo3;
        }

        /** インナー作業内容３使用量 */
        @SerializedName("innerSagyo3Siyo")
        private double innerSagyo3Siyo;
        public double getInnerSagyo3Siyo() {
            return innerSagyo3Siyo;
        }
        public void setInnerSagyo3Siyo(double innerSagyo3Siyo) {
            this.innerSagyo3Siyo = innerSagyo3Siyo;
        }

        /** インナー作業内容４ */
        @SerializedName("innerSagyo4")
        private String innerSagyo4;
        public String getInnerSagyo4() {
            return innerSagyo4;
        }
        public void setInnerSagyo4(String innerSagyo4) {
            this.innerSagyo4 = innerSagyo4;
        }

        /** インナー作業内容４使用量 */
        @SerializedName("innerSagyo4Siyo")
        private double innerSagyo4Siyo;
        public double getInnerSagyo4Siyo() {
            return innerSagyo4Siyo;
        }
        public void setInnerSagyo4Siyo(double innerSagyo4Siyo) {
            this.innerSagyo4Siyo = innerSagyo4Siyo;
        }

        /** ラベル枚数 */
        @SerializedName("labelSu")
        private int labelSu;
        public int getLabelSu() {
            return labelSu;
        }
        public void setLabelSu(int labelSu) {
            this.labelSu = labelSu;
        }

        /** NW(net weight) */
        @SerializedName("netWeight")
        private double netWeight;
        public double getNetWeight() {
            return netWeight;
        }
        public void setNetWeight(double netWeight) {
            this.netWeight = netWeight;
        }

        /** 危険品内容量 */
        @SerializedName("danNaiyoRyo")
        private double danNaiyoRyo;
        public double getDanNaiyoRyo() {
            return danNaiyoRyo;
        }
        public void setDanNaiyoRyo(double danNaiyoRyo) {
            this.danNaiyoRyo = danNaiyoRyo;
        }

        /** 危険品単位 */
        @SerializedName("danTani")
        private String danTani;
        public String getDanTani() {
            return danTani;
        }
        public void setDanTani(String danTani) {
            this.danTani = danTani;
        }

        /** 危険品内装容器 */
        @SerializedName("danNaisoYoki")
        private String danNaisoYoki;
        public String getDanNaisoYoki() {
            return danNaisoYoki;
        }
        public void setDanNaisoYoki(String danNaisoYoki) {
            this.danNaisoYoki = danNaisoYoki;
        }

        /** 危険品本数 */
        @SerializedName("danHonsu")
        private int danHonsu;
        public int getDanHonsu() {
            return danHonsu;
        }
        public void setDanHonsu(int danHonsu) {
            this.danHonsu = danHonsu;
        }

        /** 危険品外装容器 */
        @SerializedName("danGaisoYoki")
        private String danGaisoYoki;
        public String getDanGaisoYoki() {
            return danGaisoYoki;
        }
        public void setDanGaisoYoki(String danGaisoYoki) {
            this.danGaisoYoki = danGaisoYoki;
        }

        /** 危険品外装容器個数 */
        @SerializedName("danGaisoKosu")
        private int danGaisoKosu;
        public int getDanGaisoKosu() {
            return danGaisoKosu;
        }
        public void setDanGaisoKosu(int danGaisoKosu) {
            this.danGaisoKosu = danGaisoKosu;
        }

        /** 備考 */
        @SerializedName("biko")
        private String biko;

        public String getBiko() {
            return biko;
        }
        public void setBiko(String biko) {
            this.biko = biko;
        }
    }

    // 出庫指示明細リスト
    public static class SyukkoDetailRequestDetail {
        public int getRenban() {
            return renban;
        }

        public void setRenban(int renban) {
            this.renban = renban;
        }

        public int getLineNo() {
            return lineNo;
        }

        public void setLineNo(int lineNo) {
            this.lineNo = lineNo;
        }

        public int getCsNumber() {
            return csNumber;
        }

        public void setCsNumber(int csNumber) {
            this.csNumber = csNumber;
        }

        public int getOverPackNo() {
            return overPackNo;
        }

        public void setOverPackNo(int overPackNo) {
            this.overPackNo = overPackNo;
        }

        public String getKonpozumiFlg() {
            return konpozumiFlg;
        }

        public void setKonpozumiFlg(String konpozumiFlg) {
            this.konpozumiFlg = konpozumiFlg;
        }

        /**
         * 連番
         */
        @SerializedName("renban")
        public int renban;
        /**
         * 行番号
         */
        @SerializedName("lineNo")
        public int lineNo;
        /**
         * ケースマーク番号
         */
        @SerializedName("csNumber")
        public int csNumber;
        /**
         * オーバーパック番号
         */
        @SerializedName("overPackNo")
        public int overPackNo;
        /**
         * 梱包済フラグ
         */
        @SerializedName("konpozumiFlg")
        public String konpozumiFlg;
    }
}
