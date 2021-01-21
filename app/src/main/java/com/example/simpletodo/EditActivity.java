package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
/**
 * EditActivity.java
 * Purpose: To open another page specifically allowing the user to edit and save a selected item
 *
 * @author Josephine Nguyen
 * @version 1.0
 */
public class EditActivity extends AppCompatActivity {
    EditText editItem;
    Button saveBttn;

    @Override
    /**onCreate()
     * Purpose: Called when app is ran --> show UI, attach widgets/listeners, set title, attach function to saveBttn
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editItem = findViewById(R.id.editItem);
        saveBttn = findViewById(R.id.saveBttn);

        getSupportActionBar().setTitle("Edit Item");

        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));   //populate text field with selected item

        //Save Function:
        saveBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            //Define how to save new item: creates Intent, passes edited item, send RESULT_OK result, close app
            public void onClick(View v){
                Intent intent = new Intent();

                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}
