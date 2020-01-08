//Michael Orr 300290498
 
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
public class HangmanMain{
	public static void main(String[] args) throws Exception{
		System.out.println("Welcome to Hangman"+"\n");
		
		int guessLimit = 7;
		boolean guessed = false;
		boolean gameOver = false;
		int score = 0;
		String word = wordPick();
		word = word.toUpperCase();
		//System.out.println("The secret word is: " + word);
		ArrayList<Character> hidden = hidden(word);
		char[] array = word.toCharArray();
		ArrayList<Character> incorrectLetters = new ArrayList<>();
		int number = 0;
		
		System.out.print("Hidden Word: ");
		printArray(hidden);
		System.out.println("Incorrect Guesses: ");
		System.out.println("Guesses Left: " + guessLimit);
		System.out.println("Score: " + score);
		System.out.print("Enter next guess (type 'done' to exit): ");
		char guess = guessLetter(score);
		
		for (int i=0; i<word.length(); i++) {
			if(guess == array[i]) {
				guessed = true;
				hidden.set(i, guess);
				number++;
				score += 10;
			}
		}	
		if(guessed == false) {
			incorrectLetters.add(guess);
			guessLimit--;
			System.out.println("Sorry, there were no " + guess + "'s");
		}
		else {
			System.out.println("There are " + number + " " + guess + "'s");
		}
		System.out.println("\n");
		
		while(guessLimit > 0) {
			guessed = false;			
			number = 0;
			
			System.out.print("Hidden Word: ");
			printArray(hidden);
			System.out.print("Incorrect Guesses: ");
			if(incorrectLetters.size() >= 1) {
				printLetterArray(incorrectLetters);
			}
			System.out.println("Guesses Left: "+guessLimit);
			System.out.println("Score: "+score);			
			System.out.print("Enter next guess (type 'done' to exit): ");
			guess = guessLetter(score);
			
			boolean check = check(guess, incorrectLetters, hidden);
			if(check) {
				System.out.println("You already guessed " + guess + " before!");
				System.out.println("\n");
				continue;
			}
			
			for (int i=0; i<word.length(); i++) {
				if(guess == array[i]) {
					guessed = true;
					hidden.set(i, guess);
					number++;
					score += 10;
				}
			}			
			if(guessed == false) {
				incorrectLetters.add(guess);
				for(int j=1; j<incorrectLetters.size(); j++) {
					for(int i=1; i<incorrectLetters.size(); i++) {
						char y = incorrectLetters.get(i);
						char x = incorrectLetters.get(i-1);
						if(x > y) {
							char temp = x;
							incorrectLetters.set((i-1), y);
							incorrectLetters.set(i, temp);
						}	
					}
				}				
				guessLimit--;
				System.out.println("Sorry, there were no " + guess + "'s");
			}
			else {
				System.out.println("There are " + number + " " + guess + "'s");
			}
						
			if(guessLimit==0) {
				System.out.println("\n"+"Game Over. The answer was: " + word);
				System.out.println("\n");
				gameOver = true;
				break;
			}
			
			StringBuilder builder = new StringBuilder(hidden.size());
			for(Character ch : hidden) {
					builder.append(ch);
			}
			String answer = builder.toString();			
			if(word.equalsIgnoreCase(answer)){
				System.out.print("\n"+"Well done! The answer was: ");
				printArray(hidden);
				guessLimit = 7;
				word = wordPick();
				word = word.toUpperCase();
				array = word.toCharArray();
				hidden = hidden(word);
				incorrectLetters.clear();
				score += 100;
				score += guessLimit*30;				
			}
			System.out.println("\n");
		}
		if(gameOver) {
			highScore(score);
		}
	}
	public static boolean check (char guess, ArrayList<Character> incorrectLetters, ArrayList<Character> hidden) {
		boolean check = false;
		if(incorrectLetters.size() >= 1) {
			for(int i=0; i<incorrectLetters.size(); i++) {
				if(incorrectLetters.get(i) == guess) {
					check = true;
				}
			}
		}
		for(int i=0; i<hidden.size(); i++) {
			if(hidden.get(i) == guess) {
				check = true;
			}
		}	
		return check;
	}
	public static ArrayList<Character> hidden(String word) {
		ArrayList<Character> hidden = new ArrayList<>();
		for(int i=0; i<word.length(); i++) {
			hidden.add('-');
		}
		return hidden;
	}
	public static void printArray(ArrayList<Character> arrayList) {
		for(Character c : arrayList) {
			System.out.print(c);
		}
		System.out.println("");
	}
	public static void printLetterArray(ArrayList<Character> arrayList) {
		System.out.print(arrayList.get(0));
		for(int i=1; i<arrayList.size(); i++) {
			System.out.print(", " + arrayList.get(i));
		}
		System.out.println("");
	}
	public static char guessLetter(int score) {		
		char guess = 'a';
		try{
			Scanner input = new Scanner(System.in);
			String s = input.next();
			if(s.equalsIgnoreCase("done")) {
				System.out.println("\n"+"Thanks for playing!");
				System.out.println("\n");
				try{
					highScore(score);
				}
				catch(Exception e){}
				System.exit(0);
			}
			char[] g = s.toCharArray();
			guess = g[0];
			guess = Character.toUpperCase(guess);
			if(!Character.isLetter(guess) || s.length()>1) {
				throw new MyFormatException("You must enter a single letter: ");
			}		
		}
		catch(MyFormatException mfe) {
			System.out.print(mfe.getMessage());
			guess = guessLetter(score);
		}		
		return guess;
	}
	public static void highScore (int score) throws Exception{
		System.out.println("High Score List");		
		File highScores = new File("WordFiles/highScores.txt");
		FileWriter fw;
		System.out.println("Type 'clear' to create a new High Score List.");
		System.out.print("Type anything else to see the High Score List: ");
		Scanner input = new Scanner(System.in);
		String clear = input.next();
		if(!highScores.exists() || clear.equals("clear")) {
			fw = new FileWriter(highScores);
		}
		else {
			fw = new FileWriter(highScores, true);
		}
		BufferedWriter bw = new BufferedWriter(fw);		
		bw.append(score+"\n");
		bw.close();
		fw.close();
		
				
		FileReader fr = new FileReader(highScores);
		StringBuffer sb = new StringBuffer();
		ArrayList<String> result = new ArrayList<>();
		
		while (fr.ready()) {
			char c = (char)fr.read();
			if(c == '\n') {
				String s = sb.toString();
				s = s.replaceAll("\\s", "");
				s.trim();
				result.add(s);
				sb = new StringBuffer();
			} else {
				sb.append(c);
			}
		}
		if(sb.length() > 0) {
			String s = sb.toString();
			s = s.replaceAll("\\s", "");
			s.trim();
			result.add(s);
		}
		ArrayList<Integer> convert = new ArrayList<>();
		for(String stringValue : result) {
			try {
				convert.add(Integer.parseInt(stringValue));
			} catch(NumberFormatException nfe) {}
		}
		Collections.sort(convert);
		Collections.reverse(convert);
		if(convert.size()>0) {
			System.out.println("#1: " + convert.get(0) + "points");
		}
		if(convert.size()>1) {
			System.out.println("#2: " + convert.get(1) + "points");
		}
		if(convert.size()>2) {
			System.out.println("#3: " + convert.get(2) + "points");
		}
		if(convert.size()>3) {
			System.out.println("#4: " + convert.get(3) + "points");
		}
		if(convert.size()>4) {
			System.out.println("#5: " + convert.get(4) + "points");
		}
		fr.close();
		System.out.print("\n" + "Start Over? Type 'yes', or 'no': ");
		String y = input.next();
		String[] yarray = {y};
		boolean mihai = true;
		
		while(mihai) {			
			if(!y.equalsIgnoreCase("yes") && !y.equalsIgnoreCase("no")) {
				System.out.println("Sorry I didn't quite understand that???");
				System.out.print("\n" + "Start Over? Type 'yes', or 'no': ");
				y = input.next();
			}
			if(y.equalsIgnoreCase("yes")) {
				main(yarray);
			}
			if(y.equalsIgnoreCase("no")) {
				System.out.println("Goodbye!");
			}
			mihai = !y.equalsIgnoreCase("yes") && !y.equalsIgnoreCase("no");
		}
		
	}
	public static String wordPick() throws Exception{
		//Mostly everything in this method I got from the URL below.
		//https://www.baeldung.com/java-file-to-arraylist
		ArrayList<String> result = new ArrayList<>();
		File dictionary = new File("WordFiles/dictionary.txt");
		FileReader f = new FileReader(dictionary);
		StringBuffer sb = new StringBuffer();
				
		while (f.ready()) {
			char c = (char) f.read();
			if(c == '\n') {
				String s = sb.toString();
				s = s.replaceAll("\\s", "");
				result.add(s);
				sb = new StringBuffer();
			} else {
				sb.append(c);
			}
		}
		if(sb.length() > 0) {
			String s = sb.toString();
			s = s.replaceAll("\\s", "");
			result.add(s);
		}
		int number = (int)(Math.random()*120000);
		String word = result.get(number);
		f.close();
		return word;		
	}
}




























