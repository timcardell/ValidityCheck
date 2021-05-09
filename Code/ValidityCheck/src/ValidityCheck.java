
import java.util.List;
import java.text.SimpleDateFormat;  
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner; 



public class ValidityCheck {
	int numberType;;

	public ValidityCheck( int type) {
		numberType = type;
	}


	void isValidNumber(String personalNumber){

		//reformat string to 10 digits
		boolean containLetters= doesContainLetters(personalNumber);
		if(containLetters) {
			System.out.println("Numret får inte innehålla bokstäver!");
			return;
		}
		String reformattedString = makeToTenDigits(personalNumber);
		if(reformattedString.length() < 10) {
			System.out.println("Personnumret inte korrekt: måste innehålla minst 10 siffror!");
			return;
		}
		else if(reformattedString.length() == 12) {
			System.out.println("Det valda året existerar ej!");
			return;
		}

		char [] charArray = stringToChar(reformattedString);
		int [] intArray = charToInt(charArray);

		//check if control digit match
		boolean isValidControlNumber = luhns(intArray);
		printValidity(reformattedString, numberType, isValidControlNumber);
	}

	static int[] charToInt(char [] arr ){
		int [] IntArr = new int [arr.length];

		for(int i=0; i<arr.length; i++) {
			IntArr[i] = Integer.parseInt(String.valueOf(arr[i])); 
		}
		return IntArr;
	}
	//convert string to char and place in array
	static char[] stringToChar(String str){
		char [] arr = new char [str.length()];
		for(int i=0; i<str.length(); i++) {
			arr[i] = str.charAt(i);

		}
		return arr;
	}

	//calculates the control digit and compares it to last element in array, return true if they match
	static boolean luhns(int [] array) {
		int size = array.length-1;
		if(array[array.length-1] == 0) return true;
		int sum = 0;
		boolean isControlDigitValid = false;
		if(array.length == 10) {
			for(int i=0; i<size; i++) {
				if ( i % 2 == 0  ) {
					//tempList[i] = array[i]*2;
					int newElementVal = array[i]*2;

					List<Integer> list = new ArrayList<Integer>();
					separateDigits(newElementVal, list);
					sum += sumList(list);
				}
				else {
					sum += array[i];
				}  
			}
		}

		if((10 - (sum % 10) % 10) == array[array.length-1]) {
			isControlDigitValid = true;
		}


		return isControlDigitValid;

	}
	//Separate digits
	static void separateDigits(int num, List<Integer> list) {

		if(num / 10 > 0) {
			separateDigits(num / 10, list);
		}
		list.add(num % 10);

	}
	//sum all list elemnts
	static int sumList(List<Integer> list) {
		int sum = 0; 

		for (int i : list) {
			sum = sum + i;
		}
		return sum;
	}

	//prints the validity of control digit and date for every number
	static void printValidity(String number, int numberType, boolean isValidControlDigit) {
		//personal number = 1
		//samordningsnummer = 2
		//organistation number = 3
		if(numberType == 1) {
			number = number.substring(0, number.length()-4);
			String DATE_FORMAT = "yyMMdd";
			try {
				DateFormat df = new SimpleDateFormat(DATE_FORMAT);
				df.setLenient(false);
				df.parse(number);


				if(isValidControlDigit) {
					System.out.println("Personnumret är korrekt!");
				}
				else {
					System.out.println("Personnumret inte korrekt: kontrollsifran är felaktig!");
				}

			} catch (ParseException e) {
				System.out.println("Personnumret inte korrekt: det valda datumet existerar inte!");
				if(!isValidControlDigit) {
					System.out.println("Kontrollsifran är felaktig!");
				}
			}
		}
		else if(numberType == 2) {

			number = number.substring(0, number.length()-4);

			String day = String.valueOf(Integer.parseInt(number.substring(4,6))-60);

			if(day.length() == 1) {
				day = "0"+ day;
			}
			String year = number.substring(0,2);
			String month = number.substring(2,4);

			String date = year+month+day;

			String DATE_FORMAT = "yyMMdd";
			try {
				DateFormat df = new SimpleDateFormat(DATE_FORMAT);
				df.setLenient(false);
				df.parse(date);
				if(isValidControlDigit) {
					System.out.println("Samordningsnummer inte korrekt!");
				}

			} catch (ParseException e) {
				System.out.println("Samordningsnummer inte korrekt: et valda datumet existerar inte!");
				if(!isValidControlDigit) {
					System.out.println("Kontrollsifran är felaktig!");
				}
			}
		}

		else {
			//additional cumpuations for org numbers
			if(Integer.parseInt(number.substring(2,4)) >= 20 && isValidControlDigit) {
				System.out.println("Organisationsnumret är korrekt!");
			}
			else {
				System.out.println("Organisationsnumret är inte korrekt: mellersta sektionen måste vara minst 20!");
			}
			if(!isValidControlDigit) {
				System.out.println("Kontrollsifran är felaktig!");
			}
		}
	}

	//make each string to a ten digit string, used in later computations
	static String makeToTenDigits(String number) {

		if(number.length() == 10) {
			//assume person not 100 years old, return initial ten digit array
			return number;
		}
		else if(number.length() > 10) {
			if(number.contains("+")) {
				number = number.replaceFirst("\\+", "");
			} 
			else if(number.contains("-")) {
				number = number.replaceFirst("\\-", "");
			}
			if(number.length() == 12) {
				//now if pers. if number has length, I delete two first digits repr., 18,19 or 20 
				if(Integer.parseInt(number.substring(0,2)) > 20){

					return number;
				}
				number = number.substring(2, number.length());
			}
		}

		return number;
	}

	//boolean to check if string contains any alphabetical characters
	static boolean doesContainLetters(String str) {
		str = str.toLowerCase();
		int counter = 0;
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char ch = charArray[i];
			if ((ch >= 'a' && ch <= 'z')) {
				counter = counter +1;
				return true;

			}
		}
		return false;
	}


	public static void main(String[] args) {

		//rerun validity check while true
		while(true) {
			System.out.print("Välj typ av nummer: \n1: Personnummer \n2: Samordningsnummer \n3: Organistationsnumber ");  
			Scanner sc= new Scanner(System.in); 
			String nr= sc.nextLine();  
			//personal number
			ValidityCheck valiidityCheck = new ValidityCheck(Integer.parseInt(nr));
			System.out.print("\n");
			System.out.print("Enter number:");  
			Scanner sc1= new Scanner(System.in);
			String number= sc1.nextLine(); 
			valiidityCheck.isValidNumber(number);
			System.out.print("\n");
		}
	}


}





