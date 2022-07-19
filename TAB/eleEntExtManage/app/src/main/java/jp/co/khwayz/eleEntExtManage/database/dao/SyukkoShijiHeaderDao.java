package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.khwayz.eleEntExtManage.common.models.PickedInvoiceSearchInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceSearchInfo;

public class SyukkoShijiHeaderDao {
    public static final String TABLE_NAME = "T_SYUKKOSIJI_HEADER";
    public static final String C_INVOICE_NO = "INVOICE_NO";
    public static final String C_TOKUISAKI_NAME = "TOKUISAKI_NAME";
    public static final String C_SHIMUKECHI = "SHIMUKECHI";
    public static final String C_LIST_HENSIN_KIBOBI = "LIST_HENSIN_KIBOBI";
    public static final String C_SYUKKA_DATE = "SYUKKA_DATE";
    public static final String C_GYOSU = "GYOSU";
    public static final String C_HOREI = "HOREI";
    public static final String C_KIKENHIN = "KIKENHIN";
    public static final String C_BIKOU = "BIKOU";
    public static final String C_KONPO_HOLD_FLG = "KONPO_HOLD_FLG";
    public static final String C_UPDATE_FLAG = "UPDATE_FLAG";
    public static final String C_REGIST_DATE = "REGIST_DATE";
    public static final String C_UPDATE_DATE = "UPDATE_DATE";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_INVOICE_NO + " TEXT NOT NULL," // Invoice番号
            + C_TOKUISAKI_NAME + " TEXT," // 得意先名
            + C_SHIMUKECHI + " TEXT," // 仕向け地
            + C_LIST_HENSIN_KIBOBI + " TEXT," // リスト返信希望日
            + C_SYUKKA_DATE + " TEXT," // 出荷日
            + C_GYOSU + " INTEGER," // 行数
            + C_HOREI + " TEXT," // 保冷
            + C_KIKENHIN + " TEXT," // 危険品
            + C_BIKOU + " TEXT," // 備考
            + C_KONPO_HOLD_FLG + " TEXT," // 梱包保留フラグ
            + C_UPDATE_FLAG + " TEXT," // 更新フラグ
            + C_REGIST_DATE + " TEXT," // 登録日時
            + C_UPDATE_DATE + " TEXT," // 更新日時
            + "PRIMARY KEY("
            + C_INVOICE_NO
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
     * 出庫Invoice検索用データ登録
     * @param db
     * @param list
     */
    public void bulkInsert(SQLiteDatabase db, ArrayList<SyukkoInvoiceSearchInfo> list) {
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
            for (SyukkoInvoiceSearchInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_TOKUISAKI_NAME, item.getCustomerName());
                values.put(C_SHIMUKECHI, item.getDestination());
                values.put(C_LIST_HENSIN_KIBOBI, item.getListReplyDesiredDate());
                values.put(C_SYUKKA_DATE, item.getIssueDate());
                values.put(C_GYOSU, item.getLineCount());
                values.put(C_HOREI, item.getTransportTemprature());
                values.put(C_KIKENHIN, item.getDangerousGoods());
                values.put(C_BIKOU, item.getRemarks());
                values.put(C_KONPO_HOLD_FLG, item.getPickingHoldFlag());
                values.put(C_UPDATE_FLAG, "0");
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
     * ピッキングInvoice検索用データ登録
     * @param db
     * @param list
     */
    public void bulkInsertPicked(SQLiteDatabase db, ArrayList<PickedInvoiceSearchInfo> list) {
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
            for (PickedInvoiceSearchInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_TOKUISAKI_NAME, item.getCustomerName());
                values.put(C_SHIMUKECHI, item.getDestination());
                values.put(C_LIST_HENSIN_KIBOBI, item.getListReplyDesiredDate());
                values.put(C_SYUKKA_DATE, item.getIssueDate());
                values.put(C_GYOSU, item.getLineCount());
                values.put(C_HOREI, item.getTransportTemprature());
                values.put(C_KIKENHIN, item.getDangerousGoods());
                values.put(C_BIKOU, item.getRemarks());
                values.put(C_KONPO_HOLD_FLG, item.getKonpoHoldFlag());
                values.put(C_UPDATE_FLAG, "0");
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

    public ArrayList<SyukkoInvoiceSearchInfo> getSyukkoShijiList(SQLiteDatabase db) {
        final String sql = "SELECT * FROM " + TABLE_NAME;

        ArrayList<SyukkoInvoiceSearchInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                SyukkoInvoiceSearchInfo item = new SyukkoInvoiceSearchInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setCustomerName(cursor.getString(cursor.getColumnIndex(C_TOKUISAKI_NAME)));
                item.setDestination(cursor.getString(cursor.getColumnIndex(C_SHIMUKECHI)));
                item.setListReplyDesiredDate(cursor.getString(cursor.getColumnIndex(C_LIST_HENSIN_KIBOBI)));
                item.setIssueDate(cursor.getString(cursor.getColumnIndex(C_SYUKKA_DATE)));
                item.setLineCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_GYOSU))));
                item.setTransportTemprature(cursor.getString(cursor.getColumnIndex(C_HOREI)));
                item.setDangerousGoods(cursor.getString(cursor.getColumnIndex(C_KIKENHIN)));
                item.setRemarks(cursor.getString(cursor.getColumnIndex(C_BIKOU)));
                item.setPickingHoldFlag(cursor.getString(cursor.getColumnIndex(C_KONPO_HOLD_FLG)));
                result.add(item);
            }
            return result;
        }
    }

    public ArrayList<SyukkoInvoiceSearchInfo> getSyukkoShijiSortList(SQLiteDatabase db, String orderKey, String sortOrder) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + orderKey+ " " + sortOrder;
        String[] args = new String[1];
        args[0] = orderKey;

        ArrayList<SyukkoInvoiceSearchInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                SyukkoInvoiceSearchInfo item = new SyukkoInvoiceSearchInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setCustomerName(cursor.getString(cursor.getColumnIndex(C_TOKUISAKI_NAME)));
                item.setDestination(cursor.getString(cursor.getColumnIndex(C_SHIMUKECHI)));
                item.setListReplyDesiredDate(cursor.getString(cursor.getColumnIndex(C_LIST_HENSIN_KIBOBI)));
                item.setIssueDate(cursor.getString(cursor.getColumnIndex(C_SYUKKA_DATE)));
                item.setLineCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_GYOSU))));
                item.setTransportTemprature(cursor.getString(cursor.getColumnIndex(C_HOREI)));
                item.setDangerousGoods(cursor.getString(cursor.getColumnIndex(C_KIKENHIN)));
                item.setRemarks(cursor.getString(cursor.getColumnIndex(C_BIKOU)));
                item.setPickingHoldFlag(cursor.getString(cursor.getColumnIndex(C_KONPO_HOLD_FLG)));
                result.add(item);
            }
            return result;
        }
    }

    public ArrayList<PickedInvoiceSearchInfo> getSyukkoShijiSortListPicked(SQLiteDatabase db, String orderKey, String sortOrder) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + orderKey+ " " + sortOrder;
        String[] args = new String[1];
        args[0] = orderKey;

        ArrayList<PickedInvoiceSearchInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                PickedInvoiceSearchInfo item = new PickedInvoiceSearchInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setCustomerName(cursor.getString(cursor.getColumnIndex(C_TOKUISAKI_NAME)));
                item.setDestination(cursor.getString(cursor.getColumnIndex(C_SHIMUKECHI)));
                item.setListReplyDesiredDate(cursor.getString(cursor.getColumnIndex(C_LIST_HENSIN_KIBOBI)));
                item.setIssueDate(cursor.getString(cursor.getColumnIndex(C_SYUKKA_DATE)));
                item.setLineCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_GYOSU))));
                item.setTransportTemprature(cursor.getString(cursor.getColumnIndex(C_HOREI)));
                item.setDangerousGoods(cursor.getString(cursor.getColumnIndex(C_KIKENHIN)));
                item.setRemarks(cursor.getString(cursor.getColumnIndex(C_BIKOU)));
                item.setKonpoHoldFlag(cursor.getString(cursor.getColumnIndex(C_KONPO_HOLD_FLG)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * Invoice番号指定出庫指示ヘッダ情報取得
     * @param db
     * @param invoiceNo
     * @return
     */
    public PickedInvoiceSearchInfo getSyukkoShijiDataByInvoiceNo(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? ";
        String[] args = new String[1];
        args[0] = invoiceNo;

        PickedInvoiceSearchInfo result;
        try (Cursor cursor = db.rawQuery(sql, args)) {
            cursor.moveToNext();
            result = new PickedInvoiceSearchInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
            result.setInvoiceNo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
            result.setCustomerName(cursor.getString(cursor.getColumnIndex(C_TOKUISAKI_NAME)));
            result.setDestination(cursor.getString(cursor.getColumnIndex(C_SHIMUKECHI)));
            result.setListReplyDesiredDate(cursor.getString(cursor.getColumnIndex(C_LIST_HENSIN_KIBOBI)));
            result.setIssueDate(cursor.getString(cursor.getColumnIndex(C_SYUKKA_DATE)));
            result.setLineCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_GYOSU))));
            result.setTransportTemprature(cursor.getString(cursor.getColumnIndex(C_HOREI)));
            result.setDangerousGoods(cursor.getString(cursor.getColumnIndex(C_KIKENHIN)));
            result.setRemarks(cursor.getString(cursor.getColumnIndex(C_BIKOU)));
            result.setKonpoHoldFlag(cursor.getString(cursor.getColumnIndex(C_KONPO_HOLD_FLG)));
            return result;
        }
    }
}
