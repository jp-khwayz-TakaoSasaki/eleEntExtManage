package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.khwayz.eleEntExtManage.common.models.OverPackKonpoShizaiInfo;
import jp.co.khwayz.eleEntExtManage.common.models.PackingCancelInfo;

/**
 * オーバーパック梱包資材テーブルDao
 */
public class OverpackKonpoShizaiDao {
    public static final String TABLE_NAME = "T_OVERPACK_KONPOSHIZAI";
    public static final String C_INVOICE_NO = "INVOICE_NO";
    public static final String C_OVERPACK_NO = "OVERPACK_NO";
    public static final String C_PACKING_MATERIAL_NO = "PACKING_MATERIAL_NO";
    public static final String C_PACKING_MATERIAL = "PACKING_MATERIAL";
    public static final String C_REGIST_DATE = "REGIST_DATE";
    public static final String C_UPDATE_DATE = "UPDATE_DATE";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_INVOICE_NO + " TEXT NOT NULL,"
            + C_OVERPACK_NO + " INTEGER NOT NULL,"
            + C_PACKING_MATERIAL_NO + " INTEGER,"
            + C_PACKING_MATERIAL + " TEXT,"
            + C_REGIST_DATE + " TEXT,"
            + C_UPDATE_DATE + " TEXT,"
            + "PRIMARY KEY("
            + C_INVOICE_NO + ","
            + C_OVERPACK_NO + ","
            + C_PACKING_MATERIAL_NO
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
    public void bulkInsert(SQLiteDatabase db, ArrayList<OverPackKonpoShizaiInfo> list) {
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
            for (OverPackKonpoShizaiInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_OVERPACK_NO, item.getOverPackNo());
                values.put(C_PACKING_MATERIAL_NO, item.getPackingMaterialNo());
                values.put(C_PACKING_MATERIAL, item.getPackingMaterial());
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
     * 梱包アウターテーブルInsert
     * @param db
     * @param list
     */
    public void insert(SQLiteDatabase db, ArrayList<OverPackKonpoShizaiInfo> list) {
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
            for (OverPackKonpoShizaiInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_OVERPACK_NO, item.getOverPackNo());
                values.put(C_PACKING_MATERIAL_NO, item.getPackingMaterialNo());
                values.put(C_PACKING_MATERIAL, item.getPackingMaterial());
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
     * 指定Invoice番号のレコードを全取得する
     * @param db
     * @param invoiceNo
     * @return
     */
    public ArrayList<OverPackKonpoShizaiInfo> getOverPackKonpoShizaiList(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + C_INVOICE_NO + " = ?";
        String[] args = new String[1];
        args[0] = invoiceNo;
        ArrayList<OverPackKonpoShizaiInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                OverPackKonpoShizaiInfo item = new OverPackKonpoShizaiInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setOverPackNo(cursor.getInt(cursor.getColumnIndex(C_OVERPACK_NO)));
                item.setPackingMaterialNo(cursor.getInt(cursor.getColumnIndex(C_PACKING_MATERIAL_NO)));
                item.setPackingMaterial(cursor.getString(cursor.getColumnIndex(C_PACKING_MATERIAL)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * オーバーパック番号指定レコード取得
     * @param db
     * @param invoiceNo
     * @param overPackNo
     * @return
     */
    public ArrayList<OverPackKonpoShizaiInfo> getOverPackKonpoShizaiListByOverPackNo(
            SQLiteDatabase db, String invoiceNo, String overPackNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + C_INVOICE_NO + " = ? AND " + C_OVERPACK_NO + " = ?"
                + "ORDER BY " + C_PACKING_MATERIAL_NO;
        String[] args = new String[2];
        args[0] = invoiceNo;
        args[1] = overPackNo;
        ArrayList<OverPackKonpoShizaiInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                OverPackKonpoShizaiInfo item = new OverPackKonpoShizaiInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setOverPackNo(cursor.getInt(cursor.getColumnIndex(C_OVERPACK_NO)));
                item.setPackingMaterialNo(cursor.getInt(cursor.getColumnIndex(C_PACKING_MATERIAL_NO)));
                item.setPackingMaterial(cursor.getString(cursor.getColumnIndex(C_PACKING_MATERIAL)));
                result.add(item);
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
             * データ削除
             */
            for (PackingCancelInfo item : list) {
                String sql = "delete from " + TABLE_NAME + " where "
                        + C_INVOICE_NO + " = '" + item.getInvoiceNo() + "' and "
                        + C_OVERPACK_NO + " = " + item.getOverPackNo();
                db.execSQL(sql);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteByOverPackNo(SQLiteDatabase db, String invoiceNo, String overPackNo) {
        try {
            // トランザクション開始
            db.beginTransaction();

            // データ削除
            String sql = "delete from " + TABLE_NAME + " where "
                    + C_INVOICE_NO + " = '" + invoiceNo + "' and "
                    + C_OVERPACK_NO + " = " + overPackNo;
            db.execSQL(sql);
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
