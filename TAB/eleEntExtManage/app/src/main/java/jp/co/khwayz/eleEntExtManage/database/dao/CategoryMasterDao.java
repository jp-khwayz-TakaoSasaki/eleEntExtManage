package jp.co.khwayz.eleEntExtManage.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.common.models.CategoryInfo;

public class CategoryMasterDao {
    public static final String TABLE_NAME = "M_KUBUN";
    public static final String C_KBN = "KBN";
    public static final String C_ELEMENT = "ELEMENT";
    public static final String C_ELEMENT_NM = "ELEMENT_NM";
    public static final String C_ELEMENT_MEMO = "ELEMENT_MEMO";
    public static final String C_SORT_ORDER = "SORT_ORDER";

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + C_KBN + " TEXT NOT NULL," // 区分
            + C_ELEMENT + " TEXT," // 要素
            + C_ELEMENT_NM + " TEXT," // 要素名
            + C_ELEMENT_MEMO + " TEXT," // 要素備考
            + C_SORT_ORDER + " INTEGER," // ソート順
            + "PRIMARY KEY("
            + C_KBN + ","
            + C_ELEMENT
            + "));";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public void createTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    public void upgradeTable(SQLiteDatabase db) {
        db.execSQL(SQL_DROP);
        createTable(db);
    }

    public void bulkInsert(SQLiteDatabase db, ArrayList<CategoryInfo> list) {
        try {
            // トランザクション開始
            db.beginTransaction();
            // 全件削除
            db.execSQL("delete from " + TABLE_NAME);
            /*
             * データ登録
             */
            for (CategoryInfo item : list) {
                ContentValues values = new ContentValues();
                values.put(C_KBN, item.getCategory());
                values.put(C_ELEMENT, item.getElement());
                values.put(C_ELEMENT_NM, item.getElementName());
                values.put(C_ELEMENT_MEMO, item.getElementMemo());
                values.put(C_SORT_ORDER, item.getSortOrder());
                db.insert(TABLE_NAME, null, values);
            }
            // トランザクションコミット
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // 区分マスタ―取得
    public ArrayList<CategoryInfo> getCategoryList(SQLiteDatabase db, String category) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE KBN = ? ORDER BY " + C_SORT_ORDER;
        String[] args = new String[1];
        args[0] = category;

        ArrayList<CategoryInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                String kbn = cursor.getString(cursor.getColumnIndex(C_KBN));
                String element = cursor.getString(cursor.getColumnIndex(C_ELEMENT));
                CategoryInfo item = new CategoryInfo(kbn, element);
                item.setElementName(cursor.getString(cursor.getColumnIndex(C_ELEMENT_NM)));
                item.setElementMemo(cursor.getString(cursor.getColumnIndex(C_ELEMENT_MEMO)));
                item.setSortOrder(cursor.getInt(cursor.getColumnIndex(C_SORT_ORDER)));
                result.add(item);
            }
            return result;
        }
    }

    public ArrayList<CategoryInfo> getTransportListDistinct(SQLiteDatabase db) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " GROUP BY "
                + C_ELEMENT_NM + " HAVING KBN = ? " + " ORDER BY " + C_SORT_ORDER;
        String[] args = new String[1];
        args[0] = Constants.KBN_ESHIMUKECHI;

        ArrayList<CategoryInfo> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                String kbn = cursor.getString(cursor.getColumnIndex(C_KBN));
                String element = cursor.getString(cursor.getColumnIndex(C_ELEMENT));
                CategoryInfo item = new CategoryInfo(kbn, element);
                item.setElementName(cursor.getString(cursor.getColumnIndex(C_ELEMENT_NM)));
                item.setElementMemo(cursor.getString(cursor.getColumnIndex(C_ELEMENT_MEMO)));
                item.setSortOrder(cursor.getInt(cursor.getColumnIndex(C_SORT_ORDER)));
                result.add(item);
            }
            return result;
        }
    }

    public CategoryInfo getCategory(SQLiteDatabase db, String category, String element) {
        final String sql = "SELECT * FROM " + TABLE_NAME + " WHERE KBN = ? AND ELEMENT = ? ORDER BY " + C_SORT_ORDER;
        String[] args = new String[2];
        args[0] = category;
        args[1] = element;
        CategoryInfo result = new CategoryInfo(category, element);
        try (Cursor cursor = db.rawQuery(sql, args)) {
            if (cursor.moveToNext()) {
                result.setCategory(cursor.getString(cursor.getColumnIndex(C_KBN)));
                result.setElement(cursor.getString(cursor.getColumnIndex(C_ELEMENT)));
                result.setElementName(cursor.getString(cursor.getColumnIndex(C_ELEMENT_NM)));
                result.setElementMemo(cursor.getString(cursor.getColumnIndex(C_ELEMENT_MEMO)));
                result.setSortOrder(cursor.getInt(cursor.getColumnIndex(C_SORT_ORDER)));
            }
            return result;
        }
    }

    /**
     * 仕向地用Spinner配列取得
     * @return 要素名のみの区分リスト
     */
    public ArrayList<CategoryInfo> getDestinationSpinnerArray(SQLiteDatabase db) {
        // SQL
        final String sql = "SELECT DISTINCT " + C_ELEMENT_NM + " FROM " + TABLE_NAME + " WHERE KBN = ? ORDER BY " + C_SORT_ORDER;
        String[] args = new String[1];
        args[0] = Constants.KBN_ESHIMUKECHI;
        // 戻り値
        ArrayList<CategoryInfo> result = new ArrayList<>();
        // 空白行を追加しておく
        CategoryInfo item = new CategoryInfo("", "");
        result.add(item);

        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                item = new CategoryInfo("", "");
                item.setElementName(cursor.getString(cursor.getColumnIndex(C_ELEMENT_NM)));
                result.add(item);
            }
            return result;
        }

    }
}
