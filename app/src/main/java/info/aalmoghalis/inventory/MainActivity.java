package info.aalmoghalis.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public final class MainActivity extends AppCompatActivity {
 private DrawerLayout drawer;
 private static final String[] DRAWER={"حفـظ نسخة إحتياطية","إسترجاع قاعدة البيانات","جوجل درايف","دليل الحسابات","إعـدادات","للتــواصـل والـدعم","حــول البـرنـامج","خروج"};
 @Override protected void onCreate(Bundle state){
  super.onCreate(state); setContentView(R.layout.activity_main);
  drawer=findViewById(R.id.drawer);
  findViewById(R.id.menu).setOnClickListener(v->drawer.openDrawer(Gravity.START));
  ListView list=findViewById(R.id.drawerList);
  list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,DRAWER));
  list.setOnItemClickListener((p,v,pos,id)->{
   if(pos==7){finish();return;}
   if(pos==3) startActivity(new Intent(this,AccountTreeMainActivity.class));
   else if(pos==4) startActivity(new Intent(this,SettingsActivity.class));
   else if(pos==1) { }
   else if(pos==2) { }
   drawer.closeDrawer(Gravity.START);
  });
  bindActions();
 }
 private void bindActions(){
  findViewById(R.id.action_receipt).setOnClickListener(v->startActivity(new Intent(this,BillEditActivity.class).putExtra("TR_TYPE",0)));
  findViewById(R.id.action_sales).setOnClickListener(v->startActivity(new Intent(this,Bills2Activity.class).putExtra("TR_TYPE",1)));
  findViewById(R.id.action_accounts).setOnClickListener(v->startActivity(new Intent(this,AccountTreeMainActivity.class)));
  findViewById(R.id.action_purchases).setOnClickListener(v->startActivity(new Intent(this,Bills2Activity.class).putExtra("TR_TYPE",2)));
  findViewById(R.id.menu_inventory).setOnClickListener(v->{});
  findViewById(R.id.menu_vouchers).setOnClickListener(v->startActivity(new Intent(this,AccountTreeMainActivity.class)));
  findViewById(R.id.menu_items).setOnClickListener(v->startActivity(new Intent(this,ItemsActivity.class)));
  findViewById(R.id.menu_reports).setOnClickListener(v->startActivity(new Intent(this,ReportsListActivity.class)));
  findViewById(R.id.menu_currencies).setOnClickListener(v->{});
 }
}