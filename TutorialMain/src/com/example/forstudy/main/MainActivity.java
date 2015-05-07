package com.example.forstudy.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private Button btn_1, btn_2, btn_3;
	private final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_1 = (Button) findViewById(R.id.menu_1);
		btn_2 = (Button) findViewById(R.id.menu_2);
		btn_3 = (Button) findViewById(R.id.menu_3);

		// Call SubActivity by Intent
		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SubActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		// Send Broadcast to Sub App
		btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendBroadcast(new Intent("com.example.forstudy.main.sendreceiver"));
			}
		});
		
		// CRUD query by handling SQLite
		btn_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DBActivity.class);
				startActivity(intent);
			}
		});
		
	}

	// Change Background Image by Intent Result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.RelativeLayout01);

		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String photo_tag = data.getStringExtra("photo_tag");

				if (photo_tag.equals("1"))
					rl.setBackgroundResource(R.drawable.photo1);
				else if (photo_tag.equals("2"))
					rl.setBackgroundResource(R.drawable.photo2);
				else if (photo_tag.equals("3"))
					rl.setBackgroundResource(R.drawable.photo3);
				else if (photo_tag.equals("4"))
					rl.setBackgroundResource(R.drawable.photo4);
				else if (photo_tag.equals("5"))
					rl.setBackgroundResource(R.drawable.photo5);
				else if (photo_tag.equals("6"))
					rl.setBackgroundResource(R.drawable.photo6);
			}
		}
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
