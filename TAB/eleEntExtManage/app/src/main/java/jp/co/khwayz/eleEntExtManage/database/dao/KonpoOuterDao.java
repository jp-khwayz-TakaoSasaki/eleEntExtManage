package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.khwayz.eleEntExtManage.casemark_print.CaseMarkPrintInfo;
import jp.co.khwayz.eleEntExtManage.casemark_print.CaseMarkPrintStatusInfo;
import jp.co.khwayz.eleEntExtManage.common.models.CaseMarkPrintInvoiceSearchInfo;
import jp.co.khwayz.eleEntExtManage.common.models.OuterInfo;
import jp.co.khwayz.eleEntExtManage.common.models.PackingCancelInfo;

/**
 * 梱包アウターテーブルDao
 */
public class KonpoOuterDao {
    public static final String TABLE_NAME = "T_KONPO_OUTER";
    public static final String C_INVOICE_NO = "INVOICE_NO";
    public static final String C_CS_NUMBER = "CS_NUMBER";
    public static final String C_HYOKI_CS_NUMBER = "HYOKI_CS_NUMBER";
    public static final String C_CASEMARK1 = "CASEMARK1";
    public static final String C_CASEMARK2 = "CASEMARK2";
    public static final String C_CASEMARK3 = "CASEMARK3";
    public static final String C_CASEMARK4 = "CASEMARK4";
    public static final String C_CASEMARK5 = "CASEMARK5";
    public static final String C_CASEMARK6 = "CASEMARK6";
    public static final String C_CASEMARK7 = "CASEMARK7";
    public static final String C_CASEMARK8 = "CASEMARK8";
    public static final String C_CASEMARK9 = "CASEMARK9";
    public static final String C_CASEMARK10 = "CASEMARK10";
    public static final String C_CASEMARK11 = "CASEMARK11";
    public static final String C_CASEMARK12 = "CASEMARK12";
    public static final String C_OUTER_SAGYO_1 = "OUTER_SAGYO_1";
    public static final String C_OUTER_SAGYO_1_SIYO = "OUTER_SAGYO_1_SIYO";
    public static final String C_OUTER_SAGYO_2 = "OUTER_SAGYO_2";
    public static final String C_OUTER_SAGYO_2_SIYO = "OUTER_SAGYO_2_SIYO";
    public static final String C_OUTER_SAGYO_3 = "OUTER_SAGYO_3";
    public static final String C_OUTER_SAGYO_3_SIYO = "OUTER_SAGYO_3_SIYO";
    public static final String C_OUTER_SAGYO_4 = "OUTER_SAGYO_4";
    public static final String C_OUTER_SAGYO_4_SIYO = "OUTER_SAGYO_4_SIYO";
    public static final String C_BLUEICE_SIYO = "BLUEICE_SIYO";
    public static final String C_DRYICE_SIYO = "DRYICE_SIYO";
    public static final String C_LABEL_SU = "LABEL_SU";
    public static final String C_KONPO_SU = "KONPO_SU";
    public static final String C_OUTER_LENGTH = "OUTER_LENGTH";
    public static final String C_OUTER_WIDTH = "OUTER_WIDTH";
    public static final String C_OUTER_HEIGHT = "OUTER_HEIGHT";
    public static final String C_NET_WEIGHT = "NET_WEIGHT";
    public static final String C_GROSS_WEIGHT = "GROSS_WEIGHT";
    public static final String C_SAISYU_KONPO_NISUGATA = "SAISYU_KONPO_NISUGATA";
    public static final String C_BIKO = "BIKO";
    public static final String C_PALETT_UCHIWAKE = "PALETT_UCHIWAKE";
    public static final String C_CARTON_SU = "CARTON_SU";
    public static final String C_NIFUDA_SU = "NIFUDA_SU";
    public static final String C_CS_PRINT_FLG = "CS_PRINT_FLG";
    public static final String C_CS_PASTE_FLG = "CS_PASTE_FLG";
    public static final String C_SI_NO = "SI_NO";
    public static final String C_NYUKO_NISUGATA = "NYUKO_NISUGATA";
    public static final String C_N_LOCATION = "N_LOCATION";
    public static final String C_BOGAI_FLG = "BOGAI_FLG";
    public static final String C_PICZUMI_FLG = "PICZUMI_FLG";
    public static final String C_SHIMUKECHI = "SHIMUKECHI";
    public static final String C_SYUKKA_DATE = "SYUKKA_DATE";
    public static final String C_YUSO_MODE = "YUSO_MODE";
    public static final String C_REGIST_DATE = "REGIST_DATE";
    public static final String C_UPDATE_DATE = "UPDATE_DATE";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_INVOICE_NO + " TEXT NOT NULL,"
            + C_CS_NUMBER + " INTEGER NOT NULL,"
            + C_HYOKI_CS_NUMBER + " TEXT,"
            + C_CASEMARK1 + " TEXT,"
            + C_CASEMARK2 + " TEXT,"
            + C_CASEMARK3 + " TEXT,"
            + C_CASEMARK4 + " TEXT,"
            + C_CASEMARK5 + " TEXT,"
            + C_CASEMARK6 + " TEXT,"
            + C_CASEMARK7 + " TEXT,"
            + C_CASEMARK8 + " TEXT,"
            + C_CASEMARK9 + " TEXT,"
            + C_CASEMARK10 + " TEXT,"
            + C_CASEMARK11 + " TEXT,"
            + C_CASEMARK12 + " TEXT,"
            + C_OUTER_SAGYO_1 + " TEXT,"
            + C_OUTER_SAGYO_1_SIYO + " REAL,"
            + C_OUTER_SAGYO_2 + " TEXT,"
            + C_OUTER_SAGYO_2_SIYO + " REAL,"
            + C_OUTER_SAGYO_3 + " TEXT,"
            + C_OUTER_SAGYO_3_SIYO + " REAL,"
            + C_OUTER_SAGYO_4 + " TEXT,"
            + C_OUTER_SAGYO_4_SIYO + " REAL,"
            + C_BLUEICE_SIYO + " REAL,"
            + C_DRYICE_SIYO + " REAL,"
            + C_LABEL_SU + " INTEGER,"
            + C_KONPO_SU + " INTEGER,"
            + C_OUTER_LENGTH + " REAL,"
            + C_OUTER_WIDTH + " REAL,"
            + C_OUTER_HEIGHT + " REAL,"
            + C_NET_WEIGHT + " REAL,"
            + C_GROSS_WEIGHT + " REAL,"
            + C_SAISYU_KONPO_NISUGATA + " TEXT,"
            + C_BIKO + " TEXT,"
            + C_PALETT_UCHIWAKE + " TEXT,"
            + C_CARTON_SU + " INTEGER,"
            + C_NIFUDA_SU + " INTEGER,"
            + C_CS_PRINT_FLG + " TEXT,"
            + C_CS_PASTE_FLG + " TEXT,"
            + C_SI_NO + " TEXT,"
            + C_NYUKO_NISUGATA + " TEXT,"
            + C_N_LOCATION + " TEXT,"
            + C_BOGAI_FLG + " TEXT,"
            + C_PICZUMI_FLG + " TEXT,"
            + C_SHIMUKECHI + " TEXT,"
            + C_SYUKKA_DATE + " TEXT,"
            + C_YUSO_MODE + " TEXT,"
            + C_REGIST_DATE + " TEXT,"
            + C_UPDATE_DATE + " TEXT,"
            + "PRIMARY KEY("
            + C_INVOICE_NO + ","
            + C_CS_NUMBER
            + "));";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public void createTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    public void upgradeTable(SQLiteDatabase db) {
        db.execSQL(SQL_DROP);
        createTable(db);
    }

