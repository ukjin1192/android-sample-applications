package com.example.customkeyboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Custom Keyboard which supports mathematical expressions
 */
public class CustomKeyboard {

	private Activity mHostActivity;

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;

	private KeyboardView mKeyboardView;
	private ScrollView scrollContainer;
	private TextView emptySpace;
	
	private static int KEYBOARD_HEIGHT = 563;
	private static int scrollContainerHeight = -1;
	private static int scrollableAreaHeight = -1;
	
	/**
	 * Create a custom keyboard, that uses the KeyboardView
	 * 
	 * @param host
	 *            Host activity
	 * @param viewid
	 *            ID of the KeyboardView
	 * @param layoutid
	 *            ID of the layout
	 */
	public CustomKeyboard(Activity host, int viewid, int layoutid) {

		mHostActivity = host;
		mKeyboardView = (KeyboardView) mHostActivity.findViewById(viewid);
		mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
		mKeyboardView.setPreviewEnabled(false);
		mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

		// Hide default soft keyboard
		mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Get shared preferences from main activity
		pref = mHostActivity.getSharedPreferences("pref", 1);
		editor = pref.edit();

		// Get views
		scrollContainer = (ScrollView) mHostActivity.findViewById(R.id.scroll_container);
		emptySpace = (TextView) mHostActivity.findViewById(R.id.empty_space);
	}

	private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

		// It should be equal with android:code value in keyboard xml file
		public final static int CodeNormalMode = 55000;
		public final static int CodeEngineeringMode = 55001;
		public final static int CodeDelete = 55002;
		public final static int CodeClear = 55003;
		public final static int CodePrevious = 55004;
		public final static int CodeNext = 55005;
		public final static int CodeLeft = 55006;
		public final static int CodeRight = 55007;
		public final static int CodeSquare = 55010;
		public final static int CodeExponential = 55011;
		public final static int CodeFactorial = 55012;
		public final static int CodeSquareRoot = 55013;
		public final static int CodeLogarithmE = 55014;
		public final static int CodeLogarithm10 = 55015;
		public final static int CodeAbsolute = 55016;
		public final static int CodeSine = 55017;
		public final static int CodeCosine = 55018;
		public final static int CodeTangent = 55019;
		public final static int CodePi = 55020;

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {

			View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
			EditText edittext = (EditText) focusCurrent;
			Editable editable = edittext.getText();
			CustomCalculator cc = new CustomCalculator(mHostActivity);

			if (focusCurrent == null
					|| focusCurrent.getClass() != EditText.class)
				return;

			int start = edittext.getSelectionStart();
			int end = edittext.getSelectionEnd();

			// Delete selected part when new input comes
			if (start != end && primaryCode != CodeNormalMode
					&& primaryCode != CodeEngineeringMode
					&& primaryCode != CodePrevious && primaryCode != CodeNext
					&& primaryCode != CodeLeft && primaryCode != CodeRight) {
				editable.delete(start, end);
			}

			if (primaryCode == CodeNormalMode) {
				// Switch keyboard between normal mode and engineering mode
				editor.putString("keyboardMode", "normal");
				editor.commit();

				Toast.makeText(mHostActivity, "Normal Mode", Toast.LENGTH_SHORT)
						.show();

				mKeyboardView.setKeyboard(new Keyboard(mHostActivity,
						R.layout.normal_keyboard));
			} else if (primaryCode == CodeEngineeringMode) {
				// Switch keyboard between normal mode and engineering mode
				editor.putString("keyboardMode", "engineering");
				editor.commit();

				Toast.makeText(mHostActivity, "Engineering Mode",
						Toast.LENGTH_SHORT).show();

				mKeyboardView.setKeyboard(new Keyboard(mHostActivity,
						R.layout.engineering_keyboard));
			} else if (primaryCode == CodeDelete) {
				// Erase character in front of the cursor
				if (editable != null && start > 0)
					editable.delete(start - 1, start);
			} else if (primaryCode == CodeClear) {
				// Clear whole equation
				if (editable != null)
					editable.clear();
			} else if (primaryCode == CodePrevious) {
				// Calculate equation
				cc.calculateEquation(edittext);

				// Move to the previous EditText
				View focusNew = edittext.focusSearch(View.FOCUS_BACKWARD);

				// Exception check for first and last EditText
				if (focusNew != null && focusNew instanceof EditText) {
					focusNew.requestFocus();

					// Block selected EditText
					edittext = (EditText) focusNew;
					edittext.selectAll();
				}

			} else if (primaryCode == CodeNext) {
				// Calculate equation
				cc.calculateEquation(edittext);

				// Move to the next EditText
				View focusNew = edittext.focusSearch(View.FOCUS_FORWARD);

				// Exception check for first and last EditText
				if (focusNew != null && focusNew instanceof EditText) {
					focusNew.requestFocus();

					// Block selected EditText
					edittext = (EditText) focusNew;
					edittext.selectAll();
				}

			} else if (primaryCode == CodeLeft) {
				// Move cursor to left
				if (start > 0)
					edittext.setSelection(start - 1);
			} else if (primaryCode == CodeRight) {
				// Move cursor to right
				if (start < edittext.length())
					edittext.setSelection(start + 1);
			} else if (primaryCode == CodeSquare) {
				// X ^ Y
				if (!(editable.length() + 1 > 40))
					editable.insert(start, "^");
			} else if (primaryCode == CodeExponential) {
				// e ^ X
				if (!(editable.length() + 5 > 40)) {
					editable.insert(start, "exp()");
					edittext.setSelection(start + 4);
				}
			} else if (primaryCode == CodeFactorial) {
				// n !
				if (!(editable.length() + 1 > 40))
					editable.insert(start, "!");
			} else if (primaryCode == CodeSquareRoot) {
				// root(x)
				if (!(editable.length() + 6 > 40)) {
					editable.insert(start, "sqrt()");
					edittext.setSelection(start + 5);
				}
			} else if (primaryCode == CodeLogarithmE) {
				// ln(x)
				if (!(editable.length() + 5 > 40)) {
					editable.insert(start, "log()");
					edittext.setSelection(start + 4);
				}
			} else if (primaryCode == CodeLogarithm10) {
				// log(x)
				if (!(editable.length() + 7 > 40)) {
					editable.insert(start, "log10()");
					edittext.setSelection(start + 6);
				}
			} else if (primaryCode == CodeAbsolute) {
				// ABS(X)
				if (!(editable.length() + 5 > 40)) {
					editable.insert(start, "abs()");
					edittext.setSelection(start + 4);
				}
			} else if (primaryCode == CodeSine) {
				// sin(x)
				if (!(editable.length() + 5 > 40)) {
					editable.insert(start, "sin()");
					edittext.setSelection(start + 4);
				}
			} else if (primaryCode == CodeCosine) {
				// cos(x)
				if (!(editable.length() + 5 > 40)) {
					editable.insert(start, "cos()");
					edittext.setSelection(start + 4);
				}
			} else if (primaryCode == CodeTangent) {
				// tan(x)
				if (!(editable.length() + 5 > 40)) {
					editable.insert(start, "tan()");
					edittext.setSelection(start + 4);
				}
			} else if (primaryCode == CodePi) {
				// 3.141592 ...
				if (!(editable.length() + 2 > 40))
					editable.insert(start, "PI");
			} else {
				if (!(editable.length() + 1 > 40))
					editable.insert(start,
							Character.toString((char) primaryCode));
			}
		}

