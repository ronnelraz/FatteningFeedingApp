package com.ronnelrazo.fatteningfeedingapp.localDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper  {

    private Context context;
    private static final String DATABASE_NAME = "Fattening_app";
    private static final int DATABASE_VERSION = 1;

    //tables
    public PC_FAT_MAS_USER mas_user = new PC_FAT_MAS_USER();
    public PC_FAT_MAS_AUTHORIZE mas_authorize = new PC_FAT_MAS_AUTHORIZE();
    public FR_MS_TRN_FEED mas_feed_trn = new FR_MS_TRN_FEED();


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // add this line to initialize your context.

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String user = "CREATE TABLE " + mas_user.PC_FAT_MAS_USER +" (" +
                  mas_user.AD_USER + " TEXT PRIMARY KEY, " +
                  mas_user.PASSWORD + " TEXT, " +
                  mas_user.ACTIVE_FLAG + " TEXT)";
        db.execSQL(user);


        String authorize = "CREATE TABLE " + mas_authorize.PC_FAT_MAS_AUTHORIZED +" (" +
                mas_authorize.AD_USER + " TEXT, " +
                mas_authorize.ORG_CODE + " TEXT, " +
                mas_authorize.FARM_CODE + " TEXT," +
                "PRIMARY KEY(AD_USER, ORG_CODE)  ); ";
        db.execSQL(authorize);

        String trn_feed = "CREATE TABLE " + mas_feed_trn.FR_MS_TRN_FEED +
                " (" + mas_feed_trn.ORG_CODE + " TEXT, " +
                mas_feed_trn.PERIOD + " TEXT, " +
                mas_feed_trn.PROJECT_CODE + " TEXT, " +
                mas_feed_trn.FARM_ORG + " TEXT, " +
                mas_feed_trn.TRANSACTION_DATE + " TEXT, " +
                mas_feed_trn.DOCUMENT_TYPE + " TEXT, " +
                mas_feed_trn.DOCUMENT_NO + " TEXT," +
                mas_feed_trn.DOCUMENT_EXT + " TEXT," +
                mas_feed_trn.PROJECT_STOCK_ORG + " TEXT, " +
                mas_feed_trn.STOCK_ORG + " TEXT, " +
                mas_feed_trn.PRODUCT_CODE + " TEXT, " +
                mas_feed_trn.PRODUCT_SPEC + " TEXT, " +
                mas_feed_trn.GRADE_CODE + " TEXT," +
                mas_feed_trn.LOT_NO + " TEXT," +
                mas_feed_trn.TRANSACTION_TYPE + " TEXT," +
                mas_feed_trn.TRANSACTION_CODE + " TEXT," +
                mas_feed_trn.QTY + " TEXT," +
                mas_feed_trn.WGH + " TEXT, " +
                mas_feed_trn.VAL + " TEXT," +
                mas_feed_trn.COST + " TEXT," +
                mas_feed_trn.REF_DOCUMENT_TYPE + " TEXT," +
                mas_feed_trn.REF_DOCUMENT_NO + " TEXT," +
                mas_feed_trn.REF_DOCUMENT_EXT + " TEXT," +
                mas_feed_trn.REF_ORG_CODE + " TEXT," +
                mas_feed_trn.REF_PROJECT_CODE + " TEXT," +
                mas_feed_trn.REF_PLANT_CODE + " TEXT," +
                mas_feed_trn.REF_FARM_ORG + " TEXT," +
                mas_feed_trn.USER_CREATE + " TEXT," +
                mas_feed_trn.CREATE_DATE + " TEXT, " +
                mas_feed_trn.REF_DOCUMENT_TYPE_OR + " TEXT," +
                mas_feed_trn.REF_DOCUMENT_NO_OR + " TEXT," +
                mas_feed_trn.REF_DOCUMENT_DATE_OR + " TEXT," +
                mas_feed_trn.CANCEL_FLAG + " TEXT," +
                mas_feed_trn.CANCEL_DATE + " TEXT," +
                mas_feed_trn.LAST_USER_ID + " TEXT," +
                mas_feed_trn.LAST_UPDATE_DATE + " TEXT," +
                mas_feed_trn.PRODUCTION_DATE + " TEXT," +
                "PRIMARY KEY(ORG_CODE, PERIOD,PROJECT_CODE,FARM_ORG,TRANSACTION_DATE,DOCUMENT_TYPE,DOCUMENT_NO,DOCUMENT_EXT)  ); ";
        db.execSQL(trn_feed);


        String create_fr_farm_org = "CREATE TABLE FR_FARM_ORG (" +
                "ORG_CODE" + " TEXT, "+
                "FARM_ORG" + " TEXT, "+
                "NAME_LOC" + " TEXT, "+
                "NAME_ENG" + " TEXT, "+
                "FARM" + " TEXT, "+
                "UNIT" + " TEXT, "+
                "HOUSE" + " TEXT, "+
                "LOCATION" + " TEXT, "+
                "FLOCK" + " TEXT, "+
                "GRP" + " TEXT, "+
                "CANCEL_FLAG" + " TEXT, "+
                "CANCEL_DATE" + " TEXT, "+
                "USER_CREATE" + " TEXT, "+
                "CREATE_DATE" + " TEXT, "+
                "LAST_USER_ID" + " TEXT, "+
                "LAST_UPDATE_DATE" + " TEXT, "+
                "ACTIVE_FLG" + " TEXT, "+
                "PROJECT" + " TEXT, "+
                "PARENT_FARM_ORG" + " TEXT, "+
                "MANAGEMENT_FLG" + " TEXT, "+
                "REC_FD_FLG" + " TEXT, "+
                "REC_MD_FLG" + " TEXT, "+
                "REC_BD_FLG" + " TEXT, "+
                "ISS_FD_FLG" + " TEXT, "+
                "ISS_MD_FLG" + " TEXT, "+
                "ISS_BD_FLG" + " TEXT, "+
                "REC_FD_WAY_TYPE" + " TEXT, "+
                "REC_MD_WAY_TYPE" + " TEXT, "+
                "REC_BD_WAY_TYPE" + " TEXT, "+
                "ISS_FD_WAY_TYPE" + " TEXT, "+
                "ISS_MD_WAY_TYPE" + " TEXT, "+
                "ISS_BD_WAY_TYPE" + " TEXT, "+
                "PARENT_FARM_ORG_FD" + " TEXT, "+
                "PARENT_FARM_ORG_MD" + " TEXT, "+
                "REC_AI_FLG" + " TEXT, "+
                "ISS_AI_FLG" + " TEXT, "+
                "REC_AI_WAY_TYPE" + " TEXT, "+
                "ISS_AI_WAY_TYPE" + " TEXT, "+
                "PARENT_FARM_ORG_AI" + " TEXT, "+
                "REC_MK_FLG" + " TEXT, "+
                "ISS_MK_FLG" + " TEXT, "+
                "REC_MK_WAY_TYPE" + " TEXT, "+
                "ISS_MK_WAY_TYPE" + " TEXT, "+
                "PARENT_FARM_ORG_MK" + " TEXT, "+
                "REF_FARM" + " TEXT, "+
                "CHAT_FARM_NAME" + " TEXT, "+
                "FLOCK_IN" + " TEXT, "+
                "CUSTOMER_STD_CODE" + " TEXT, "+
                "PRIMARY KEY(ORG_CODE, FARM_ORG)  ); ";
        db.execSQL(create_fr_farm_org);


        String FR_MS_SWINE_MATERIAL = "CREATE TABLE FR_MS_SWINE_MATERIAL (" +
                "ORG_CODE TEXT, " +
                "PERIOD TEXT, " +
                "PROJECT_CODE TEXT, " +
                "FARM_ORG TEXT, " +
                "STOCK_TYPE TEXT, " +
                "TRANSACTION_DATE TEXT, " +
                "DOCUMENT_TYPE TEXT, " +
                "DOCUMENT_NO TEXT, " +
                "DOCUMENT_EXT TEXT, " +
                "REF_DOCUMENT_NO TEXT, " +
                "PROJECT_STOCK_ORG TEXT, " +
                "STOCK_ORG TEXT, " +
                "PRODUCT_CODE TEXT, " +
                "PRODUCT_SPEC TEXT, " +
                "GRADE_CODE TEXT, " +
                "LOT_NO TEXT, " +
                "TRANSACTION_TYPE TEXT, " +
                "TRANSACTION_CODE TEXT, " +
                "QTY TEXT, " +
                "WGH TEXT, " +
                "VAL TEXT, " +
                "COST TEXT, " +
                "CANCEL_FLAG TEXT, " +
                "CANCEL_DATE TEXT, " +
                "USER_CREATE TEXT, " +
                "CREATE_DATE TEXT, " +
                "LAST_USER_ID TEXT, " +
                "LAST_UPDATE_DATE TEXT, " +
                "LAST_FUNCTION TEXT, " +
                "FARM_ORG_LOCATION TEXT, " +
                "DES_FARM_ORG TEXT, " +
                "UNIT TEXT, " +
                "ADJ TEXT, " +
                "LICENSE_NO TEXT, " +
                "REF_PRODUCT_CODE TEXT, " +
                "EXPIRE_DATE TEXT, " +
                "PRINT_NO TEXT, " +
                "PRODUCTION_DATE TEXT, " +
                "PRIMARY KEY(ORG_CODE, PERIOD,PROJECT_CODE,FARM_ORG,STOCK_TYPE,TRANSACTION_DATE,DOCUMENT_TYPE,DOCUMENT_NO,DOCUMENT_EXT)  ); ";
        db.execSQL(FR_MS_SWINE_MATERIAL);


        String FR_MAS_CLOSE_PERIOD = "CREATE TABLE FR_MAS_CLOSE_PERIOD (" +
                "ACTIVE_PERIOD TEXT, " +
                "PRIMARY KEY(ACTIVE_PERIOD)  ); ";
        db.execSQL(FR_MAS_CLOSE_PERIOD);


        String FDSTOCKBALANCE = "CREATE TABLE FEED_STOCK_BALANCE (" +
                "ORG_CODE TEXT, " +
                "FARM TEXT, " +
                "FARM_ORG TEXT, " +
                "PRODUCT_CODE TEXT, " +
                "PRODUCT_NAME TEXT, " +
                "PRODUCT_SPEC TEXT, " +
                "LOT_NO TEXT, " +
                "USE_CONDITION TEXT, " +
                "BF_QTY TEXT, " +
                "RECEIVED_DATE TEXT, " +
                "ISSUED_QTY TEXT, " +
                "BALANCE_QTY TEXT, " +
                "BF_WGH TEXT, " +
                "RECEIVED_WGH TEXT, " +
                "ISSUED_WGH TEXT, " +
                "BALANCE_WGH TEXT, " +
                "PACKING_SIZE TEXT ); ";
        db.execSQL(FDSTOCKBALANCE);


        String FR_MAS_FARM_INFORMATION = "CREATE TABLE FR_MAS_FARM_INFORMATION (" +
                "ORG_CODE TEXT, " +
                "FARM TEXT, " +
                "NAME_LOC TEXT, " +
                "NAME_ENG TEXT, " +
                "PROJECT TEXT, " +
                "FARM_TYPE TEXT, " +
                "FARM_MANAGER TEXT, " +
                "AREA_FEEDING TEXT, " +
                "AREA_SALE TEXT, " +
                "HUSBANDRY TEXT, " +
                "OPERATION TEXT, " +
                "FEED_CONDITION TEXT, " +
                "MEDICINE_CONDITION TEXT, " +
                "BREEDER_CONDITION TEXT, " +
                "CANCEL_FLAG TEXT, " +
                "CANCEL_DATE TEXT, " +
                "USER_CREATE TEXT, " +
                "CREATE_DATE TEXT, " +
                "LAST_USER_ID TEXT, " +
                "LAST_UPDATE_DATE TEXT, " +
                "REF_FARM TEXT, " +
                "AUTO_TRACKING TEXT, " +
                "FARM_OLD TEXT, " +
                "BENEFIT_CONDITION TEXT, " +
                "TAX_CONDITION TEXT, " +
                "HATCHERY_CONDITION TEXT, " +
                "ITF_SMARTSOFT TEXT, " +
                "AIR_SYSTEM TEXT, " +
                "WATER_SYSTEM TEXT, " +
                "HOLDING_DAY TEXT, " +
                "PARENT_FARM TEXT, " +
                "BANK_ACCOUNT TEXT, " +
                "LICENSE_FARM TEXT, " +
                "TELEPHONE_NO TEXT, " +
                "NAME_LOC_LAB TEXT, " +
                "NAME_ENG_LAB TEXT, " +
                "CHAT_FARM_NAME TEXT, " +
                "CUSTOMER_STD_CODE TEXT, " +
                "PRIMARY KEY(ORG_CODE, FARM)  ); ";
        db.execSQL(FR_MAS_FARM_INFORMATION);


        String log_device = "CREATE TABLE DEVICE_LOG (" +
                "rowID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Download_start_date TEXT, " +
                "Download_finish_date TEXT, " +
                "Download_table_name TEXT, " +
                "Total_data TEXT, " +
                "Total_data_download TEXT, " +
                "AD_USER TEXT); ";
        db.execSQL(log_device);




    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + mas_user.PC_FAT_MAS_USER );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + mas_authorize.PC_FAT_MAS_AUTHORIZED );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + mas_feed_trn.FR_MS_TRN_FEED );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS FR_FARM_ORG");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS FR_MS_SWINE_MATERIAL");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS FR_MAS_CLOSE_PERIOD");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS FEED_STOCK_BALANCE");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS  FR_MAS_FARM_INFORMATION");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS  DEVICE_LOG");
        onCreate(db);








    }



}
