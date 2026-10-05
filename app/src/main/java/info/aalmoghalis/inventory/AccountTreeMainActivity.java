package info.aalmoghalis.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public final class AccountTreeMainActivity extends AppCompatActivity {
    private AppDb helper;
    private final ArrayList<Long> ids=new ArrayList<>();
    private final ArrayList<String> names=new ArrayList<>();
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);
        helper=new AppDb(this);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        TextView title=new TextView(this); title.setText("دليل الحسابات"); title.setTextSize(20); title.setGravity(17); title.setPadding(12,18,12,18); root.addView(title);
        LinearLayout add=new LinearLayout(this); add.setPadding(8,4,8,4);
        EditText name=new EditText(this); name.setHint("اسم الحساب"); add.addView(name,new LinearLayout.LayoutParams(0,60,1));
        TextView save=new TextView(this); save.setText("إضافة"); save.setGravity(17); save.setPadding(18,0,18,0); add.addView(save,new LinearLayout.LayoutParams(-2,60)); root.addView(add);
        ListView list=new ListView(this); root.addView(list,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);
        save.setOnClickListener(v->{String n=name.getText().toString().trim(); if(n.isEmpty()){Toast.makeText(this,"أدخل اسم الحساب",Toast.LENGTH_SHORT).show();return;} ContentValues cv=new ContentValues();cv.put("name",n);cv.put("parent_id",0);helper.getWritableDatabase().insert("account_tree",null,cv);name.setText("");load(list);});
        load(list);
    }
    private void load(ListView list){ids.clear();names.clear();SQLiteDatabase db=helper.getReadableDatabase();try(Cursor c=db.rawQuery("SELECT id,name,parent_id FROM account_tree WHERE parent_id=0 ORDER BY id",null)){while(c.moveToNext()){ids.add(c.getLong(0));names.add(c.getString(1));}}list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names));}
}
