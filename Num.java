package acd170130;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

/**
 *    
 *     @author Aniket Pathak: adp170003
 *     @author Pranjal Deshmukh: psd180000
 *     @author Tanu Rampal: txr180007
 *     @author Abhilasha Devkar: acd170130
 *     Long Project 1: Num - To Perform Integer arithmetic with arbitrarily large numbers
 *
 */

public class Num implements Comparable<Num> {

	static long defaultBase = 10000000; // Change as needed
	long base = defaultBase; // Change as needed

	long[] arr; // array to store arbitrarily large integers
	boolean isNegative = false; // boolean flag to represent negative numbers
	int len; // actual number of elements of array that are used; number is stored in arr[0..len-1]
				

	public Num(String s) {
		int count_zeros = 0;
		String numStr = s;
		// Handle negative case here:
		if (s.charAt(0) == '-') {
			this.isNegative = true;
			numStr = s.substring(1);
		}
		long[] array = new long[numStr.length()];
		for (int i = 0; i < numStr.length(); i++) {
			long carryover = numStr.charAt(i) - '0';
			long tmp = 0;
			int j;

			/*
			 * start at the bottom of the array and work towards the top
			 *
			 * multiply the existing array value by 10, then add new value. carry over
			 * remainder as you work back towards the top of the array
			 */
			for (j = (numStr.length() - 1); (j >= 0); j--) {
				tmp = (array[j] * 10) + carryover;
				array[j] = tmp % defaultBase;
				carryover = tmp / defaultBase;
			}

		}

		if (array.length == 1) {
			count_zeros = 0;
		} else {
			for (long i : array) {
				if (i != 0) {
					break;
				}
				count_zeros++;
			}
		}

		arr = new long[numStr.length() - count_zeros];
		int k = -1;
		this.len = numStr.length() - count_zeros;
		for (int i = numStr.length() - 1; i >= count_zeros; i--) {
			arr[++k] = array[i];

		}
		
		this.base = defaultBase;

	}
	/*
	 * Constructor for instantiating Num with input long
	 * 
	 */

	public Num(long x) {
		if (x == 0) {
			this.len = 1;
			long[] arr = new long[1];
			arr[0] = 0;
			this.arr = arr;
			this.base = defaultBase;
		} else {
			long num = x;
			if (x < 0) {
				this.isNegative = true;
				num = Math.abs(x);
			}
			long temp = num;
			int count = 0;
			int i = 0;
			while (temp > 0) {

				temp = temp / defaultBase;
				count++;
			}
			this.len = count;
			this.arr = new long[count];

			while (num > 0) {
				this.arr[i] = num % defaultBase;
				num = num / defaultBase;
				i++;
			}
			this.base = defaultBase;
		}

	}

	/* Helper function to perform addition
	 * params: Num, Num
	 * return: Num
	 */
	public static Num addHelper(Num a, Num b) {
		long[] result = new long[Math.max(a.arr.length, b.arr.length) + 1];
		int i = 0, j = 0, r = 0;// counters for traversing input (i,j) and result (r)
		long carry = 0;
		while (i < a.len && j < b.len) {
			if (a.arr[i] + b.arr[j] + carry >= defaultBase) {
				result[r] = (a.arr[i] + b.arr[j] + carry) % defaultBase;
				carry = (a.arr[i] + b.arr[j] + carry) / defaultBase;
			} else {
				result[r] = a.arr[i] + b.arr[j] + carry;
				carry = 0;
			}
			i++;
			j++;
			r++;

		}
		if (i < a.len) {
			while (i < a.len) {
				if (a.arr[i] + carry >= defaultBase) {
					result[r] = (a.arr[i] + carry) % defaultBase;
					carry = (a.arr[i] + carry) / defaultBase;
				} else {
					result[r] = a.arr[i] + carry;
					carry = 0;
				}
				i++;
				r++;
			}
		}
		if (j < b.len) {
			while (j < b.len) {
				if (b.arr[j] + carry >= defaultBase) {
					result[r] = (b.arr[j] + carry) % defaultBase;
					carry = (b.arr[j] + carry) / defaultBase;
				} else {
					result[r] = b.arr[j] + carry;
					carry = 0;
				}
				j++;
				r++;
			}
		}
		if (carry > 0) {
			result[r] = carry;
			r++;
		}

		String str = "";
		for (int count = 0; count < result.length; count++) {

			str = str + result[count];
		}
		Num g = new Num("0");
		g.arr = result;
		g.len = r;
		g.base = defaultBase;
		return g;
	}

