package info.aalmoghalis.inventory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public final class ReportActivity extends AppCompatActivity {
 private AppDb db;
 @Override protected void onCreate(Bundle state){
  super.onCreate(state);setContentView(R.layout.activity_list);db=new AppDb(this);
  int type=getIntent().getIntExtra("REPORT",-1);String[] titles={"تقـرير-مخزون الأصناف","تقـرير-حسابات الموردين","تقـرير-إجمالي المشتريات","تقـرير-إجمالي المبيعات","تقـرير-الصندوق","تقـرير-النفقات والإيرادات"};
  ((TextView)findViewById(R.id.listTitle)).setText(type>=0&&type<titles.length?titles[type]:"التقرير");
  load(type,(ListView)findViewById(R.id.items));
 }
 private void load(int type,ListView list){
  ArrayList<String> rows=new ArrayList<>();SQLiteDatabase d=db.getReadableDatabase();
  String sql=null;String[] args=null;
  if(type==2||type==3){sql="SELECT id,date_,amount,paid_amount,remarks FROM bills WHERE tr_type=? ORDER BY id DESC";args=new String[]{String.valueOf(type==2?2:1)};}
  else if(type==4){sql="SELECT id,date_,in_amount,out_amount,remarks FROM transactions ORDER BY id DESC";}
  if(sql!=null)try(Cursor c=d.rawQuery(sql,args)){while(c.moveToNext()){StringBuilder s=new StringBuilder("#").append(c.getLong(0));for(int i=1;i<c.getColumnCount();i++)s.append("  ").append(c.getString(i));rows.add(s.toString());}}
  if(rows.isEmpty())rows.add("لا توجد سجلات");
  list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rows));
 }
}