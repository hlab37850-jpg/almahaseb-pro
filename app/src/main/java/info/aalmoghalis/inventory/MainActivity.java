package info.aalmoghalis.inventory;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;

public final class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private static final String[] DRAWER = {
            "حفـظ نسخة إحتياطية", "إسترجاع قاعدة البيانات", "جوجل درايف", "دليل الحسابات",
            "إعـدادات", "للتــواصـل والـدعم", "حــول البـرنـامج", "خروج"
    };
    private static final String[] ROOT = {"قبض/صرف", "المبيعات", "الحسابات", "المشتريات"};

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer);
        findViewById(R.id.menu).setOnClickListener(v -> drawer.openDrawer(Gravity.START));
        ListView list = findViewById(R.id.drawerList);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DRAWER));
        list.setOnItemClickListener((p, v, position, id) -> {
            if (position == 7) finish();
            else drawer.closeDrawer(Gravity.START);
        });
        buildRootGrid();
    }

    private void buildRootGrid() {
        GridLayout grid = findViewById(R.id.grid);
        for (String label : ROOT) {
            MaterialButton b = new MaterialButton(this);
            b.setText(label);
            b.setTextSize(17);
            b.setGravity(Gravity.CENTER);
            b.setAllCaps(false);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = 0;
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            int m = (int)(getResources().getDisplayMetrics().density * 5);
            lp.setMargins(m, m, m, m);
            grid.addView(b, lp);
        }
    }
}
