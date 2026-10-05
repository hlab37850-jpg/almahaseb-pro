package info.aalmoghalis.inventory;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public final class ReportsListActivity extends AppCompatActivity {
    private static final String[] TITLES={"تقـرير-مخزون الأصناف","تقـرير-حسابات الموردين","تقـرير-إجمالي المشتريات","تقـرير-إجمالي المبيعات","تقـرير-الصندوق","تقـرير-النفقات والإيرادات"};
    @Override protected void onCreate(Bundle state){super.onCreate(state);setContentView(R.layout.activity_list);((TextView)findViewById(R.id.listTitle)).setText("التقارير");((ListView)findViewById(R.id.items)).setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,TITLES));}
}
