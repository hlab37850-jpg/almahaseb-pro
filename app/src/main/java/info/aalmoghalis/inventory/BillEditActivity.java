package info.aalmoghalis.inventory;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public final class BillEditActivity extends AppCompatActivity {
    private AppDb db;
    @Override protected void onCreate(Bundle state){
        super.onCreate(state); db=new AppDb(this);
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(10,10,10,10);
        TextView title=new TextView(this); int type=getIntent().getIntExtra("TR_TYPE",1); title.setText(type==1?"بيع":"شراء");title.setTextSize(20);title.setGravity(17);root.addView(title);
        EditText date=new EditText(this);date.setHint("التاريخ");root.addView(date);
        EditText customer=new EditText(this);customer.setHint("العميل / المورد");root.addView(customer);
        EditText amount=new EditText(this);amount.setHint("المبلغ");amount.setInputType(2|8192);root.addView(amount);
        EditText paid=new EditText(this);paid.setHint("المدفوع");paid.setInputType(2|8192);root.addView(paid);
        EditText remarks=new EditText(this);remarks.setHint("ملاحظات");root.addView(remarks);
        Button save=new Button(this);save.setText("حفظ");root.addView(save);
        save.setOnClickListener(v->{ContentValues cv=new ContentValues();cv.put("tr_type",type);cv.put("date_",date.getText().toString());cv.put("cus_id",0);cv.put("amount",parse(amount.getText().toString()));cv.put("paid_amount",parse(paid.getText().toString()));cv.put("remarks",remarks.getText().toString());cv.put("bill_type",type);long id=db.getWritableDatabase().insert("bills",null,cv);if(id>0){Toast.makeText(this,"تم الحفظ",Toast.LENGTH_SHORT).show();finish();}else Toast.makeText(this,"تعذر الحفظ",Toast.LENGTH_SHORT).show();});
        setContentView(root);
    }
    private double parse(String s){try{return Double.parseDouble(s.replace(',','.'));}catch(Exception e){return 0;}}
}
