package com.example.forstudy.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SubActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_background);

		ImageView photo01 = (ImageView) findViewById(R.id.Photo01);
		ImageView photo02 = (ImageView) findViewById(R.id.Photo02);
		ImageView photo03 = (ImageView) findViewById(R.id.Photo03);
		ImageView photo04 = (ImageView) findViewById(R.id.Photo04);
		ImageView photo05 = (ImageView) findViewById(R.id.Photo05);
		ImageView photo06 = (ImageView) findViewById(R.id.Photo06);

		photo01.setOnClickListener(putPhotoTagToIntent);
		photo02.setOnClickListener(putPhotoTagToIntent);
		photo03.setOnClickListener(putPhotoTagToIntent);
		photo04.setOnClickListener(putPhotoTagToIntent);
		photo05.setOnClickListener(putPhotoTagToIntent);
		photo06.setOnClickListener(putPhotoTagToIntent);

	}

	// Put tag value of image to intent 
	View.OnClickListener putPhotoTagToIntent = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			Intent intent = getIntent();
			intent.putExtra("photo_tag", v.getTag().toString());

			setResult(RESULT_OK, intent);
			finish();
		}
	};

}