		@Override
		public void onPress(int arg0) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeUp() {
		}
	};

	/**
	 * Returns whether the CustomKeyboard is visible
	 */
	public boolean isCustomKeyboardVisible() {

		return mKeyboardView.getVisibility() == View.VISIBLE;
	}

	/**
	 * Make the CustomKeyboard visible
	 */
	public void showCustomKeyboard(View v) {

		mKeyboardView.setVisibility(View.VISIBLE);
		mKeyboardView.setEnabled(true);

		// Hide default soft keyboard
		if (v != null)
			((InputMethodManager) mHostActivity
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(v.getWindowToken(), 0);

		
		if (scrollContainerHeight < 0)
			scrollContainerHeight = scrollContainer.getChildAt(0).getHeight();
		if (scrollableAreaHeight < 0)
			scrollableAreaHeight = scrollContainer.getMeasuredHeight();
		
		final int scrollPosition = ((View) v.getParent()).getTop() - scrollableAreaHeight + v.getHeight() + KEYBOARD_HEIGHT;
		
		// Increase empty space height when keyboard hides EditText
		if (((View) v.getParent()).getTop() + v.getHeight() + KEYBOARD_HEIGHT > scrollContainerHeight) {
			LayoutParams params = emptySpace.getLayoutParams();
			params.height = ((View) v.getParent()).getTop() + v.getHeight() + KEYBOARD_HEIGHT - scrollContainerHeight;
			emptySpace.setLayoutParams(params);
			
			new Thread( new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						scrollContainer.scrollTo(0, scrollPosition);
					}
				}
			}).start();
			
		} else {
			scrollContainer.scrollTo(0, scrollPosition);
		}
	}

	/**
	 * Make the CustomKeyboard invisible
	 */
	public void hideCustomKeyboard() {
		mKeyboardView.setVisibility(View.GONE);
		mKeyboardView.setEnabled(false);

		// Rollback relative layout height by make zero height from Empty Space
		LayoutParams params = emptySpace.getLayoutParams();
		params.height = 0;
		emptySpace.setLayoutParams(params);
	}

	/**
	 * Register EditText for using CustomKeyboard
	 * 
	 * @param resid
	 *            ID of the EditText resource
	 */
	public void registerEditText(int resid) {

		final EditText edittext = (EditText) mHostActivity.findViewById(resid);

		// Make the CustomKeyboard appear
		edittext.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					showCustomKeyboard(v);
				} else {
					hideCustomKeyboard();
					CustomCalculator cc = new CustomCalculator(mHostActivity);
					cc.calculateEquation(edittext);
				}

			}
		});

		// Disable default soft keyboard on touch
		edittext.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				EditText edittext = (EditText) v;
				edittext.onTouchEvent(event);

				// Hide default soft keyboard
				((InputMethodManager) mHostActivity
						.getSystemService(Activity.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				return true;
			}
		});

		// Pop-up keyboard on click without any focus change
		edittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edittext.hasFocus()) {
					showCustomKeyboard(v);
				}
			}
		});

		// Disable default soft keyboard on long-click
		edittext.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		// Disable spell check
		edittext.setInputType(edittext.getInputType()
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}
}
