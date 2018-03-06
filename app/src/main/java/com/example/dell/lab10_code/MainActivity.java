package com.example.dell.lab10_code;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button btn_add;
    private ListView list;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add=  findViewById(R.id.add);
        list=  findViewById(R.id.main_list);
        updateList();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, Additem.class);
                startActivityForResult(intent, 0);  /*0代表请求跳转到添加界面*/
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View layout = inflater.inflate(R.layout.dialoglayout, null);
                final EditText ed_birth= layout.findViewById(R.id.ed_birth); //变量初始化
                final EditText ed_gift=  layout.findViewById(R.id.ed_gift);
                final TextView name=  layout.findViewById(R.id.txt_name);
                final TextView phone=  layout.findViewById(R.id.phone_number);

                Cursor cursor= (Cursor) parent.getItemAtPosition(position);  //获取点击的item数据
                name.setText(cursor.getString(cursor.getColumnIndex("name")));
                ed_birth.setText(cursor.getString(cursor.getColumnIndex("birth")));
                ed_gift.setText(cursor.getString(cursor.getColumnIndex("gift")));
                phone.setText(getPhone(cursor.getString(cursor.getColumnIndex("name"))));

                AlertDialog.Builder builder=  new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("o(^▽^)o")
                        .setView(layout)
                        .setNegativeButton("放弃修改", null)
                        .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyDB mydb= new MyDB(MainActivity.this);
                                mydb.update(name.getText().toString(), ed_birth.getText().toString(), ed_gift.getText().toString());
                                updateList();
                            }
                        })
                        .show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor= (Cursor) parent.getItemAtPosition(position);
                final String temp = cursor.getString(cursor.getColumnIndex("name"));
                AlertDialog.Builder builder=  new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除？")
                        .setNegativeButton("否", null)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyDB mydb= new MyDB(MainActivity.this);
                                mydb.delete(temp);
                                updateList();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    private void updateList() {
        MyDB mydb= new MyDB(MainActivity.this);
        Cursor cursor = mydb.getAll();
        if ((cursor != null) && (cursor.getCount() >= 0)) {
            adapter= new SimpleCursorAdapter(this,
                    R.layout.item,
                    cursor,
                    new String[] {"name", "birth", "gift"},
                    new int[] {R.id.nameText, R.id.birthText, R.id.giftText});
            list.setAdapter(adapter);
        }
        mydb.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == 1) { //发生新的添加
                updateList();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPhone(String _name) {
        Cursor Cursor1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String str1 = "";
        while (Cursor1.moveToNext()) {
            String str2 = Cursor1.getString(Cursor1.getColumnIndex("_id"));
            if (Cursor1.getString(Cursor1.getColumnIndex("display_name")).equals(_name)) {
                str1 = "";
                if (Integer.parseInt(Cursor1.getString(Cursor1.getColumnIndex("has_phone_number"))) > 0) {
                    Cursor Cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + str2, null, null);
                    while (Cursor2.moveToNext()) {
                        str1 = str1 + Cursor2.getString(Cursor2.getColumnIndex("data1")) + " ";
                    }
                    Cursor2.close();
                }
            }
        }
        Cursor1.close();
        if (str1.equals("")) str1= "无";
        return str1;
    }
}