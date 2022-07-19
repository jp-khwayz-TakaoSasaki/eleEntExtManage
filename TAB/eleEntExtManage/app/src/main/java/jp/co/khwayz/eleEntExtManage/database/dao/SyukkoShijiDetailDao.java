package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.khwayz.eleEntExtManage.PackingScanInfo;
import jp.co.khwayz.eleEntExtManage.common.models.IssueTagInfo;
import jp.co.khwayz.eleEntExtManage.common.models.PackingCancelInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoShijiKeyInfo;
import jp.co.khwayz.eleEntExtManage.common.models.SyukkoInvoiceDetailInfo;
import jp.co.khwayz.eleEntExtManage.packing.OverPackListInfo;

public class SyukkoShijiDetailDao {
    public static final String TABLE_NAME = "T_SYUKKOSIJI_DETAIL";
    public static final String C_INVOICE_NO = "INVOICE_NO";
    public static final String C_RENBAN = "RENBAN";
    public static final String C_LINE_NO = "LINE_NO";
    public static final String C_JUCHU_NO = "JUCHU_NO";
    public static final String C_HACHU_NO = "HACHU_NO";
    public static final String C_HACHU_EDA = "HACHU_EDA";
    public static final String C_TOKUISAKI_HACHU_NO = "TOKUISAKI_HACHU_NO";
    public static final String C_HINMOKU_CODE = "HINMOKU_CODE";
    public static final String C_HINMOKU_NAME = "HINMOKU_NAME";
    public static final String C_MISYUKKA_SU = "MISYUKKA_SU";
    public static final String C_SYUKKA_SU_SUM = "SYUKKA_SU_SUM";
    public static final String C_SYUKKA_SU = "SYUKKA_SU";
    public static final String C_SYUKKA_TANI = "SYUKKA_TANI";
    public static final String C_TANABAN = "TANABAN";
    public static final String C_ZAIKOHIN_FLG = "ZAIKOHIN_FLG";
    public static final String C_PICZUMI_FLG = "PICZUMI_FLG";
    public static final String C_KONPOZUMI_FLG = "KONPOZUMI_FLG";
    public static final String C_OVERPACK_NO = "OVERPACK_NO";
    public static final String C_CS_NUMBER = "CS_NUMBER";
    public static final String C_NYUKO_NISUGATA = "NYUKO_NISUGATA";
    public static final String C_COMBINE_NO = "COMBINE_NO";
    public static final String C_NYUKO_DATE = "NYUKO_DATE";
    public static final String C_COM_KONPOSIJI = "COM_KONPOSIJI";
    public static final String C_BASIC_KONPO_HOHO = "BASIC_KONPO_HOHO";
    public static final String C_BIKO = "BIKO";
    public static final String C_HOREIZAI = "HOREIZAI";
    public static final String C_UPDATE_FLAG = "UPDATE_FLAG";
    public static final String C_SINGLE_PACK_FLAG = "SINGLE_PACK_FLAG";
    public static final String C_REGIST_DATE = "REGIST_DATE";
    public static final String C_UPDATE_DATE = "UPDATE_DATE";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_INVOICE_NO + " TEXT NOT NULL," // Invoice番号
            + C_RENBAN + " INTEGER NOT NULL,"
            + C_LINE_NO + " INTEGER NOT NULL,"
            + C_JUCHU_NO + " TEXT,"
            + C_HACHU_NO + " TEXT,"
            + C_HACHU_EDA + " INTEGER,"
            + C_TOKUISAKI_HACHU_NO + " INTEGER,"
            + C_HINMOKU_CODE + " TEXT,"
            + C_HINMOKU_NAME + " TEXT,"
            + C_MISYUKKA_SU + " REAL,"
            + C_SYUKKA_SU_SUM + " REAL,"
            + C_SYUKKA_SU + " REAL,"
            + C_SYUKKA_TANI + " TEXT,"
            + C_TANABAN + " TEXT,"
            + C_ZAIKOHIN_FLG + " TEXT,"
            + C_PICZUMI_FLG + " TEXT,"
            + C_KONPOZUMI_FLG + " TEXT,"
            + C_OVERPACK_NO + " INTEGER,"
            + C_CS_NUMBER + " INTEGER,"
            + C_NYUKO_NISUGATA + " TEXT,"
            + C_COMBINE_NO + " INTEGER,"
            + C_NYUKO_DATE + " TEXT,"
            + C_COM_KONPOSIJI + " TEXT,"
            + C_BASIC_KONPO_HOHO + " TEXT,"
            + C_BIKO + " TEXT,"
            + C_HOREIZAI + " TEXT,"
            + C_UPDATE_FLAG + " TEXT,"
            + C_SINGLE_PACK_FLAG + " TEXT,"
            + C_REGIST_DATE + " TEXT," // 登録日時
            + C_UPDATE_DATE + " TEXT," // 更新日時
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
     * 出庫予定Invoice検索用
     * @param db
     * @param list
     */
    public void bulkInsert(SQLiteDatabase db, ArrayList<SyukkoInvoiceDetailInfo> list) {
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
            for (SyukkoInvoiceDetailInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_RENBAN, item.getRenban());
                values.put(C_LINE_NO, item.getLineNo());
                values.put(C_JUCHU_NO, item.getReceiveOrderNo());
                values.put(C_HACHU_NO, item.getPlaceOrderNo());
                values.put(C_HACHU_EDA, item.getBranchNo());
                values.put(C_HINMOKU_CODE, item.getItemCode());
                values.put(C_HINMOKU_NAME, item.getItemName());
                values.put(C_SYUKKA_SU, item.getIssueQuantity());
                values.put(C_SYUKKA_SU_SUM, item.getIssueQuantitySummery());
                values.put(C_SYUKKA_TANI, item.getUnit());
                values.put(C_MISYUKKA_SU, item.getStockQuantity());
                values.put(C_PICZUMI_FLG, item.getPickingFlag());
                values.put(C_NYUKO_NISUGATA, item.getPackingType());
                values.put(C_TANABAN, item.getLocationNo());
                values.put(C_UPDATE_FLAG, "0");
                values.put(C_SINGLE_PACK_FLAG, "0");
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
     * 荷札情報受信用
     * @param db
     * @param list
     */
    public void bulkInsertTagInfoGet(SQLiteDatabase db, ArrayList<IssueTagInfo> list) {
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
            for (IssueTagInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_INVOICE_NO, item.getInvoiceNo());
                values.put(C_RENBAN, item.getRenban());
                values.put(C_LINE_NO, item.getLineNo());
                values.put(C_HACHU_NO, item.getPlaceOrderNo());
                values.put(C_HACHU_EDA, item.getBranchNo());
                values.put(C_JUCHU_NO, item.getReceiveOrderNo());
                values.put(C_HINMOKU_CODE, item.getItemCode());
                values.put(C_HINMOKU_NAME, item.getItemName());
                values.put(C_MISYUKKA_SU, item.getZaikoSu());
                values.put(C_SYUKKA_SU, item.getSyukkaSu());
                values.put(C_SYUKKA_SU_SUM, item.getSyukkaSuSum());
                values.put(C_SYUKKA_TANI, item.getSyukkaTani());
                values.put(C_KONPOZUMI_FLG, item.getKonpozumiFlag());
                values.put(C_SINGLE_PACK_FLAG, "0");
                values.put(C_CS_NUMBER, item.getCsNumber());
                values.put(C_OVERPACK_NO, item.getOverPackNo());
                values.put(C_NYUKO_NISUGATA, item.getPackingType());
                values.put(C_NYUKO_DATE, item.getReceiptDate());
                values.put(C_COMBINE_NO, item.getCombineNo());
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
     * ピッキング済明細取得
     * @param db
     * @return
     */
    public ArrayList<SyukkoInvoiceDetailInfo> getSyukkoShijiListPicked(SQLiteDatabase db) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE PICZUMI_FLG = ?";
        String[] args = new String[1];
        args[0] = "1";
        ArrayList<SyukkoInvoiceDetailInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                SyukkoInvoiceDetailInfo item = new SyukkoInvoiceDetailInfo(cursor.getString(cursor.getColumnIndex(C_INVOICE_NO)));
                item.setRenban(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_RENBAN))));
                item.setLineNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_LINE_NO))));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * 明細取得
     * @param db
     * @return
     */
    public ArrayList<IssueTagInfo> getSyukkoShijiList(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? ";
        String[] args = new String[1];
        args[0] = invoiceNo;
        ArrayList<IssueTagInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                IssueTagInfo item = new IssueTagInfo(invoiceNo
                        ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_RENBAN)))
                        ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_LINE_NO))));
                item.setCsNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_CS_NUMBER))));
                item.setOverPackNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_OVERPACK_NO))));
                item.setKonpozumiFlag(cursor.getString(cursor.getColumnIndex(C_KONPOZUMI_FLG)));
                result.add(item);
            }
            return result;
        }
    }

    /**
     * 入庫日取得
     * @param db
     * @param invoiceNo
     * @param renban
     * @param lineNo
     * @return
     */
    public String getNyukoDate(SQLiteDatabase db, String invoiceNo, int renban, int lineNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND RENBAN = ? AND LINE_NO = ?";
        String[] args = new String[3];
        args[0] = invoiceNo;
        args[1] = String.valueOf(renban);
        args[2] = String.valueOf(lineNo);
        String result = "";
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(C_NYUKO_DATE));
                break;
            }
            return result;
        }
    }

    /**
     * オーバーパック情報指定明細取得
     * @param db
     * @param invoiceNo
     * @param list
     * @return
     */
    public ArrayList<OverPackListInfo> getSyukkoShijiListByOverPackInfo(SQLiteDatabase db, String invoiceNo
            ,ArrayList<SyukkoShijiKeyInfo> list) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND RENBAN = ? AND LINE_NO = ?";
        String[] args = new String[3];
        args[0] = invoiceNo;

        ArrayList<OverPackListInfo> result = new ArrayList<>();
        try {
            for(SyukkoShijiKeyInfo key : list){
                args[1]=String.valueOf(key.getRenban());
                args[2]=String.valueOf(key.getLineNo());
                Cursor cursor = db.rawQuery(sql, args);
                while (cursor.moveToNext()) {
                    OverPackListInfo item = new OverPackListInfo(
                            cursor.getString(cursor.getColumnIndex(C_OVERPACK_NO)).equals("0")
                                    ? "" :cursor.getString(cursor.getColumnIndex(C_OVERPACK_NO))
                            ,cursor.getString(cursor.getColumnIndex(C_HACHU_NO))
                            ,cursor.getString(cursor.getColumnIndex(C_HACHU_EDA))
                            ,cursor.getString(cursor.getColumnIndex(C_JUCHU_NO))
                            ,cursor.getString(cursor.getColumnIndex(C_COMBINE_NO))
                            ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_RENBAN)))
                            ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_LINE_NO))));
                    result.add(item);
                    break;
                }
            }
            return result;
        } finally {
        }
    }

    /**
     * 指定キーレコード取得
     * @param db
     * @param invoiceNo
     * @param selectList
     * @return
     */
    public ArrayList<IssueTagInfo> getSyukkoShijiListByKey (SQLiteDatabase db, String invoiceNo, ArrayList<SyukkoShijiKeyInfo> selectList){
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND RENBAN = ? AND LINE_NO = ? ORDER BY CS_NUMBER";
        String[] args = new String[3];
        args[0] = invoiceNo;
        ArrayList<IssueTagInfo> result = new ArrayList<>();
        try {
            for(SyukkoShijiKeyInfo key : selectList){
                args[1]=String.valueOf(key.getRenban());
                args[2]=String.valueOf(key.getLineNo());
                Cursor cursor = db.rawQuery(sql, args);
                while (cursor.moveToNext()) {
                    IssueTagInfo item = new IssueTagInfo(invoiceNo
                            ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_RENBAN)))
                            ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_LINE_NO))));
                    item.setCsNumber(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_CS_NUMBER))));
                    item.setOverPackNo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_OVERPACK_NO))));
                    item.setPlaceOrderNo(cursor.getString(cursor.getColumnIndex(C_HACHU_NO)));
                    item.setSyukkaSu(Double.parseDouble(cursor.getString(cursor.getColumnIndex(C_SYUKKA_SU))));
                    item.setReceiptDate(cursor.getString(cursor.getColumnIndex(C_NYUKO_DATE)));
                    result.add(item);

                    break;
                }
            }
            return result;
        } finally {
        }
    }

    /**
     * 同一明細情報取得
     * @param db
     * @param invoiceNo
     * @param placeOrderNo
     * @param issueQuantity
     * @return
     */
    public ArrayList<SyukkoShijiKeyInfo> getSameDetailInfo(SQLiteDatabase db, String invoiceNo, String placeOrderNo, String issueQuantity) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND HACHU_NO = ? AND SYUKKA_SU = ?";
        String[] args = new String[3];
        args[0] = invoiceNo;
        args[1] = placeOrderNo;
        args[2] = issueQuantity;

        ArrayList<SyukkoShijiKeyInfo> result = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(sql, args);
            while (cursor.moveToNext()) {
                SyukkoShijiKeyInfo item = new SyukkoShijiKeyInfo(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_RENBAN)))
                        , Integer.parseInt(cursor.getString(cursor.getColumnIndex(C_LINE_NO))));
                result.add(item);
            }
            return result;
        } finally {
        }
    }

    /**
     * ピッキング済フラグ更新
     * @param db
     * @param invoiceNo
     * @param lineNo
     * @param flgValue
     */
    public void updatePickingFlg(SQLiteDatabase db, String invoiceNo, String renban, String lineNo, String flgValue) {
        try {
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(C_PICZUMI_FLG, flgValue);
            String[] whereSql = {invoiceNo, renban, lineNo};
            db.update(TABLE_NAME, contentValues, "INVOICE_NO = ? AND RENBAN = ? AND lINE_NO = ?", whereSql);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * 個別梱包フラグ更新
     * @param db
     * @param invoiceNo
     * @param updateList
     */
    public void updateSinglePackingFlag(SQLiteDatabase db, String invoiceNo, ArrayList<SyukkoShijiKeyInfo> updateList){
        try {
            // トランザクション開始
            db.beginTransaction();
            /*
             * データ更新
             */
            for (SyukkoShijiKeyInfo item : updateList) {
                String sql = "UPDATE " + TABLE_NAME + " SET SINGLE_PACK_FLAG='1' WHERE "
                        + C_INVOICE_NO + " = '" + invoiceNo + "' AND "
                        + C_RENBAN + " = " + String.valueOf(item.getRenban()) + " AND "
                        + C_LINE_NO + " = " + String.valueOf(item.getLineNo());
                db.execSQL(sql);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 単独梱包フラグ更新
     * 　出庫指示明細テーブルのInvoice番号、行番号が、梱包インナーテーブルのInvoice番号、行番号と一致するレコードについて、
     * 　単独梱包フラグを「１：単独梱包情報あり」に更新する。
     * @param db
     */
    public void updateSinglePackingFlag(SQLiteDatabase db) {
        String sql = "update T_SYUKKOSIJI_DETAIL set SINGLE_PACK_FLAG='1' WHERE EXISTS (select * from T_KONPO_INNER t2 " +
                "where T_SYUKKOSIJI_DETAIL.INVOICE_NO = t2.INVOICE_NO and T_SYUKKOSIJI_DETAIL.RENBAN = t2.RENBAN " +
                "and T_SYUKKOSIJI_DETAIL.LINE_NO = t2.LINE_NO)";
        try {
            // トランザクション開始
            db.beginTransaction();
            db.execSQL(sql);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * 梱包済フラグ更新
     * @param db
     * @param invoiceNo
     * @param renban
     * @param lineNo
     * @param flgValue
     */
    public void updatePackingFlg(SQLiteDatabase db, String invoiceNo
            , String renban, String lineNo, String flgValue) {
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(C_KONPOZUMI_FLG, flgValue);
            String[] whereSql = {invoiceNo, renban, lineNo};
            db.update(TABLE_NAME, contentValues, "INVOICE_NO = ? AND RENBAN = ? AND lINE_NO = ?", whereSql);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * 梱包状態解除
     * @param db
     * @param list
     */
    public void cancelPackingState(SQLiteDatabase db, ArrayList<PackingCancelInfo> list) {
        try {
            // トランザクション開始
            db.beginTransaction();
            /*
             * データ更新
             */
            for (PackingCancelInfo item : list) {
                String sql = "update " + TABLE_NAME + " set KONPOZUMI_FLG=0, OVERPACK_NO=NULL, CS_NUMBER=NULL where "
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

    /**
     * オーバーパック番号解除
     * @param db
     * @param invoiceNo
     * @param overPackNo
     */
    public void releaseOverPackNo(SQLiteDatabase db, String invoiceNo, String overPackNo) {
        try {
            // トランザクション開始
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(C_OVERPACK_NO, 0);
            String[] whereSql = {invoiceNo, overPackNo};
            db.update(TABLE_NAME, contentValues, "INVOICE_NO = ? AND OVERPACK_NO = ?", whereSql);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * オーバーパック番号設定
     * @param db
     * @param list
     */
    public void registOverPackNo(SQLiteDatabase db, int overPackNo, String invoiceNo, ArrayList<OverPackListInfo> list) {
        try {
            // トランザクション開始
            db.beginTransaction();
            /*
             * データ更新
             */
            for (OverPackListInfo item : list) {
                String sql = "update " + TABLE_NAME + " set OVERPACK_NO=" + String.valueOf(overPackNo) + " where "
                        + C_INVOICE_NO + " = '" + invoiceNo + "' and "
                        + C_RENBAN + " = " + item.getRenBan() + " and "
                        + C_LINE_NO + " = " + item.getLineNo();
                db.execSQL(sql);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 梱包未完了明細有無チェック
     * @param db
     * @param invoiceNo
     * @return
     */
    public boolean isPackingFinished(SQLiteDatabase db, String invoiceNo) {
        String[] args = new String[2];
        args[0] = invoiceNo;
        args[1] = "1";
        long recodeCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME,"INVOICE_NO = ? and KONPOZUMI_FLG != ?",args);
        return(recodeCount > 0 ? true : false);
    }

    /**
     * Invoice番号指定梱包データ取得
     * @param db
     * @return
     */
    public ArrayList<PackingScanInfo> getPackingScanListByInvoice(SQLiteDatabase db, String invoiceNo) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE INVOICE_NO = ? AND KONPOZUMI_FLG = ?";
        String[] args = new String[2];
        args[0] = invoiceNo;
        args[1] = "0";
        int seqNo = 1;
        ArrayList<PackingScanInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                PackingScanInfo item = new PackingScanInfo(
                        seqNo,
                        cursor.getString(cursor.getColumnIndex(C_OVERPACK_NO)),
                        cursor.getString(cursor.getColumnIndex(C_HACHU_NO)),
                        cursor.getInt(cursor.getColumnIndex(C_HACHU_EDA)),
                        cursor.getString(cursor.getColumnIndex(C_JUCHU_NO)),
                        cursor.getString(cursor.getColumnIndex(C_HINMOKU_CODE)),
                        cursor.getString(cursor.getColumnIndex(C_HINMOKU_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(C_MISYUKKA_SU)),
                        cursor.getDouble(cursor.getColumnIndex(C_SYUKKA_SU)),
                        cursor.getDouble(cursor.getColumnIndex(C_SYUKKA_SU_SUM)),
                        cursor.getString(cursor.getColumnIndex(C_SYUKKA_TANI)),
                        cursor.getString(cursor.getColumnIndex(C_NYUKO_NISUGATA)),
                        cursor.getString(cursor.getColumnIndex(C_COMBINE_NO)),
                        cursor.getString(cursor.getColumnIndex(C_NYUKO_DATE)),
                        cursor.getInt(cursor.getColumnIndex(C_RENBAN)),
                        cursor.getInt(cursor.getColumnIndex(C_LINE_NO)),
                        cursor.getString(cursor.getColumnIndex(C_SINGLE_PACK_FLAG)),
                        cursor.getString(cursor.getColumnIndex(C_KONPOZUMI_FLG)));

                result.add(item);
                seqNo++;
            }
            return result;
        }
    }

    /**
     * Invoice番号、ケースマーク番号指定梱包データ取得
     * @param db
     * @return
     */
    public ArrayList<PackingScanInfo> getPackingScanListByCaseMark(SQLiteDatabase db, String invoiceNo, String displayCsNo) {
        final String sql = "select " +
                "t2.OVERPACK_NO, " +
                "t2.HACHU_NO, " +
                "t2.HACHU_EDA, " +
                "t2.JUCHU_NO, " +
                "t2.HINMOKU_CODE, " +
                "t2.HINMOKU_NAME, " +
                "t2.MISYUKKA_SU, " +
                "t2.SYUKKA_SU, " +
                "t2.SYUKKA_SU_SUM, " +
                "t2.SYUKKA_TANI, " +
                "t2.NYUKO_NISUGATA, " +
                "t2.COMBINE_NO, " +
                "t2.NYUKO_DATE, " +
                "t2.RENBAN, " +
                "t2.LINE_NO, " +
                "t2.SINGLE_PACK_FLAG, " +
                "t2.KONPOZUMI_FLG " +
                "FROM " +
                "( select INVOICE_NO, CS_NUMBER from T_KONPO_OUTER where INVOICE_NO = '" + invoiceNo +
                "' and HYOKI_CS_NUMBER = '" + displayCsNo +
                "') t1 inner join T_SYUKKOSIJI_DETAIL t2 on t1.INVOICE_NO = t2.INVOICE_NO and t1.CS_NUMBER = t2.CS_NUMBER";
        int seqNo = 1;
        ArrayList<PackingScanInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                PackingScanInfo item = new PackingScanInfo(
                        seqNo,
                        cursor.getString(cursor.getColumnIndex(C_OVERPACK_NO)),
                        cursor.getString(cursor.getColumnIndex(C_HACHU_NO)),
                        cursor.getInt(cursor.getColumnIndex(C_HACHU_EDA)),
                        cursor.getString(cursor.getColumnIndex(C_JUCHU_NO)),
                        cursor.getString(cursor.getColumnIndex(C_HINMOKU_CODE)),
                        cursor.getString(cursor.getColumnIndex(C_HINMOKU_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(C_MISYUKKA_SU)),
                        cursor.getDouble(cursor.getColumnIndex(C_SYUKKA_SU)),
                        cursor.getDouble(cursor.getColumnIndex(C_SYUKKA_SU_SUM)),
                        cursor.getString(cursor.getColumnIndex(C_SYUKKA_TANI)),
                        cursor.getString(cursor.getColumnIndex(C_NYUKO_NISUGATA)),
                        cursor.getString(cursor.getColumnIndex(C_COMBINE_NO)),
                        cursor.getString(cursor.getColumnIndex(C_NYUKO_DATE)),
                        cursor.getInt(cursor.getColumnIndex(C_RENBAN)),
                        cursor.getInt(cursor.getColumnIndex(C_LINE_NO)),
                        cursor.getString(cursor.getColumnIndex(C_SINGLE_PACK_FLAG)),
                        cursor.getString(cursor.getColumnIndex(C_KONPOZUMI_FLG)));
                result.add(item);
                seqNo++;
            }
            return result;
        }
    }

    /**
     * オーバーパック番号最大値取得
     * @param db
     * @return
     */
    public int getMaxOverPackNo(SQLiteDatabase db) {
        final String sql = "SELECT MAX(OVERPACK_NO) AS OVERPACK_NO FROM " + TABLE_NAME;
        int result = 0;
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(C_OVERPACK_NO));
                break;
            }
            return result + 1;
        }
    }
}
