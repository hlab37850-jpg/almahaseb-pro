package info.aalmoghalis.inventory;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public final class Bills2Activity extends AppCompatActivity {
    private static final String[] TITLES={"قائمة المبيعات","قائمة المشتريات","تحويل مخزني","تسويات مخزنية","جرد مخزني","عرض سعر","طلب شراء","توريد مخزني","صرف مخزني"};
    @Override protected void onCreate(Bundle state){super.onCreate(state);setContentView(R.layout.activity_list);int type=getIntent().getIntExtra("TR_TYPE",1);((TextView)findViewById(R.id.listTitle)).setText(type==2?"فواتير الشراء":"فواتير البيع");ListView list=findViewById(R.id.items);list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,TITLES));}
}
