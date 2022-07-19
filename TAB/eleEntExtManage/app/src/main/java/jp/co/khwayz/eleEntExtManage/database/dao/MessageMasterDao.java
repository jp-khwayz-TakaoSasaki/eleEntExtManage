package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.http.response.MsgMasterResponse;

public class MessageMasterDao {
    public static final String TABLE_NAME = "M_MESSAGE";
    public static final String C_MSG_NO = "MSG_NO";
    public static final String C_MSG = "MSG";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + C_MSG_NO + " TEXT NOT NULL," // メッセージ番号(区分 + 連番)
                    + C_MSG + " TEXT," // メッセージ
                    + "PRIMARY KEY("
                    + C_MSG_NO
                    + "));";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public void createTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    public void upgradeTable(SQLiteDatabase db) {
        db.execSQL(SQL_DROP);
        createTable(db);
    }

    public void bulkInsert(SQLiteDatabase db, ArrayList<MsgMasterResponse.ResponseBody> list) {
        try {
            // トランザクション開始
            db.beginTransaction();
            // 全件削除
            db.execSQL("delete from " + TABLE_NAME);
            /*
             * データ登録
             */
            for (MsgMasterResponse.ResponseBody item : list) {
                ContentValues values = new ContentValues();
                String msgNo = item.getMessageKbn() + item.getNum();
                values.put(C_MSG_NO, msgNo);
                values.put(C_MSG, item.getMessage());
                db.insert(TABLE_NAME, null, values);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public String getMessage(SQLiteDatabase db, String msgNo) {
        String sql = "SELECT MSG FROM " + TABLE_NAME + " WHERE MSG_NO = ?";
        String[] args = new String[1];
        args[0] = msgNo;
        try (Cursor cursor = db.rawQuery(sql, args)) {
            if (cursor.moveToNext()) {
                return cursor.getString(0);
            }
            return "";
        }
    }
}
