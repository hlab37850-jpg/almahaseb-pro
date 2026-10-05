package info.aalmoghalis.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class AppDb extends SQLiteOpenHelper {
    private static final String NAME = "almahaseb.db";
    private static final int VERSION = 1;
    public AppDb(Context c) { super(c, NAME, null, VERSION); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS bills (id INTEGER PRIMARY KEY AUTOINCREMENT,tr_type INTEGER,date_ TEXT,amount REAL DEFAULT 0,d_amount REAL DEFAULT 0,cus_id INTEGER,tran_status INTEGER DEFAULT 0,bill_type INTEGER DEFAULT 0,bill_no2 TEXT,remarks TEXT,curr_id INTEGER,br_id INTEGER,cash_id INTEGER,is_back INTEGER DEFAULT 0,bill_no TEXT,param1 TEXT,online INTEGER DEFAULT 0,t_val REAL DEFAULT 0,tax_amount REAL DEFAULT 0,time_ TEXT,paid_amount REAL DEFAULT 0,user_id INTEGER,id2 INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS bill_transactions (id INTEGER PRIMARY KEY AUTOINCREMENT,bill_id INTEGER,item_id INTEGER,item_type_id INTEGER,qty REAL DEFAULT 0,qty_t REAL DEFAULT 0,cost_price REAL DEFAULT 0,sls_u_price REAL DEFAULT 0,curr_id INTEGER,d_amount REAL DEFAULT 0,remark TEXT,unit_id INTEGER,u_val REAL DEFAULT 0,base_unit INTEGER,qty_pr REAL DEFAULT 0,e_date TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS bills2 (id INTEGER PRIMARY KEY AUTOINCREMENT,tr_type INTEGER,date_ TEXT,amount REAL DEFAULT 0,remarks TEXT,curr_id INTEGER,br_id INTEGER,bill_no TEXT,is_back INTEGER DEFAULT 0,user_id INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS bill_transactions2 (id INTEGER PRIMARY KEY AUTOINCREMENT,bill_id INTEGER,item_id INTEGER,item_type_id INTEGER,qty REAL DEFAULT 0,qty_t REAL DEFAULT 0,cost_price REAL DEFAULT 0,curr_id INTEGER,d_amount REAL DEFAULT 0,remark TEXT,unit_id INTEGER,u_val REAL DEFAULT 0,base_unit INTEGER,qty_pr REAL DEFAULT 0,e_date TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS items_temp (item_id INTEGER,item_type_id INTEGER,qty REAL DEFAULT 0,qty_t REAL DEFAULT 0,price REAL DEFAULT 0,curr_id INTEGER,remark TEXT,unit_id INTEGER,u_val REAL DEFAULT 0,base_unit INTEGER,qty_pr REAL DEFAULT 0,e_date TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,gsm TEXT,address TEXT,account_parent INTEGER,group_id INTEGER,account_type INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT,cus_id INTEGER,out_amount REAL DEFAULT 0,in_amount REAL DEFAULT 0,date_ TEXT,remarks TEXT,now_ TEXT,param1 TEXT,param2 TEXT,fund_id INTEGER,curr_id INTEGER,bill_id INTEGER,user_id INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS account_tree (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,parent_id INTEGER DEFAULT 0,account_type INTEGER DEFAULT 0,group_id INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,barcode TEXT,item_type_id INTEGER,unit_id INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS units (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,base_unit INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE IF NOT EXISTS currencies (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,code TEXT,rate REAL DEFAULT 1)");
        db.execSQL("CREATE TABLE IF NOT EXISTS branches (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,address TEXT)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_bills_date ON bills(date_)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_bill_transactions_bill ON bill_transactions(bill_id)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_transactions_customer ON transactions(cus_id)");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_account_parent ON account_tree(parent_id)");
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        if (oldVersion < 2) { /* reserved for source-backed migrations */ }
    }
}
