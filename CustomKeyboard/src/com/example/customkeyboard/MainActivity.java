/**
 * CustomKeyboard.java				Soft Keyboard that supports complicated equation
 * CustomCalculator.java			Parse and calculate mathematical eqaution by exp4j library
 * libs/exp4j						Mathematical library that parse and calculate equation (Apache 2.0 License)
 * layout/activity_main.xml			Sample table layout
 * layout/normal_keyboard.xml		Normal keyboard that supports four fundamental arithmetic operations
 * layout/engineering_keyboard.xml	Engineering keyboard that supports quite complicated operations like log(x) and sin(x)
 * res/value/dimens.xml				Designate key width and height for portrait mode
 * res/values-land/dimens.xml		Designate key width and height for landscape mode
 * drawable/						image using at soft keyboard button
 */

package com.example.customkeyboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * Register EditText to use Custom Keyboard from layout
 */
public class MainActivity extends Activity {

	CustomKeyboard cKeyboard;

	/**
	 * Register EditText which should use Custom Keyboard
	 * Use Normal mode keyboard as default
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Maintain keyboard mode when device rotated
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		String keyboardMode = pref.getString("keyboardMode", "normal");
		
		if (keyboardMode.equals("normal"))
			cKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.layout.normal_keyboard);
		else
			cKeyboard = new CustomKeyboard(this, R.id.keyboardview, R.layout.engineering_keyboard);

		cKeyboard.registerEditText(R.id.EditText01);
		cKeyboard.registerEditText(R.id.EditText02);
		cKeyboard.registerEditText(R.id.EditText03);
		cKeyboard.registerEditText(R.id.EditText04);
		cKeyboard.registerEditText(R.id.EditText05);
		cKeyboard.registerEditText(R.id.EditText06);
		cKeyboard.registerEditText(R.id.EditText07);
		cKeyboard.registerEditText(R.id.EditText08);
	}
	
	/**
	 * Hide CustomKeyboard when click outside
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (cKeyboard.isCustomKeyboardVisible())
			cKeyboard.hideCustomKeyboard();

		return super.onTouchEvent(event);
	}

	/**
	 * Hide CustomKeyboard when click cancel button
	 */
	@Override
	public void onBackPressed() {

		if (cKeyboard.isCustomKeyboardVisible())
			cKeyboard.hideCustomKeyboard();
		else
			this.finish();
	}
}