	/*
	 * Function to perform addition of two values of data-type Num
	 * params: Num, Num
	 * return: Num
	 */
	public static Num add(Num a, Num b) {
		Num result = new Num(0);

		if (!a.isNegative && !b.isNegative) {
			result = addHelper(a, b);
		}

		else if (a.isNegative && b.isNegative) {
			result = addHelper(a, b);
			result.isNegative = true;
		}

		else if (!a.isNegative && b.isNegative) {
			if (a.compareTo(b) == 1) {
				// b.isNegative = false;
				result = subtractHelper(a, b);
			}

			else if (a.compareTo(b) == -1) {
				result = subtractHelper(b, a);
				result.isNegative = true;
			}

			else {
				return result;
			}
		}

		else if (a.isNegative && !b.isNegative) {
			if (a.compareTo(b) == 1) {
				result = subtractHelper(a, b);
				result.isNegative = true;
			}

			else if (a.compareTo(b) == -1) {
				result = subtractHelper(b, a);
			}

			else {
				return result;
			}
		}
		result.base = defaultBase;
		return result;
	}
	
	/* Helper function for the subtraction operation on two Num type params
	 * params-type: Num
	 * return type: Num
	 */
	public static Num subtractHelper(Num a, Num b) {
		long[] result = new long[Math.max(a.arr.length, b.arr.length) + 1];
		int i = 0, j = 0, r = 0, count_zeros = 0;
		long borrow = 0;

		while (i < a.len && j < b.len) {
			if (a.arr[i] - borrow < b.arr[j]) {
				result[r] = a.arr[i] - borrow + defaultBase - b.arr[j];
				borrow = 1;
			} else {
				result[r] = a.arr[i] - borrow - b.arr[j];
				borrow = 0;
			}
			i++;
			j++;
			r++;

		}
		if (i < a.len) {
			while (i < a.len) {
				result[r] = (a.arr[i] - borrow);
				borrow = 0;

				i++;
				r++;

			}
		}
		for (int l = r - 1; l >= 0; l--) {
			if (result[l] != 0) {
				break;
			}
			count_zeros++;
		}
		
		Num g = new Num("0");
		g.arr = result;

		g.len = r - count_zeros;
		g.base = defaultBase;
		return g;
	}
	
	/* Subtract function for two Nums
	 * params: Num, Num
	 * return: Num
	 */
	public static Num subtract(Num a, Num b) {
		Num result = new Num(0);

		// Both A and B are positive
		if (!a.isNegative && !b.isNegative) {
			if (a.compareTo(b) == 1) {
				result = subtractHelper(a, b);
			} else if (a.compareTo(b) == -1) {
				result = subtractHelper(b, a);
				result.isNegative = true;
			} else if (a.compareTo(b) == 0) {
				return result;
			}
		}

		// Both A and B are negative
		else if (a.isNegative && b.isNegative) {
			if (a.compareTo(b) == -1) {
				result = subtractHelper(b, a);
			} else if (a.compareTo(b) == 1) {
				result = subtractHelper(a, b);
				result.isNegative = true;
			} else {
				result = add(a, b);
				result.isNegative = true;
			}
		} else if (!a.isNegative && b.isNegative) { // A is positive and B is negative
			result = add(a, b);
			result.isNegative = false;
		} else if (a.isNegative && !b.isNegative) { // A is negative and B is positive
			result = add(a, b);
			result.isNegative = true;
		}
		
		result.base = defaultBase;
		return result;
	}
	
	/* Helper function for product
	 * params: Num, Num
	 * return: Num
	 */
	 public static Num multiplyHelper(Num a, Num b)
	 {		 
		 long[]a_reverse = reverseList(a);
		 long[]b_reverse = reverseList(b);
		 int len1 = a_reverse.length;
		 int len2 = b_reverse.length;
		 long[]product = new long[ len1 + len2 ];
		 int countZeros = 0;
		 
		 for (int i = len1 - 1; i >= 0; i--) {
		        for (int j = len2 - 1; j >= 0; j--) {
		            int index = len1 + len2 - i - j - 2;
		            product[index] += a_reverse[i] * b_reverse[j];
		            product[index + 1] += product[index] / defaultBase;
		            product[index] %= defaultBase;
		            
		        }
		    }
		 for(int i = product.length-1;i >= 0;i--) {
			 if(product[i] != 0) {
				 break;
			 }
			 countZeros++;
		 }
		
		 Num result = new Num(0);
		 result.arr = product;
		 result.len = product.length-countZeros;
		 result.base = defaultBase;
		 return result;
	 }
	 
