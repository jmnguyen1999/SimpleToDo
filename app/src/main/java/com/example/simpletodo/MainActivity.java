package com.example.simpletodo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

/**
 * MainActivity.java
 * Purpose: Where To-Do app resides, defines functions of app and file system for persistence of items
 *
 * @author Josephine Nguyen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";         //keys for intents, arbitrary
    public static final String  KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;        //request code for opening an EditActivity, arbitrary

    List<String> items;         //holds all current items in app
    Button addBttn;
    EditText etItem;            //text field
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @Override
    /**onCreate()
     * Purpose: Called when app is ran --> show UI, attach widgets/listeners, define functions of widgets/listeners:
     *      addBttn - adds a new item to list
     *      longClickListener - removes selected item
     *      clickListener - opens EditActivity (to edit selected item)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1.) Connect widgets to those in xml file
        addBttn = findViewById(R.id.addBttn);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        loadItems();    //boots previously saved items

        //2.) Prepare click listeners to define a RecyclerView (RV) Adapter:
        // 2a.) Define an OnLongClickListener:   function - Removes selected item
        ItemsAdapter.OnLongClickListener longClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            // Removes from model, alert RV Adapter, show user a pop up confirmation (Toast)
            public void onClick(int position){
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        // 2b.) Define a ClickListener:      function - Opens an EditActivity
        ItemsAdapter.OnClickListener clickListener = new ItemsAdapter.OnClickListener(){
            @Override
            // Uses Intent to open EditActivity, passes needed info, show to screen
            public void onClick(int position){
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);
                startActivityForResult(intent, EDIT_TEXT_CODE);      //EDIT_TEXT_CODE = a request code
            }
        };

        //3.) Define an RV Adapter and connect to RV
        itemsAdapter = new ItemsAdapter(items, longClickListener, clickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //4.) Add Function via addBttn: Define OnClickListener
        addBttn.setOnClickListener(new View.OnClickListener() {
            //when clicked: get text, store/add it to "items", alert RV adapter, clear text field, show pop up confirmation (Toast)
            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString();
                items.add(item);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();        //write new items to saved file
            }
        });
    }


    @Override
    /**onActivityResult()
     * Purpose: Handles the result of the EditActivity - Verifies result (RESULT_OK && from EDIT_TEXT_CODE), updates/saves item, notifies RV adapter, show pop up confirmation to user
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();        //write changed items to saved file

            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT);
        }
        else{
            Log.w("MainActivity", "Unknown call to onActivityResult()");
        }
    }

    /**getDataFile()
     * Purpose: Returns the file where all items/data is being stored, helper method for loadItems and saveItems()
     */
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    /**loadItems()
     * Purpose: loads saved items, called when app is booted - reads from file by getDataFile()
     */
    private void loadItems() {
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    /**saveItems()
     * Purpose: Writes items into the data file (from getDataFile()), called whenever list of items is changed
     */
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch(IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}