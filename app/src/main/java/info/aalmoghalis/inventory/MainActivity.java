package info.aalmoghalis.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;

public final class MainActivity extends AppCompatActivity {
 private DrawerLayout drawer;
 private static final String[] DRAWER={"حفـظ نسخة إحتياطية","إسترجاع قاعدة البيانات","جوجل درايف","دليل الحسابات","إعـدادات","للتــواصـل والـدعم","حــول البـرنـامج","خروج"};
 private static final String[] ROOT={"قبض/صرف","المبيعات","الحسابات","المشتريات"};
 @Override protected void onCreate(Bundle state){super.onCreate(state);setContentView(R.layout.activity_main);drawer=findViewById(R.id.drawer);findViewById(R.id.menu).setOnClickListener(v->drawer.openDrawer(Gravity.START));ListView list=findViewById(R.id.drawerList);list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,DRAWER));list.setOnItemClickListener((p,v,pos,id)->{if(pos==7)finish();else if(pos==3){startActivity(new Intent(this,ReportsListActivity.class));drawer.closeDrawer(Gravity.START);}else drawer.closeDrawer(Gravity.START);});buildRootGrid();}
 private void buildRootGrid(){GridLayout grid=findViewById(R.id.grid);for(int i=0;i<ROOT.length;i++){final int index=i;MaterialButton b=new MaterialButton(this);b.setText(ROOT[i]);b.setTextSize(17);b.setGravity(Gravity.CENTER);b.setAllCaps(false);b.setOnClickListener(v->{if(index==1||index==3){Intent in=new Intent(this,Bills2Activity.class);in.putExtra("TR_TYPE",index==3?2:1);startActivity(in);}});GridLayout.LayoutParams lp=new GridLayout.LayoutParams();lp.width=0;lp.height=0;lp.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);lp.rowSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);int m=(int)(getResources().getDisplayMetrics().density*5);lp.setMargins(m,m,m,m);grid.addView(b,lp);}}
}
