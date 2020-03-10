import java.io.*;
import java.util.Scanner;

/*
 * Mounceph Morssaoui
 * ID: 40097557
 * 
 */

public class ArithmeticCalculator {
	/*
	 * This program will read from a text file syntactically correct arithmetic expressions
	 * and solve for them in a different text file
	 * 
	 */

	public static void main(String[] args) {

		
		//Implementing a numbers Stack and an operator stack
		String[] valStk = new String[10];
		String[] opStk = new String[10];


		//Intialize and Declaring for reading/writing in text file
		PrintWriter pw = null;
		Scanner sc = null;

		try {
			pw = new PrintWriter(new FileOutputStream("testcasewithsolution.txt"));

			sc = new Scanner(new FileInputStream("testcase.txt"));
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);			
		}

		///////////////////////////////////////////////////////////////////////////////


		//As long as there is a line in reading file, on different file, write expression with result of expression
		while(sc.hasNextLine()) {
			String exp = sc.nextLine();
			
			String[] token = exp.split(" ");

			String totalExp = "" ;
			for (int i = 0; i<token.length;i++) 
				totalExp+=token[i] + " ";

			pw.println(totalExp);
			pw.print("ANSWER: ");
			pw.println(solve(valStk,opStk,token));
			pw.println("");
			
			valStk[0] = null;
			
		}
	
