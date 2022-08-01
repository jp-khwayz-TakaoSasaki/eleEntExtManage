package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.khwayz.eleEntExtManage.common.models.InnerInfo;
import jp.co.khwayz.eleEntExtManage.common.models.PackingCancelInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;

/**
 * 梱包インナーテーブルDao
 */
public class KonpoInnerDao {
    public static final String TABLE_NAME = "T_KONPO_INNER";
    public static final String C_INVOICE_NO = "INVOICE_NO";
    public static final String C_RENBAN = "RENBAN";
    public static final String C_LINE_NO = "LINE_NO";
    public static final String C_INNER_SAGYO_1 = "INNER_SAGYO_1";
    public static final String C_INNER_SAGYO_1_SIYO = "INNER_SAGYO_1_SIYO";
    public static final String C_INNER_SAGYO_2 = "INNER_SAGYO_2";
    public static final String C_INNER_SAGYO_2_SIYO = "INNER_SAGYO_2_SIYO";
    public static final String C_INNER_SAGYO_3 = "INNER_SAGYO_3";
    public static final String C_INNER_SAGYO_3_SIYO = "INNER_SAGYO_3_SIYO";
    public static final String C_INNER_SAGYO_4 = "INNER_SAGYO_4";
    public static final String C_INNER_SAGYO_4_SIYO = "INNER_SAGYO_4_SIYO";
    public static final String C_LABEL_SU = "LABEL_SU";
    public static final String C_NET_WEIGHT = "NET_WEIGHT";
    public static final String C_DAN_NAIYO_RYO = "DAN_NAIYO_RYO";
    public static final String C_DAN_TANI = "DAN_TANI";
    public static final String C_DAN_NAISO_YOKI = "DAN_NAISO_YOKI";
    public static final String C_DAN_HONSU = "DAN_HONSU";
    public static final String C_DAN_GAISO_YOKI = "DAN_GAISO_YOKI";
    public static final String C_DAN_GAISO_KOSU = "DAN_GAISO_KOSU";
    public static final String C_BIKO = "BIKO";
    public static final String C_REGIST_DATE = "REGIST_DATE";
    public static final String C_UPDATE_DATE = "UPDATE_DATE";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_INVOICE_NO + " TEXT NOT NULL,"
            + C_RENBAN + " INTEGER NOT NULL,"
            + C_LINE_NO + " INTEGER NOT NULL,"
            + C_INNER_SAGYO_1 + " TEXT NOT NULL,"
            + C_INNER_SAGYO_1_SIYO + " REAL,"
            + C_INNER_SAGYO_2 + " TEXT,"
            + C_INNER_SAGYO_2_SIYO + " REAL,"
            + C_INNER_SAGYO_3 + " TEXT,"
            + C_INNER_SAGYO_3_SIYO + " REAL,"
            + C_INNER_SAGYO_4 + " TEXT,"
            + C_INNER_SAGYO_4_SIYO + " REAL,"
            + C_LABEL_SU + " INTEGER,"
            + C_NET_WEIGHT + " REAL,"
            + C_DAN_NAIYO_RYO + " REAL,"
            + C_DAN_TANI + " TEXT,"
            + C_DAN_NAISO_YOKI + " TEXT,"
            + C_DAN_HONSU + " INTEGER,"
            + C_DAN_GAISO_YOKI + " TEXT,"
            + C_DAN_GAISO_KOSU + " INTEGER,"
            + C_BIKO + " TEXT,"
            + C_REGIST_DATE + " TEXT,"
            + C_UPDATE_DATE + " TEXT,"
            + "PRIMARY KEY("
            + C_INVOICE_NO + ","
            + C_RENBAN + ","
            + C_LINE_NO
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
     * インナー情報リスト取得
     * @param db
     * @param invoiceNo
     * @return
     */
    public ArrayList<InnerInfo> getInnerInfoList(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ?" ;
        String[] args = new String[1];
        args[0] = invoiceNo;

        ArrayList<InnerInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                InnerInfo item = new InnerInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setRenban(cursor.getInt(cursor.getColumnIndex(C_RENBAN)));
                item.setLineNo(cursor.getInt(cursor.getColumnIndex(C_LINE_NO)));
                item.setInnerSagyo1(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_1)));
                item.setInnerSagyo1Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_1_SIYO)));
                item.setInnerSagyo2(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_2)));
                item.setInnerSagyo2Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_2_SIYO)));
                item.setInnerSagyo3(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_3)));
                item.setInnerSagyo3Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_3_SIYO)));
                item.setInnerSagyo4(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_4)));
                item.setInnerSagyo4Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_4_SIYO)));
                item.setLabelSu(cursor.getInt(cursor.getColumnIndex(C_LABEL_SU)));
                item.setNetWeight(cursor.getDouble(cursor.getColumnIndex(C_NET_WEIGHT)));
                item.setDanNaiyoRyo(cursor.getInt(cursor.getColumnIndex(C_DAN_NAIYO_RYO)));
                item.setDanTani(cursor.getString(cursor.getColumnIndex(C_DAN_TANI)));
                item.setDanNaisoYoki(cursor.getString(cursor.getColumnIndex(C_DAN_NAISO_YOKI)));
                item.setDanHonsu(cursor.getInt(cursor.getColumnIndex(C_DAN_HONSU)));
                item.setDanGaisoYoki(cursor.getString(cursor.getColumnIndex(C_DAN_GAISO_YOKI)));
                item.setDanGaisoKosu(cursor.getInt(cursor.getColumnIndex(C_DAN_GAISO_KOSU)));
                item.setBiko(cursor.getString(cursor.getColumnIndex(C_BIKO)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * ピッキングInvoice検索用
     * @param db
     * @param list
     */
    public void bulkInsert(SQLiteDatabase db, ArrayList<InnerInfo> list) {
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
            for (InnerInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_RENBAN, item.getRenban());
                values.put(C_LINE_NO, item.getLineNo());
                values.put(C_INNER_SAGYO_1, item.getInnerSagyo1() );
                values.put(C_INNER_SAGYO_1_SIYO, item.getInnerSagyo1Siyo() );
                values.put(C_INNER_SAGYO_2, item.getInnerSagyo2() );
                values.put(C_INNER_SAGYO_2_SIYO, item.getInnerSagyo2Siyo() );
                values.put(C_INNER_SAGYO_3, item.getInnerSagyo3() );
                values.put(C_INNER_SAGYO_3_SIYO, item.getInnerSagyo3Siyo() );
                values.put(C_INNER_SAGYO_4, item.getInnerSagyo4() );
                values.put(C_INNER_SAGYO_4_SIYO, item.getInnerSagyo4Siyo() );
                values.put(C_LABEL_SU, item.getLabelSu() );
                values.put(C_NET_WEIGHT, item.getNetWeight() );
                values.put(C_DAN_NAIYO_RYO, item.getDanNaiyoRyo() );
                values.put(C_DAN_TANI, item.getDanTani() );
                values.put(C_DAN_NAISO_YOKI, item.getDanNaisoYoki() );
                values.put(C_DAN_HONSU, item.getDanHonsu() );
                values.put(C_DAN_GAISO_YOKI, item.getDanGaisoYoki() );
                values.put(C_DAN_GAISO_KOSU, item.getDanGaisoKosu() );
                values.put(C_BIKO, item.getBiko() );
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
     * レコードreplace
     * @param db
     * @param invoiceNo
     * @param updateList
     * @param updateInfo
     */
    public void replace(SQLiteDatabase db, String invoiceNo, ArrayList<SyukkoShijiKeyInfo> updateList, InnerInfo updateInfo) {
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
            for (SyukkoShijiKeyInfo item : updateList) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, invoiceNo);
                values.put(C_RENBAN, item.getRenban());
                values.put(C_LINE_NO, item.getLineNo());
                values.put(C_INNER_SAGYO_1, updateInfo.getInnerSagyo1() );
                values.put(C_INNER_SAGYO_1_SIYO, updateInfo.getInnerSagyo1Siyo() );
                values.put(C_INNER_SAGYO_2, updateInfo.getInnerSagyo2() );
                values.put(C_INNER_SAGYO_2_SIYO, updateInfo.getInnerSagyo2Siyo() );
                values.put(C_INNER_SAGYO_3, updateInfo.getInnerSagyo3() );
                values.put(C_INNER_SAGYO_3_SIYO, updateInfo.getInnerSagyo3Siyo() );
                values.put(C_INNER_SAGYO_4, updateInfo.getInnerSagyo4() );
                values.put(C_INNER_SAGYO_4_SIYO, updateInfo.getInnerSagyo4Siyo() );
                values.put(C_LABEL_SU, updateInfo.getLabelSu() );
                values.put(C_NET_WEIGHT, updateInfo.getNetWeight() );
                values.put(C_DAN_NAIYO_RYO, updateInfo.getDanNaiyoRyo() );
                values.put(C_DAN_TANI, updateInfo.getDanTani() );
                values.put(C_DAN_NAISO_YOKI, updateInfo.getDanNaisoYoki() );
                values.put(C_DAN_HONSU, updateInfo.getDanHonsu() );
                values.put(C_DAN_GAISO_YOKI, updateInfo.getDanGaisoYoki() );
                values.put(C_DAN_GAISO_KOSU, updateInfo.getDanGaisoKosu() );
                values.put(C_BIKO, updateInfo.getBiko() );
                values.put(C_REGIST_DATE, currentDateStr);
                values.put(C_UPDATE_DATE, currentDateStr);
                db.replace(TABLE_NAME, null, values);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * レコード取得
     * @param db
     * @param invoiceNo
     * @param renban
     * @param lineNo
     * @return
     */
    public InnerInfo getRecord(SQLiteDatabase db, String invoiceNo, int renban, int lineNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND RENBAN = ? AND LINE_NO = ?";
        String[] args = new String[3];
        args[0] = invoiceNo;
        args[1] = String.valueOf(renban);
        args[2] = String.valueOf(lineNo);
        InnerInfo result = null;
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                result = new InnerInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                result.setRenban(cursor.getInt(cursor.getColumnIndex(C_RENBAN)));
                result.setLineNo(cursor.getInt(cursor.getColumnIndex(C_LINE_NO)));
                result.setInnerSagyo1(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_1)));
                result.setInnerSagyo1Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_1_SIYO)));
                result.setInnerSagyo2(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_2)));
                result.setInnerSagyo2Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_2_SIYO)));
                result.setInnerSagyo3(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_3)));
                result.setInnerSagyo3Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_3_SIYO)));
                result.setInnerSagyo4(cursor.getString(cursor.getColumnIndex(C_INNER_SAGYO_4)));
                result.setInnerSagyo4Siyo(cursor.getDouble(cursor.getColumnIndex(C_INNER_SAGYO_4_SIYO)));
                result.setLabelSu(cursor.getInt(cursor.getColumnIndex(C_LABEL_SU)));
                result.setNetWeight(cursor.getDouble(cursor.getColumnIndex(C_NET_WEIGHT)));
                result.setDanNaiyoRyo(cursor.getDouble(cursor.getColumnIndex(C_DAN_NAIYO_RYO)));
                result.setDanTani(cursor.getString(cursor.getColumnIndex(C_DAN_TANI)));
                result.setDanNaisoYoki(cursor.getString(cursor.getColumnIndex(C_DAN_NAISO_YOKI)));
                result.setDanHonsu(cursor.getInt(cursor.getColumnIndex(C_DAN_HONSU)));
                result.setDanGaisoYoki(cursor.getString(cursor.getColumnIndex(C_DAN_GAISO_YOKI)));
                result.setDanGaisoKosu(cursor.getInt(cursor.getColumnIndex(C_DAN_GAISO_KOSU)));
                result.setBiko(cursor.getString(cursor.getColumnIndex(C_BIKO)));

                break;
            }
            return result;
        }
    }

    /**
     * レコード削除
     * @param db
     * @param list
     */
    public void delete(SQLiteDatabase db, ArrayList<PackingCancelInfo> list) {
        try {
            // トランザクション開始
            db.beginTransaction();
            /*
             * データ登録
             */
            for (PackingCancelInfo item : list) {
                String sql = "delete from " + TABLE_NAME + " where "
                        + C_INVOICE_NO + " = '" + item.getInvoiceNo() + "' and "
                        + C_RENBAN + " = " + item.getRenban() + " and "
                        + C_LINE_NO + " = " + item.getLineNo();
                db.execSQL(sql);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