	 /* To multipy two Num type variables 
	  * params: Num, Num
	  * return: Num
	  */
	 public static Num product(Num a, Num b) {	
		 Num result = new Num(0);
		 if(a.compareTo(result) == 0 || b.compareTo(result) == 0)
		 {
			 return result;
		 }
		 if((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative))
		 {
			 result = multiplyHelper(a,b); 
		 }
		 else 
		 {
			result = multiplyHelper(a,b); 
			result.isNegative = true;
		 }
		 return result;
	 }


	public static long[] reverseList(Num a) {
		long[] a_reverse = new long[a.len];
		for (int i = a.len - 1; i >= 0; i--) {
			a_reverse[a.len - 1 - i] = a.arr[i];
		}
		return a_reverse;
	}
	
	/* helper function to copy strings
	 * params: int, Num
	 * return: array of Num
	 */

	public static Num[] strCopy(int index, Num a) {

		Num[] arr = new Num[2];

		long[] low = new long[index];
		long[] high = new long[a.len - index];

		for (int i = 0; i < index; i++) {
			low[i] = a.arr[i];
		}

		for (int i = index; i < a.len; i++) {
			high[i - index] = a.arr[i];
		}
		Num g_high = new Num(0);
		Num g_low = new Num(1);
		g_high.len = a.len - index;
		g_low.len = index;
		g_high.base = defaultBase;
		g_low.base = defaultBase;
		g_high.arr = high;
		g_low.arr = low;
		arr[0] = g_high;
		arr[1] = g_low;

		return arr;
	}
	
	/* Function for long multiply
	 * params: Num, Num
	 * return: Num
	 */
	private static Num LongMultiply(Num a, Num b) {

		Num temp = new Num("0");
		long carry = 0;
		int i;
		int counter = 0;
		long[] result = new long[a.arr.length * b.arr.length * 2];
		if (a.len == 1) {
			temp = a;
			a = b;
			b = temp;
		}

		for (i = 0; i < a.arr.length; i++) {
			if (((a.arr[i] * b.arr[0]) + carry) > defaultBase - 1) {
				result[i] = ((a.arr[i] * b.arr[0]) + carry) % defaultBase;
				carry = ((a.arr[i] * b.arr[0]) + carry) / defaultBase;
				counter++;
			} else {
				result[i] = ((a.arr[i] * b.arr[0]) + carry);
				carry = 0;
				counter++;
			}

		}

		if (carry > 0) {
			result[i] = carry;
			counter++;

		}

		Num g = new Num(0);
		g.arr = result;
		g.len = counter;
		g.base = defaultBase;
		return g;
	}
	
	/* Function to calculate exponentiation using divide and conquer
	 * params: Num, long
	 * return: Num
	 */
	public static Num power(Num a, long n) {
		Num answer = new Num(1);
		if (n == 0) {
			return new Num(1);
		} else {
			answer = power(a, n/2);

			if (n % 2 == 0) {
				return product(answer, answer);
			} else {
				Num temp = product(answer, answer);
				return product(temp, a);
			}
		}
	}
	
	/* Helper function for division operation using divide and conquer
	 * params: Num, Num
	 * return: Num
	 */
	public static Num divideHelper(Num a, Num b) {
		if (a.compareTo(b) == -1) {
			return new Num(0);
		}
		
		if (b.compareTo(new Num(0)) == 0) {
			throw new IllegalArgumentException("Divide by zero error");
		}
		
		Num low = new Num(0);
		Num high = a;
		while (low.compareTo(high) < 0) {
			Num temp = subtract(high, low).by2();
			Num mid = add(low, temp);
			if (a.compareTo(product(b, mid)) >= 0 && a.compareTo(product(b, add(mid, new Num(1)))) < 0) {
				return mid;
			} else if (a.compareTo(product(b, mid)) == -1) {
				high = subtract(mid, new Num(1));
			} else {
				low = add(mid, new Num(1));
			}
		}
		return low;
	}
	
	/* Function to calculate the quotient of two Nums using divide and conquer
	 * params: Num, Num
	 * return: Num
	 */		 
	 public static Num divide(Num a, Num b)
	 {
		 Num result = new Num(0);
		 if((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative))
		 {
			 result = divideHelper(a,b);			 
		 }
		 else
		 {
			 result = divideHelper(a,b);
			 result.isNegative = true;
		 }		 
		 return result;
	 }
	 
	// return a%b
	public static Num mod(Num a, Num b) {
		Num quotient = divide(a, b);
		return subtract(a, product(quotient, b));
	}

	/* To calculate square root of a given Num type
	 * params: Num
	 * return: Num
	 */
	
