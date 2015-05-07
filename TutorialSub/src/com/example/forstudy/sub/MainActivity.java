package com.example.forstudy.sub;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button btn_1, btn_2;
	private TextView tv_1;
	private boolean SERVICE_STATUS = false;
	static final Uri CONTENT_URI = Uri.parse("content://com.example.forstudy.main");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_1 = (Button) findViewById(R.id.menu_1);
		btn_2 = (Button) findViewById(R.id.menu_2);
		
		// Start and Destroy Service which receives broadcast
		btn_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SERVICE_STATUS) {
					startService(new Intent(MainActivity.this, ServiceExample.class));
					SERVICE_STATUS = false;
				} else {
					stopService(new Intent(MainActivity.this, ServiceExample.class));
					SERVICE_STATUS = true;
				}
			}
		});
		
		// Read Main Application's DB by Content Provider and Resolver
		btn_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_1 = (TextView) findViewById(R.id.TextView01);
				tv_1.setText("");
				
				// Get data from CONTENT_URI by Content Resolver
				ContentResolver cr = getContentResolver();
				Cursor result = cr.query(CONTENT_URI, null, null, null, null);
				
				ArrayList<String> list = new ArrayList<String>();
		        
				result.moveToFirst();
				
		        while(!result.isAfterLast()){
		            int id = result.getInt(0);
		            String str = result.getString(1);
		            list.add(id + " : " + str);
		            result.moveToNext();
		        }
		        result.close();
				
		        for (int i=0; i<list.size(); i++) {
					tv_1.append(list.get(i) + "\n");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
