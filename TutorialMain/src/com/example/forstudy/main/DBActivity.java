package com.example.forstudy.main;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// SQLite CRUD
public class DBActivity extends Activity {

	private Button btn_1, btn_2, btn_3, btn_4;
	private EditText et_1, et_2, et_3, et_4;
	private TextView tv_1;
	private final String DB_NAME = "sampleDB.db", TABLE_NAME = "sampleTable";
	private final int dbMode = Context.MODE_PRIVATE;
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sqlite);

		btn_1 = (Button) findViewById(R.id.db_menu_1);
		btn_2 = (Button) findViewById(R.id.db_menu_2);
		btn_3 = (Button) findViewById(R.id.db_menu_3);
		btn_4 = (Button) findViewById(R.id.db_menu_4);
		
		// Open DB and Create Table if not exists
		openDB(DB_NAME, dbMode);
		createTableIfNotExist();

		// Create query to save
		btn_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_1 = (EditText) findViewById(R.id.EditText01);
				
				insertData(et_1.getText().toString());
				et_1.setText("");
			}
		});
		
		// Read query
		btn_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				tv_1 = (TextView) findViewById(R.id.TextView01);
				tv_1.setText("");
				
				list = readData();
				for (int i=0; i<list.size(); i++) {
					tv_1.append(list.get(i) + "\n");
				}
			}
		});
		
		// Update query to edit
		btn_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_2 = (EditText) findViewById(R.id.EditText02);
				et_3 = (EditText) findViewById(R.id.EditText03);
				
				if (!et_2.getText().toString().equals(""))
					updateData(Integer.parseInt(et_2.getText().toString()), et_3.getText().toString());
				
				et_2.setText("");
				et_3.setText("");
			}
		});
		
		// Delete query
		btn_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_4 = (EditText) findViewById(R.id.EditText04);
				
				if (!et_4.getText().toString().equals(""))
					deleteData(Integer.parseInt(et_4.getText().toString()));
				
				et_4.setText("");
			}
		});
	}
	
	// Open or Create DB
    public void openDB (String dbName, int dbMode) {
        db = openOrCreateDatabase(dbName, dbMode, null);
    }
    
    // Create Table
    public void createTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, test_column TEXT NOT NULL)";
        db.execSQL(sql);
    }
    
    // Create data
    public void insertData(String str){
        String sql = "INSERT INTO " + TABLE_NAME + " values(NULL, '" + str + "');";
        db.execSQL(sql);
    }
    
    // Read data
    public ArrayList<String> readData() {
    	String sql = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor result = db.rawQuery(sql, null);
        
        result.moveToFirst();
         
        ArrayList<String> list = new ArrayList<String>();
        
        while(!result.isAfterLast()){
            int id = result.getInt(0);
            String str = result.getString(1);
            list.add(id + " : " + str);
            result.moveToNext();
        }
        result.close();
        
        return list;
    }
    
    // Update data
    public void updateData(int index, String str) {
        String sql = "UPDATE " + TABLE_NAME + " SET test_column = '" + str + "' WHERE id = " + index + ";";
        db.execSQL(sql);
    }
     
    // Delete data
    public void deleteData(int index) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = " + index + ";";
        db.execSQL(sql);
    }
}
