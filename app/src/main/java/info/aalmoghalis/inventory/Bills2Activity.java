package info.aalmoghalis.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public final class Bills2Activity extends AppCompatActivity {
 private static final String[] TITLES={"قائمة المبيعات","قائمة المشتريات","تحويل مخزني","تسويات مخزنية","جرد مخزني","عرض سعر","طلب شراء","توريد مخزني","صرف مخزني"};
 private AppDb db;
 @Override protected void onCreate(Bundle state){
  super.onCreate(state);setContentView(R.layout.activity_list);db=new AppDb(this);
  int type=getIntent().getIntExtra("TR_TYPE",1);
  ((TextView)findViewById(R.id.listTitle)).setText(type==2?"قائمة المشتريات":"قائمة المبيعات");
  Button add=new Button(this);add.setText(type==2?"شراء جديد":"بيع جديد");add.setOnClickListener(v->startActivity(new Intent(this,BillEditActivity.class).putExtra("TR_TYPE",type)));
  ((android.widget.LinearLayout)findViewById(android.R.id.content).getRootView().findViewById(R.id.listRoot)).addView(add,0,new android.widget.LinearLayout.LayoutParams(-1,56));
  load(type,(ListView)findViewById(R.id.items));
 }
 private void load(int type,ListView list){
  ArrayList<String> rows=new ArrayList<>();SQLiteDatabase d=db.getReadableDatabase();
  try(Cursor c=d.rawQuery("SELECT id,date_,amount,paid_amount,remarks FROM bills WHERE tr_type=? ORDER BY id DESC",new String[]{String.valueOf(type)})){
   while(c.moveToNext()) rows.add("#"+c.getLong(0)+"  "+safe(c.getString(1))+"  "+c.getDouble(2)+"  "+safe(c.getString(4)));
  }
  if(rows.isEmpty()) rows.add("لا توجد سجلات");
  list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rows));
 }
 private String safe(String s){return s==null?"":s;}
 @Override protected void onResume(){super.onResume();if(db!=null)load(getIntent().getIntExtra("TR_TYPE",1),(ListView)findViewById(R.id.items));}
}