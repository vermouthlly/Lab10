package com.example.dell.lab10_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Additem extends AppCompatActivity {

    private EditText change_name, change_birth, change_gift;

    private Button change_button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        change_name=  findViewById(R.id.et_name);
        change_birth=  findViewById(R.id.et_birth);
        change_gift=  findViewById(R.id.et_gift);
        change_button=  findViewById(R.id.addBtn);

        change_button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if (change_name.getText().toString().equals("")) {
                    Toast.makeText(Additem.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    MyDB mydb= new MyDB(Additem.this);
                    if (mydb.query(change_name.getText().toString())) {
                        Toast.makeText(Additem.this, "该名称已存在", Toast.LENGTH_SHORT).show();
                    } else {
                        mydb.insert(change_name.getText().toString(), change_birth.getText().toString(), change_gift.getText().toString());
                        setResult(1);
                        finish();
                    }
                }
            }

        });
    }
}
