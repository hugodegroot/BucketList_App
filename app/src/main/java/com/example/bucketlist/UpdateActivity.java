package com.example.bucketlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;

    private Item item;

    private Button button;
    public static final int REQUESTCODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title = findViewById(R.id.titleText);
        description = findViewById(R.id.descriptionText);
        button = findViewById(R.id.create);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText() != null && description.getText() != null){
                    item = new Item(title.getText().toString(), description.getText().toString(), false);
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    intent.putExtra("Item", item);
                    setResult(Activity.RESULT_OK, intent);

                    finish();
                } else {
                    //Toast.makeText(context, "Vul alle velden in!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
