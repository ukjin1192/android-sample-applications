package com.example.customkeyboard;

import java.util.EmptyStackException;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomOperator;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

/**
 * Custom Calculator which calculates mathematical expression based on 'exp4j' library
 */
public class CustomCalculator {
	
	private Activity mHostActivity;
	
	public CustomCalculator(Activity host) {
		mHostActivity = host;
	}
	
	/**
	 * Calculate equation and put result to EditText
	 * @param et			Selected EditText object
	 * @param equation		Mathematical equation
	 */
	public void calculateEquation(EditText et) {

		String equation = et.getText().toString();
		Calculable calc;
		double result;

		// Exception process for operator overlapping (exp4j library doesn't raise exception for this case)
		if (equation.contains("++") || equation.contains("+-") || equation.contains("+*") || equation.contains("+/") ||
				equation.contains("-+") || equation.contains("--") || equation.contains("-*") || equation.contains("-/") ||
				equation.contains("*+") || equation.contains("*-") || equation.contains("**") || equation.contains("*/") ||
				equation.contains("/+") || equation.contains("/-") || equation.contains("/*") || equation.contains("//")) {
			et.setText("");
			Toast.makeText(mHostActivity, "Unparsable Expression", Toast.LENGTH_SHORT).show();
		} else if (!equation.equals("")) {
			
			// Convert Radian value to Degree
			equation = equation.replace("sin(", "sin(PI/180.0*");
			equation = equation.replace("cos(", "cos(PI/180.0*");
			equation = equation.replace("tan(", "tan(PI/180.0*");
			
			try {
				// Convert .xx to 0.xx
				if (equation.charAt(0) == '.')
					equation = '0' + equation;
				
				// Convert xxx,xxx.xx to xxxxxx.xx
				equation.replaceAll(",", "");
				
				// Calculate equation with Custom Variable "PI" and Custom Function "Factorial"
				calc = new ExpressionBuilder(equation).withVariable("PI", Math.PI).withOperation(factorial).build();
				result = calc.calculate();

				// Round up decimal numbers under 0.01
				result = Math.round(result * 100.0) / 100.0;
				
				// Revise value for out of range (as stated in SRS)
				if (result >= 1000000)
					result = 999999.99;
				else if (result < 0)
					result = 0;

				// Remove decimal numbers if it is integer
				if ((result == Math.floor(result)) && !Double.isInfinite(result))
					et.setText((int) result + "");
				else
					et.setText(result + "");
				
			} catch (UnknownFunctionException e) {
				et.setText("");
				Toast.makeText(mHostActivity, "Unknown Function", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (UnparsableExpressionException e) {
				et.setText("");
				Toast.makeText(mHostActivity, "Unparsable Expression", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (EmptyStackException e) {
				et.setText("");
				Toast.makeText(mHostActivity, "Empty Stack", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (ArithmeticException e) {
				et.setText("");
				Toast.makeText(mHostActivity, "Arithmetic Error", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Custom function which calculate '!' as factorial
	 */
	CustomOperator factorial = new CustomOperator("!", true, 6, 1) {
		
		@Override
		protected double applyOperation(double[] values) {
			
			double tmp = 1d;
	        int steps = 1;
	        
	        while (steps < values[0]) {
	        	tmp = tmp * (++steps);
	        }
	        
	        return tmp;
		}
	};
}
