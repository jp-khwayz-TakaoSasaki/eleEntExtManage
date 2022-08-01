package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.instr_cfm.InvoiceTempInfo;

public class InvoiceTempDao {
    public static final String TABLE_NAME = "T_INVOICE_TENP";
    public static final String C_INVOICE_NO = "INVOICE_NO";
    public static final String C_TENP_NO = "TENP_NO";
    public static final String C_TENP_PATH = "TENP_PATH";
    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_INVOICE_NO + " TEXT NOT NULL," // Invoice番号
            + C_TENP_NO + " INTEGER," // 添付番号
            + C_TENP_PATH + " TEXT," // 添付ファイルパス
            + "PRIMARY KEY("
            + C_INVOICE_NO + "," + C_TENP_NO
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
     * 出庫登録Invoice検索用データ登録
     */
    public void bulkInsert(SQLiteDatabase db, String invoiceNo, ArrayList<InvoiceTempInfo> list) {
        try {
            // トランザクション開始
            db.beginTransaction();
            // 全件削除
            db.execSQL("DELETE FROM " + TABLE_NAME);
            // 登録リストが無い場合はテーブルクリアで終了
            if (list == null) {
                // トランザクションコミット
                db.setTransactionSuccessful();
                return;
            }
            /*
             * データ登録
             */
            for (InvoiceTempInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, invoiceNo);
                values.put(C_TENP_NO, item.getTempNo());
                values.put(C_TENP_PATH, item.getTempPath());
                db.insert(TABLE_NAME, null, values);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<InvoiceTempInfo> getIssueRegInvoiceList(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + C_INVOICE_NO + " = ?;";
        String[] args = new String[1];
        args[0] = invoiceNo;
        ArrayList<InvoiceTempInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                InvoiceTempInfo item = new InvoiceTempInfo();
                item.setTempNo(cursor.getInt(cursor.getColumnIndex(C_TENP_NO)));
                item.setTempPath(cursor.getString(cursor.getColumnIndex(C_TENP_PATH)));
                result.add(item);
            }
            return result;
        }
    }
}
