package jp.co.khwayz.eleEntExtManage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import jp.co.khwayz.eleEntExtManage.common.Constants;
import jp.co.khwayz.eleEntExtManage.database.dao.CategoryMasterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoInnerDao;
import jp.co.khwayz.eleEntExtManage.database.dao.KonpoOuterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.MessageMasterDao;
import jp.co.khwayz.eleEntExtManage.database.dao.OverpackKonpoShizaiDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiDetailDao;
import jp.co.khwayz.eleEntExtManage.database.dao.SyukkoShijiHeaderDao;

public class RfidDataBaseOpenHelper extends SQLiteOpenHelper {

    static final private int VERSION = 1;


//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";
//    public static final String C_ = "";

    public RfidDataBaseOpenHelper(@Nullable Context context) {
        super(context, Constants.LOCAL_DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* メッセージマスタ作成 */
        new MessageMasterDao().createTable(db);
        /* 区分マスタ作成 */
        new CategoryMasterDao().createTable(db);
        /* 出庫指示ヘッダ作成 */
        new SyukkoShijiHeaderDao().createTable(db);
        /* 出庫指示明細作成 */
        new SyukkoShijiDetailDao().createTable(db);
        /* 梱包アウター作成 */
        new KonpoOuterDao().createTable(db);
        /* 梱包インナー作成 */
        new KonpoInnerDao().createTable(db);
        /* オーバーパック梱包資材作成 */
        new OverpackKonpoShizaiDao().createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* メッセージマスタ再更新 */
        new MessageMasterDao().upgradeTable(db);
        /* 区分マスタ再更新 */
        new CategoryMasterDao().upgradeTable(db);
        /* 出庫指示ヘッダ再更新 */
        new SyukkoShijiHeaderDao().upgradeTable(db);
        /* 出庫指示明細再更新 */
        new SyukkoShijiDetailDao().upgradeTable(db);
        /* 梱包アウター更新 */
        new KonpoOuterDao().upgradeTable(db);
        /* 梱包インナー更新 */
        new KonpoInnerDao().upgradeTable(db);
        /* オーバーパック梱包資材更新 */
        new OverpackKonpoShizaiDao().upgradeTable(db);
    }
}