    /**
     * 梱包アウターテーブルInsert
     * @param db
     * @param list
     */
    public void bulkInsert(SQLiteDatabase db, ArrayList<OuterInfo> list) {
        try {
            // 現在日取得
            Calendar cl = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateStr = sdf.format(cl.getTime());

            // トランザクション開始
            db.beginTransaction();
            // 全件削除
            db.execSQL("delete from " + TABLE_NAME);
            /*
             * データ登録
             */
            for (OuterInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_CS_NUMBER, item.getCsNumber());
                values.put(C_HYOKI_CS_NUMBER, item.getHyokiCsNumber());
                values.put(C_OUTER_SAGYO_1, item.getOuterSagyo1());
                values.put(C_OUTER_SAGYO_1_SIYO, item.getOuterSagyo1Siyo());
                values.put(C_OUTER_SAGYO_2, item.getOuterSagyo2());
                values.put(C_OUTER_SAGYO_2_SIYO, item.getOuterSagyo2Siyo());
                values.put(C_OUTER_SAGYO_3, item.getOuterSagyo3());
                values.put(C_OUTER_SAGYO_3_SIYO, item.getOuterSagyo3Siyo());
                values.put(C_OUTER_SAGYO_4, item.getOuterSagyo4());
                values.put(C_OUTER_SAGYO_4_SIYO, item.getOuterSagyo4Siyo());
                values.put(C_BLUEICE_SIYO, item.getBlueIceSiyo());
                values.put(C_DRYICE_SIYO, item.getDryIceSiyo());
                values.put(C_LABEL_SU, item.getLabelSu());
                values.put(C_KONPO_SU, item.getKonpoSu());
                values.put(C_OUTER_LENGTH, item.getOuterLength());
                values.put(C_OUTER_WIDTH, item.getOuterWidth());
                values.put(C_OUTER_HEIGHT, item.getOuterHeight());
                values.put(C_NET_WEIGHT, item.getNetWeight());
                values.put(C_GROSS_WEIGHT, item.getGrossWeight());
                values.put(C_SAISYU_KONPO_NISUGATA, item.getSaisyuKonpoNisugata());
                values.put(C_BIKO, item.getBiko());
                values.put(C_PALETT_UCHIWAKE, item.getPalettUchiwake());
                values.put(C_CARTON_SU, item.getCartonSu());
                values.put(C_NIFUDA_SU, item.getNifudaSu());
                values.put(C_REGIST_DATE, currentDateStr);
                values.put(C_UPDATE_DATE, currentDateStr);
                db.insert(TABLE_NAME, null, values);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 梱包アウターテーブルInsert（ケースマーク印刷用）
     * @param db
     * @param list
     */
    public void bulkInsertForCsPrint(SQLiteDatabase db, ArrayList<CaseMarkPrintInvoiceSearchInfo> list) {
        try {
            // 現在日取得
            Calendar cl = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateStr = sdf.format(cl.getTime());

            // トランザクション開始
            db.beginTransaction();
            // 全件削除
            db.execSQL("delete from " + TABLE_NAME);
            /*
             * データ登録
             */
            for (CaseMarkPrintInvoiceSearchInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_SYUKKA_DATE, item.getSyukkaDate());
                values.put(C_SHIMUKECHI, item.getShimukeChi());
                values.put(C_CS_NUMBER, item.getCsNumber());
                values.put(C_HYOKI_CS_NUMBER, item.getHyoukiCsNumber());
                values.put(C_CS_PRINT_FLG, item.getCsPrintFlg());
                values.put(C_YUSO_MODE, item.getYusoMode());
                values.put(C_CASEMARK1, item.getCasemark1());
                values.put(C_CASEMARK2, item.getCasemark2());
                values.put(C_CASEMARK3, item.getCasemark3());
                values.put(C_CASEMARK4, item.getCasemark4());
                values.put(C_CASEMARK5, item.getCasemark5());
                values.put(C_CASEMARK6, item.getCasemark6());
                values.put(C_CASEMARK7, item.getCasemark7());
                values.put(C_CASEMARK8, item.getCasemark8());
                values.put(C_CASEMARK9, item.getCasemark9());
                values.put(C_CASEMARK10, item.getCasemark10());
                values.put(C_CASEMARK11, item.getCasemark11());
                values.put(C_CASEMARK12, item.getCasemark12());
                values.put(C_REGIST_DATE, currentDateStr);
                values.put(C_UPDATE_DATE, currentDateStr);
                db.insert(TABLE_NAME, null, values);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 梱包アウター情報取得
     * @param db
     * @return
     */
    public ArrayList<OuterInfo> getOuterInfoList(SQLiteDatabase db, String invoiceNo) {
            final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? ";
            String[] args = new String[1];
            args[0] = invoiceNo;
            ArrayList<OuterInfo> result = new ArrayList<>();
            try (Cursor cursor = db.rawQuery(sql, args)) {
                while (cursor.moveToNext()) {
                    OuterInfo item = new OuterInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                    item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                    item.setCsNumber(cursor.isNull(cursor.getColumnIndex(C_CS_NUMBER)) ?
                            null : cursor.getInt(cursor.getColumnIndex(C_CS_NUMBER)));
                    item.setHyokiCsNumber(cursor.getString(cursor.getColumnIndex(C_HYOKI_CS_NUMBER)));
                    item.setOuterSagyo1(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_1)));
                    item.setOuterSagyo1Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_1_SIYO)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_1_SIYO)));
                    item.setOuterSagyo2(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_2)));
                    item.setOuterSagyo2Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_2_SIYO)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_2_SIYO)));
                    item.setOuterSagyo3(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_3)));
                    item.setOuterSagyo3Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_3_SIYO)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_3_SIYO)));
                    item.setOuterSagyo4(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_4)));
                    item.setOuterSagyo4Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_4_SIYO)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_4_SIYO)));
                    item.setBlueIceSiyo(cursor.isNull(cursor.getColumnIndex(C_BLUEICE_SIYO)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_BLUEICE_SIYO)));
                    item.setDryIceSiyo(cursor.isNull(cursor.getColumnIndex(C_DRYICE_SIYO)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_DRYICE_SIYO)));
                    item.setLabelSu(cursor.isNull(cursor.getColumnIndex(C_LABEL_SU)) ?
                            null : cursor.getInt(cursor.getColumnIndex(C_LABEL_SU)));
                    item.setKonpoSu(cursor.isNull(cursor.getColumnIndex(C_KONPO_SU)) ?
                            null : cursor.getInt(cursor.getColumnIndex(C_KONPO_SU)));
                    item.setOuterLength(cursor.isNull(cursor.getColumnIndex(C_OUTER_LENGTH)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_LENGTH)));
                    item.setOuterWidth(cursor.isNull(cursor.getColumnIndex(C_OUTER_WIDTH)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_WIDTH)));
                    item.setOuterHeight(cursor.isNull(cursor.getColumnIndex(C_OUTER_HEIGHT)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_HEIGHT)));
                    item.setNetWeight(cursor.isNull(cursor.getColumnIndex(C_NET_WEIGHT)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_NET_WEIGHT)));
                    item.setGrossWeight(cursor.isNull(cursor.getColumnIndex(C_GROSS_WEIGHT)) ?
                            null : cursor.getDouble(cursor.getColumnIndex(C_GROSS_WEIGHT)));
                    item.setSaisyuKonpoNisugata(cursor.getString(cursor.getColumnIndex(C_SAISYU_KONPO_NISUGATA)));
                    item.setBiko(cursor.getString(cursor.getColumnIndex(C_BIKO)));
                    item.setPalettUchiwake(cursor.getString(cursor.getColumnIndex(C_PALETT_UCHIWAKE)));
                    item.setCartonSu(cursor.isNull(cursor.getColumnIndex(C_CARTON_SU)) ?
                            null : cursor.getInt(cursor.getColumnIndex(C_CARTON_SU)));
                    item.setNifudaSu(cursor.isNull(cursor.getColumnIndex(C_NIFUDA_SU)) ?
                            null : cursor.getInt(cursor.getColumnIndex(C_NIFUDA_SU)));
                    result.add(item);
                }
                return result;
            }
    }

    /**
     * レコード１件登録
     * @param db
     * @param item
     */
    public void insertRecord(SQLiteDatabase db, OuterInfo item) {
        try {
            // 現在日取得
            Calendar cl = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String currentDateStr = sdf.format(cl.getTime());

            // トランザクション開始
            db.beginTransaction();
            /*
             * データ登録
             */
            ContentValues values = new ContentValues();
            values.put(C_INVOICE_NO, item.getInvoiceNo());
            values.put(C_CS_NUMBER, item.getCsNumber());
            values.put(C_HYOKI_CS_NUMBER, item.getHyokiCsNumber());
            values.put(C_OUTER_SAGYO_1, item.getOuterSagyo1());
            values.put(C_OUTER_SAGYO_1_SIYO, item.getOuterSagyo1Siyo());
            values.put(C_OUTER_SAGYO_2, item.getOuterSagyo2());
            values.put(C_OUTER_SAGYO_2_SIYO, item.getOuterSagyo2Siyo());
            values.put(C_OUTER_SAGYO_3, item.getOuterSagyo3());
            values.put(C_OUTER_SAGYO_3_SIYO, item.getOuterSagyo3Siyo());
            values.put(C_OUTER_SAGYO_4, item.getOuterSagyo4());
            values.put(C_OUTER_SAGYO_4_SIYO, item.getOuterSagyo4Siyo());
            values.put(C_BLUEICE_SIYO, item.getBlueIceSiyo());
            values.put(C_DRYICE_SIYO, item.getDryIceSiyo());
            values.put(C_LABEL_SU, item.getLabelSu());
            values.put(C_KONPO_SU, item.getKonpoSu());
            values.put(C_OUTER_LENGTH, item.getOuterLength());
            values.put(C_OUTER_WIDTH, item.getOuterWidth());
            values.put(C_OUTER_HEIGHT, item.getOuterHeight());
            values.put(C_NET_WEIGHT, item.getNetWeight());
            values.put(C_GROSS_WEIGHT, item.getGrossWeight());
            values.put(C_SAISYU_KONPO_NISUGATA, item.getSaisyuKonpoNisugata());
            values.put(C_BIKO, item.getBiko());
            values.put(C_PALETT_UCHIWAKE, item.getPalettUchiwake());
            values.put(C_CARTON_SU, item.getCartonSu());
            values.put(C_NIFUDA_SU, item.getNifudaSu());
            values.put(C_REGIST_DATE, currentDateStr);
            values.put(C_UPDATE_DATE, currentDateStr);
            db.insert(TABLE_NAME, null, values);
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 梱包アウターレコード取得
     * @param db
     * @param invoiceNo
     * @param csNo
     * @return
     */
    public OuterInfo getOuterInfoRecord(SQLiteDatabase db, String invoiceNo, int csNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND CS_NUMBER = ?";
        String[] args = new String[2];
        args[0] = invoiceNo;
        args[1] = String.valueOf(csNo);

        OuterInfo result = null;
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                result = new OuterInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                result.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                result.setCsNumber(cursor.isNull(cursor.getColumnIndex(C_CS_NUMBER)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_CS_NUMBER)));
                result.setHyokiCsNumber(cursor.getString(cursor.getColumnIndex(C_HYOKI_CS_NUMBER)));
                result.setOuterSagyo1(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_1)));
                result.setOuterSagyo1Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_1_SIYO)) ?
                        null: cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_1_SIYO)));
                result.setOuterSagyo2(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_2)));
                result.setOuterSagyo2Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_2_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_2_SIYO)));
                result.setOuterSagyo3(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_3)));
                result.setOuterSagyo3Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_3_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_3_SIYO)));
                result.setOuterSagyo4(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_4)));
                result.setOuterSagyo4Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_4_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_4_SIYO)));
                result.setBlueIceSiyo(cursor.isNull(cursor.getColumnIndex(C_BLUEICE_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_BLUEICE_SIYO)));
                result.setDryIceSiyo(cursor.isNull(cursor.getColumnIndex(C_DRYICE_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_DRYICE_SIYO)));
                result.setLabelSu(cursor.isNull(cursor.getColumnIndex(C_LABEL_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_LABEL_SU)));
                result.setKonpoSu(cursor.isNull(cursor.getColumnIndex(C_KONPO_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_KONPO_SU)));
                result.setOuterLength(cursor.isNull(cursor.getColumnIndex(C_OUTER_LENGTH)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_LENGTH)));
                result.setOuterWidth(cursor.isNull(cursor.getColumnIndex(C_OUTER_WIDTH)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_WIDTH)));
                result.setOuterHeight(cursor.isNull(cursor.getColumnIndex(C_OUTER_HEIGHT)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_HEIGHT)));
                result.setNetWeight(cursor.isNull(cursor.getColumnIndex(C_NET_WEIGHT)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_NET_WEIGHT)));
                result.setGrossWeight(cursor.isNull(cursor.getColumnIndex(C_GROSS_WEIGHT)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_GROSS_WEIGHT)));
                result.setSaisyuKonpoNisugata(cursor.getString(cursor.getColumnIndex(C_SAISYU_KONPO_NISUGATA)));
                result.setBiko(cursor.getString(cursor.getColumnIndex(C_BIKO)));
                result.setPalettUchiwake(cursor.getString(cursor.getColumnIndex(C_PALETT_UCHIWAKE)));
                result.setCartonSu(cursor.isNull(cursor.getColumnIndex(C_CARTON_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_CARTON_SU)));
                result.setNifudaSu(cursor.isNull(cursor.getColumnIndex(C_NIFUDA_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_NIFUDA_SU)));
                break;
            }
            return result;
        }
    }

    /**
     * 表記用ケースマーク番号でまとめた梱包情報を取得する。
     * @param db
     * @return
     */
    public ArrayList<OuterInfo> getOuterInfoListGroupByCsNo(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " where " + C_INVOICE_NO + " = '" + invoiceNo + "' GROUP BY " + C_HYOKI_CS_NUMBER;

        ArrayList<OuterInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                OuterInfo item = new OuterInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setCsNumber(cursor.isNull(cursor.getColumnIndex(C_CS_NUMBER)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_CS_NUMBER)));
                item.setHyokiCsNumber(cursor.getString(cursor.getColumnIndex(C_HYOKI_CS_NUMBER)));
                item.setOuterSagyo1(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_1)));
                item.setOuterSagyo1Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_1_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_1_SIYO)));
                item.setOuterSagyo2(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_2)));
                item.setOuterSagyo2Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_2_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_2_SIYO)));
                item.setOuterSagyo3(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_3)));
                item.setOuterSagyo3Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_3_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_3_SIYO)));
                item.setOuterSagyo4(cursor.getString(cursor.getColumnIndex(C_OUTER_SAGYO_4)));
                item.setOuterSagyo4Siyo(cursor.isNull(cursor.getColumnIndex(C_OUTER_SAGYO_4_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_SAGYO_4_SIYO)));
                item.setBlueIceSiyo(cursor.isNull(cursor.getColumnIndex(C_BLUEICE_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_BLUEICE_SIYO)));
                item.setDryIceSiyo(cursor.isNull(cursor.getColumnIndex(C_DRYICE_SIYO)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_DRYICE_SIYO)));
                item.setLabelSu(cursor.isNull(cursor.getColumnIndex(C_LABEL_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_LABEL_SU)));
                item.setKonpoSu(cursor.isNull(cursor.getColumnIndex(C_KONPO_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_KONPO_SU)));
                item.setOuterLength(cursor.isNull(cursor.getColumnIndex(C_OUTER_LENGTH)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_LENGTH)));
                item.setOuterWidth(cursor.isNull(cursor.getColumnIndex(C_OUTER_WIDTH)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_WIDTH)));
                item.setOuterHeight(cursor.isNull(cursor.getColumnIndex(C_OUTER_HEIGHT)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_OUTER_HEIGHT)));
                item.setNetWeight(cursor.isNull(cursor.getColumnIndex(C_NET_WEIGHT)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_NET_WEIGHT)));
                item.setGrossWeight(cursor.isNull(cursor.getColumnIndex(C_GROSS_WEIGHT)) ?
                        null : cursor.getDouble(cursor.getColumnIndex(C_GROSS_WEIGHT)));
                item.setSaisyuKonpoNisugata(cursor.getString(cursor.getColumnIndex(C_SAISYU_KONPO_NISUGATA)));
                item.setBiko(cursor.getString(cursor.getColumnIndex(C_BIKO)));
                item.setPalettUchiwake(cursor.getString(cursor.getColumnIndex(C_PALETT_UCHIWAKE)));
                item.setCartonSu(cursor.isNull(cursor.getColumnIndex(C_CARTON_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_CARTON_SU)));
                item.setNifudaSu(cursor.isNull(cursor.getColumnIndex(C_NIFUDA_SU)) ?
                        null : cursor.getInt(cursor.getColumnIndex(C_NIFUDA_SU)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * Invoice番号でまとめた梱包情報を取得する。
     *      ※ケースマーク印刷画面用
     * @param db
     * @return
     */
    public ArrayList<CaseMarkPrintStatusInfo> getOuterInfoListGroupByInvoiceNo(SQLiteDatabase db) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " GROUP BY " + C_INVOICE_NO;

        ArrayList<CaseMarkPrintStatusInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                CaseMarkPrintStatusInfo item = new CaseMarkPrintStatusInfo(
                        cursor.getString(cursor.getColumnIndex(C_INVOICE_NO))
                        ,cursor.getString(cursor.getColumnIndex(C_SHIMUKECHI))
                        ,cursor.getString(cursor.getColumnIndex(C_SYUKKA_DATE))
                        ,cursor.getString(cursor.getColumnIndex(C_YUSO_MODE))
                        ,cursor.getString(cursor.getColumnIndex(C_CS_PRINT_FLG)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * Invoice番号指定ケースマーク印刷情報取得
     *      ※ケースマーク印刷画面用
     * @param db
     * @param invoiceNo
     * @return
     */
    public ArrayList<CaseMarkPrintInfo> getPrintInfoList(SQLiteDatabase db, String invoiceNo){
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? ORDER BY INVOICE_NO,CS_NUMBER";
        String[] args = new String[1];
        args[0] = invoiceNo;

        ArrayList<CaseMarkPrintInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                CaseMarkPrintInfo item = new CaseMarkPrintInfo(
                        cursor.getString(cursor.getColumnIndex(C_INVOICE_NO))
                        ,cursor.getString(cursor.getColumnIndex(C_CS_NUMBER))
                        ,null
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK1))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK2))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK3))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK4))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK5))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK6))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK7))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK8))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK9))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK10))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK11))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK12)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * ケースマーク印刷全情報取得
     *      ※ケースマーク印刷画面用
     * @param db
     * @return
     */
    public ArrayList<CaseMarkPrintInfo> getAllPrintInfoList(SQLiteDatabase db){
        final String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY INVOICE_NO,CS_NUMBER";
        ArrayList<CaseMarkPrintInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                CaseMarkPrintInfo item = new CaseMarkPrintInfo(
                        cursor.getString(cursor.getColumnIndex(C_INVOICE_NO))
                        ,cursor.getString(cursor.getColumnIndex(C_CS_NUMBER))
                        , null
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK1))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK2))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK3))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK4))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK5))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK6))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK7))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK8))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK9))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK10))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK11))
                        ,cursor.getString(cursor.getColumnIndex(C_CASEMARK12)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * 梱包に紐づくデータを解除するためのキー情報を取得する。
     * @param db
     * @return
     */
    public ArrayList<PackingCancelInfo> getPackingCancelInfoList(SQLiteDatabase db, String invoiceNo, String displayCsNo) {
        final String sql = "select t2.INVOICE_NO, t2.RENBAN, t2.LINE_NO, t2.CS_NUMBER, t2.OVERPACK_NO FROM " +
                "( select INVOICE_NO, CS_NUMBER from T_KONPO_OUTER where INVOICE_NO = '" + invoiceNo +
                "' and HYOKI_CS_NUMBER = '" + displayCsNo +
                "') t1 inner join T_SYUKKOSIJI_DETAIL t2 on t1.INVOICE_NO = t2.INVOICE_NO and t1.CS_NUMBER = t2.CS_NUMBER";

        ArrayList<PackingCancelInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                PackingCancelInfo item = new PackingCancelInfo(
                        cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)),
                        cursor.getInt(cursor.getColumnIndex("RENBAN")),
                        cursor.getInt(cursor.getColumnIndex("LINE_NO")),
                        cursor.getInt(cursor.getColumnIndex("CS_NUMBER")),
                        cursor.getInt(cursor.getColumnIndex("OVERPACK_NO"))
                );
                result.add(item);
            }
            return result;
        }
    }

    /**
     * ケースマーク番号最大値取得
     * @param db
     * @param invoiceNo
     * @return
     */
    public int getMaxCsNo(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT MAX(CS_NUMBER) AS CS_NUMBER FROM " + TABLE_NAME + " WHERE INVOICE_NO = ?";
        String[] args = new String[1];
        args[0] = invoiceNo;

        int result = 0;
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(C_CS_NUMBER));
                break;
            }
            return result;
        }
    }

    /**
     * レコード削除（Invoice番号、表記用ケースマーク番号指定
     * @param db
     * @param invoiceNo
     * @param displayCsNo
     */
    public void deleteByInvoiceDisplayCsNo(SQLiteDatabase db, String invoiceNo, String displayCsNo) {
        try {
            db.beginTransaction();

            String sql = "delete from " + TABLE_NAME + " where "
                    + C_INVOICE_NO + " = '" + invoiceNo + "' and "
                    + C_HYOKI_CS_NUMBER + " = '" + displayCsNo + "'";
            db.execSQL(sql);

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * GW未入力チェック
     * @param db
     * @param invoiceNo
     * @return
     */
    public boolean isGwInputFinished(SQLiteDatabase db, String invoiceNo) {
        String[] args = new String[1];
        args[0] = invoiceNo;
        long recodeCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME,"INVOICE_NO = ? and GROSS_WEIGHT = 0",args);
        return(recodeCount > 0 ? true : false);
    }

    /**
     * 梱包数確認
     * @param db
     * @param invoiceNo
     * @return
     */
    public long countOuterTable(SQLiteDatabase db, String invoiceNo) {
        String[] args = new String[1];
        args[0] = invoiceNo;
        return(DatabaseUtils.queryNumEntries(db, TABLE_NAME,"INVOICE_NO = ? ",args));
    }

    /**
     * 印刷済フラグ更新
     * @param db
     * @param invoiceNo
     * @param flgValue
     */
    public void updatePrintedFlg(SQLiteDatabase db, String invoiceNo, String flgValue) {
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(C_CS_PRINT_FLG, flgValue);
            String[] whereSql = {invoiceNo};
            db.update(TABLE_NAME, contentValues, "INVOICE_NO = ?", whereSql);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * 印刷済フラグ全更新
     * @param db
     * @param flgValue
     */
    public void updatePrintedFlgAll(SQLiteDatabase db, String flgValue) {
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(C_CS_PRINT_FLG, flgValue);
            db.update(TABLE_NAME, contentValues, null, null);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}
