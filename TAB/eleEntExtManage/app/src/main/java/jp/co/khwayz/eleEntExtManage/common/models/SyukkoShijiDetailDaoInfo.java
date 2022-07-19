package jp.co.khwayz.eleEntExtManage.common.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyukkoShijiDetailDaoInfo {
    private String invoiceNo;
    private int renban;
    private int lineNo;
    private String juchuNo;
    private String hachuNo;
    private int branchNo;
    private String customerHachuNo;
    private String hinmokuCode;
    private String hinmokuName;
    private double misyukkaSu;
    private double syukkaSuSam;
    private double syukkaSu;
    private String syukkaTani;
    private String tanaban;
    private String zaikohinFlag;
    private String pickzumiFlag;
    private String konpozumiFlag;
    private int overpackNo;
    private int csNumber;
    private String nyukoNisugata;
    private int combineNo;
    private String nyukoDate;
    private String konpoSiji;
    private String basicKonpoHoho;
    private String biko;
    private String horeizai;
    private String updateFlag;
    private String singlePackFlag;
}