	public static Num squareRoot(Num a) {
        if (a.compareTo(new Num(0)) == 0 || a.compareTo(new Num(1)) == 0)
            return a;
	Num result = new Num(0);
	Num high = a;
	Num low = new Num(1);
            Num mids=new Num(0);
	while (true) {
		Num mid = add(high, low).by2();
		//if (a.compareTo(product(mid, mid)) == 0)
                    mids=power(mid,2);
         if (mids.compareTo(a) == 0) 
                    {
			return mid;
		} else if (mid.compareTo(low) == 0 || mid.compareTo(high) == 0) {
			return mid;
		} else if(mids.compareTo(a) == 1) {
			 high = mid;
		}else
                    {
                        low = mid;
                    }
	}
}

	/* 
	 * Utility functions
	 * compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	 * otherwise 
	*/
	public int compareTo(Num other) {
		if (this.len > other.len) {
			return 1;
		} else if (this.len < other.len) {
			return -1;
		} else {
			for (int i = this.len - 1; i >= 0; i--) {
				if (this.arr[i] > other.arr[i]) {
					return 1;
				}
        else if (this.arr[i] < other.arr[i]) {
					return -1;
				}

			}
		}
		return 0;
	}

	/* Output using the format "base: elements of list ..."
	* For example, if base=100, and the number stored corresponds to 10965,
	* then the output is "100: 65 9 1"
	*/
	public void printList() {
		System.out.print(defaultBase + ":");
		for (int counter = 0; counter < this.len; counter++) {
			System.out.print(this.arr[counter] + " ");
		}
	}

	 //Return number to a string in base 10	
	public String toString() {
		Num temp;
        Num temp1=this;
        String str = new String();
		for (int j = temp1.arr.length - 1; j >= 0; j--) {
			str = str + temp1.arr[j];
		}
                Num newg=new Num(str);
                temp=newg.convertBase((int) 10);
                //temp=this;
                //Num temp=this.convertBaseTo10();
		String str1 = new String();
		for (int j = temp.arr.length - 1; j >= 0; j--) {
			str1 = str1 + temp.arr[j];
		}
		return str1;
	}

	public long base() {
		return base;
	}

	// Return number equal to "this" number, in base=newBase
	public Num convertBase(int newBase) {
		long[] array = new long[this.arr.length * 100];
		int count_zeros = 0;
		int count = 0;

		for (int i = this.len - 1; i >= 0; i--) {
			long carryover = this.arr[i];
			long tmp = 0;
			int j;

			/*
			 * start at the bottom of the array and work towards the top
			 *
			 * multiply the existing array value by 10, then add new value. carry over
			 * remainder as you work back towards the top of the array
			 */
			for (j = (array.length - 1); (j >= 0); j--) {
				tmp = (array[j] * defaultBase) + carryover;
				array[j] = tmp % newBase;
				carryover = tmp / newBase;
			}

		}
		for (long i : array) {

			if (i != 0) {
				break;
			}
			count_zeros++;
		}
		arr = new long[array.length - count_zeros];
		count = array.length - count_zeros;
		int k = -1;

		for (int i = array.length - 1; i >= count_zeros; i--) {
			arr[++k] = array[i];

		}

		Num g = new Num("0");
		g.arr = arr;
		g.len = arr.length;
		// System.out.println("Convert Base length"+g.len);
		return g;
	}

	/*
	 * Utility function to assist the splitting of array to use binary search
	 * params: none
	 * return: Num
	 */
	public Num by2() {
		if (this.compareTo(new Num(0)) == 0 || this.compareTo(new Num(1)) == 0) {
			return new Num(0);
		} else {
			long dividearr[] = new long[this.len];

			int idx = this.len - 1;
			long temp = this.arr[idx];
			while (temp < 2)
				temp = temp * defaultBase + (this.arr[--idx]);

			int counter = this.len - 1;
			int count = 0;
			while (true) {
				dividearr[counter] = temp / 2;
				count++;

				if (idx == 0)
					break;
				temp = (temp % 2) * defaultBase + this.arr[--idx]; 
				counter--;
			}

			long finalarr[] = new long[count];
			boolean flag = false;
			for (int i = 0; i < finalarr.length; i++) {
				if (i == 0 && dividearr[i] == 0 && this.len != count)
					flag = true;
				if (flag) {
					finalarr[i] = dividearr[i + 1];

				} else {
					finalarr[i] = dividearr[i];
				}
			}
			Num g = new Num("0");
			g.arr = finalarr;
			g.len = count;

			return g;
		}
	}