		////////////////////////////////////////////////////////////////////////////////
		pw.close();
		sc.close();
	}


	/*
	 *  METHODS TO IMPLEMENT MY OWN STACK
	 */
	
	
	//Calculates and returns size of stack
	private static int size(String[] O) {
		int c = 0;

		for (int i = 0; i < O.length; i++) {
			if (O[i]==null) {
				break;
			}
			c++;
		}

		return c;	
	}

	//Checks if stack is empty
	private static boolean isEmpty(String[] a) {
		return (size(a)==0);
	}
	
	
	//This method will push the arithmetic character into the top of the stack,
	//if it pushes will stack is filled, will create a new stack that is bigger and
	//copies the previous stack into new bigger stack
	private static void push(String[] O, String a) {

		if (size(O)==O.length) {
			String[] P = new String[O.length + 3];
			for (int i = 0; i<O.length;i++) {
				P[i]=O[i];
			}
			O = P;
			P = null;
		}else {
			O[size(O)] = a;			
		}


	}
	
	
	//Removes top element of the stack
	private static void pop(String [] O) {

		if (size(O) == 0) {
			return;
		}
		else {
			O[size(O)-1] = null;
		}


	}
	
	//Return String on top of the stack WITHOUT removing it
	private static String top(String[] P) {
		if (size(P)== 0)
			return null;
		return P[size(P)-1];
	}
	
	
	
	//This method verifies precedence of the operators, return true if the
	//referenced operator has more or equal precedence than of the top of the stack
	/*
	 * Precedence order (1 is highest: 6 is lowest)
	 * 
	 * 1. (
	 * 2. ^
	 * 3. *,/
	 * 4. +,-
	 * 5. >,>=, <, <=
	 * 6. ==, !=
	 * 
	 * 
	 */
	private static boolean precedence(String ref, String top) {
		int prec = 0 ;

		if (isValue(ref)) {
			return false;
		}

		switch (ref){

		case "(": ; 
		prec = 1;
		break;

		case "^": 
			prec = 2;
			break;
		case "*": ;case "/":
			prec = 3;	
			break;
		case "-": ;case "+": 
			prec = 4;
			break;
		case ">": ;case ">=":case "<": ;case "<=": 
			prec = 5;
			break;
		case "==": ;case "!=": 
			prec = 6;
			break;

		case "$":  ;
		prec = 7;
		break;	


		}

		int precR = prec; 

		switch (top){

		case "(": ;
		prec = 1;
		break;	
		case "^": 
			prec = 2;
			break;
		case "*": ;case "/":
			prec = 3;	
			break;
		case "-": ;case "+": 
			prec = 4;
			break;
		case ">": ;case ">=":case "<": ;case "<=": 
			prec = 5;
			break;
		case "==": ;case "!=": 
			prec = 6;
			break;


		}
		return (precR<prec);


	}
	//This method will do the mathematical operation depending on the operators,
	//Returns the mathematical value or the boolean value
	private static String doOps(double under, String ops, double top) {

		double value= 0;
		boolean val = false;
		String strValue = "";
		switch (ops) {
		case "^":
			value= Math.pow(under, top); 
			strValue = Double.toString(value);
			break;
		case "*": 
			value= (top*under);
			strValue = Double.toString(value);
			break;
		case "/":
			if (top == 0)
			{System.out.println("Cant divide by 0\nTerminating program");}
			else
			{
				value= under/top;
				strValue = Double.toString(value);
			}break;			
		case "+":
			value = under + top;
			strValue = Double.toString(value);
			break;
		case "-":
			value = under - top;
			strValue = Double.toString(value);
			break;
		case ">" : 
			val = (under>top);
			strValue = Boolean.toString(val);
			break;
		case ">=" : 
			val = (under>=top);
			strValue = Boolean.toString(val);
			break;
		case "<" : 
			val = (under<top);
			strValue = Boolean.toString(val);
			break;
		case "<=" : 
			val = (under<=top);
			strValue = Boolean.toString(val);
			break;
		case "==" : 
			val = (under==top);
			strValue = Boolean.toString(val);
			break;
		case "!=" : 
			val = (under!=top);
			strValue = Boolean.toString(val);
			break;
		}


		return strValue;
	}


	//Given a whole expression, will solve and return the answer of the expression in String type
	private static String solve(String[] valStk, String[] opStk, String[] exp) {

		boolean precedence = false;

		//Reads the whole expression
		for (int i = 0; (i < exp.length); i++) {

			//push in stack if literal is a number
			//enter else if it NOT a number
			if (isValue(exp[i])) {
				push(valStk,exp[i]);
				
				continue;
			}
			else{
				if (isEmpty(opStk)) {
					push(opStk,exp[i]);
					continue;
				}
				
				else if (exp[i].equalsIgnoreCase("(") ) {
					push(opStk,exp[i]);
					continue;
				}
				
				
				else if (exp[i].equalsIgnoreCase(")")) {
					solving(valStk,opStk,exp[i]);					
				}
				
				else {
					precedence = precedence(exp[i], top(opStk));
					if (precedence) {
						push(opStk,exp[i]);			
					}else {						
						solving(valStk,opStk,exp[i]);							
						push(opStk,exp[i]);
						
					}


				}

			}
		}


		//Solves equation knowing there will not be any brackets
		while (!isEmpty(opStk))
		solving(valStk,opStk,"$");	

		return top(valStk);
	}


	//Returns true is literal is a number
	private static boolean isValue(String literal) {
		return (literal.charAt(0) == '0' || literal.charAt(0) == '1' || literal.charAt(0) == '2' || literal.charAt(0) == '3' ||
				literal.charAt(0) == '4' || literal.charAt(0) == '5' ||literal.charAt(0) == '6' ||literal.charAt(0) == '7' ||literal.charAt(0) == '8' ||literal.charAt(0) == '9');
	}


	//Solves equation and makes sure that sub expressions in brackets are solved
	private static void solving(String[] valStk, String[] opStk, String hold) {

		double top;
		double under;
		String ans;
		String op = top(opStk);

		if (op.equalsIgnoreCase("(")) {			
			pop(opStk);
			return;
		}

		while ((!isEmpty(opStk) && !op.equalsIgnoreCase("(")) && (!precedence(hold,op)) ) {

			pop(opStk);
			top = Double.parseDouble(top(valStk));
			pop(valStk);
			under = Double.parseDouble(top(valStk));
			
			pop(valStk);			
			ans = doOps(under,op,top);
			op = top(opStk);
			push(valStk, ans);

		}

	}



}