	/* Evaluate an expression in postfix and return resulting number
	* Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
	*a number: [1-9][0-9]*. There is no unary minus operator.
	*/
	public static Num evaluatePostfix(String[] expr) {
		HashSet<String> operators = new HashSet<>(Arrays.asList("*", "+", "-", "/", "%", "^"));
		Stack<Num> st = new Stack<>();
		Num result = new Num(0);
		for (String str : expr) {
			if (!operators.contains(str)) {
				st.push(new Num(str));
			} else {
				Num a = st.pop();
				Num b = st.pop();
				switch (str) {
				case "*":
					result = product(b, a);
					st.push(result);
					break;

				case "+":
					result = add(b, a);
					st.push(result);
					break;

				case "-":
					result = subtract(b, a);
					st.push(result);
					break;
				case "/":
					result = divide(b, a);
					st.push(result);
					break;
			 	case "^":
			 		String a1 = Arrays.toString(a.arr);
			 		long a2 = Long.parseLong(a1);
			 		result = power(b,a2);
			 		st.push(result);
					break;
				case "%":
					result = mod(b, a);
					st.push(result);
					break;
				}
			}
		}

		return result;
	}

	/* Evaluate an expression in infix and return resulting number
	 * Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
	 * a number: [1-9][0-9]*.  There is no unary minus operator.
	 */
	 public static Num evaluateInfix(String[] expr) {
		 HashSet<String> operators = new HashSet<>(Arrays.asList("*", "+", "-", "/", "%", "^", "(", ")"));
		 Stack<Num> st = new Stack<>();
		 Stack<String> ops = new Stack<String>(); 
		 Num result = new Num(0);
		 
		 for(String str : expr)
		 {
			 if(!operators.contains(str)) 
			 {
				st.push(new Num(str)); 
			 }
			 
			 else if(str.equalsIgnoreCase("("))
			 {
				 ops.push(str);
			 }
			 
			 else if(str.equalsIgnoreCase(")"))
			 {
				 while(ops.peek()!="(")
				 {
					 st.push(applyOp(ops.pop(), st.pop(), st.pop())); 		             
				 }
				 ops.pop(); 
			 }
			 		
			 else if(operators.contains(str))
			 {
				 while (!ops.empty() && hasPrecedence(str, ops.peek())) 
	                  st.push(applyOp(ops.pop(), st.pop(), st.pop())); 
				 ops.push(str);
			 }			 
		 }
		 
		 while (!ops.empty()) 
	            st.push(applyOp(ops.pop(), st.pop(), st.pop())); 
		 
		 return st.pop();	
	 }
	 
	 /* Helper function for calculating precedence of operators
	  * param: String, String
	  * return: boolean
	  */	 
	 public static boolean hasPrecedence(String op1, String op2) 
	 { 
	        if (op2.equals("(") || op2.equals(")")) 
	            return false; 
	        if ((op1.equals("*") || op1.equals("/") || (op1.equals("%"))) && (op2.equals("+") || op2.equals("-"))) 
	            return false; 
	        if ((op1.equals("+") || op1.equals("-")) && (op2.equals("^")))
	        	return false;
	        else
	            return true; 
	 }
	 
	 /*
	  * Function to calculate the required value by applying the appropriate operator
	  * params: String, Num, Num
	  * return: Num
	  */	 
	 public static Num applyOp(String string, Num num, Num num2) 
	 { 
		 	Num result = new Num(0);
	        switch (string) 
	        { 
	        case "+": 
	        	result = add(num2, num); 
	        	break;
	        case "-": 
	        	result = subtract(num2, num); 
	        	break;
	        case "*": 
	        	result = product(num2, num); 
	        	break;
	        case "/": 	           
	        	result = divide(num2, num); 
	        	break;
	        case "^":
	        	String a1="";
	        	for(int i = 0; i<num.len; i++)
	        	{
	        		a1=a1+String.format ("%d", num.arr[i]);
	        	}                    
                System.out.println("a1 string " + a1);
                long a2=Long.parseLong(a1);
                System.out.println(a1);
                result = power(num2,a2);
                break;
	        } 
	        return result; 
	 }
	 
	 /*
	  * Main function
	  */
	public static void main(String[] args) throws IllegalArgumentException {
		Num x = new Num("599861");
		Num z = power(x, 2897);
		z.printList();
		
		
		long startTime, endTime, elapsedTime, memAvailable, memUsed;
		startTime = System.currentTimeMillis();
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		memAvailable = Runtime.getRuntime().totalMemory();
		memUsed = memAvailable - Runtime.getRuntime().freeMemory();

		System.out.println();
		System.out.println("Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / "
				+ (memAvailable / 1048576) + " MB.");
	}
}