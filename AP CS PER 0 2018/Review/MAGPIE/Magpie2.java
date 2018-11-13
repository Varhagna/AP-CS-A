package MAGPIE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * A program to carry on conversations with a human user. This version:
 * <ul>
 * <li>Uses advanced search for keywords</li>
 * <li>Will transform statements as well as react to keywords</li>
 * </ul>
 * 
 * @author Laurie White
 * @version April 2012
 *
 */
public class Magpie2 {
	/**
	 * Get a default greeting
	 * 
	 * @return a greeting
	 */
	public String getGreeting() {
		return "Good morrow, permit me to enagageth thee in clever discourse.";
	}

	/**
	 * Gives a response to a user statement
	 *
	 * @param statement
	 *            the user statement
	 * @return a response based on the rules given
	 * @throws FileNotFoundException
	 */
	public String getResponse(String statement) {
		String response = "";

		// Look for a two word (you <something> me) pattern
		int youPsn = findKeyword(statement, "you", 0);
		int iPsn = findKeyword(statement, "I", 0);
		
		if (statement.length() == 0) {
			response = "Is th're a bird at mine own window? Speak again, let thy beauty and thy voice penetrate the morning and the Heavens!";
		} else if (findKeyword(statement, "no") >= 0 || findKeyword(statement, "nein") >= 0 || findKeyword(statement, "niet") >= 0 || findKeyword(statement, "non") >= 0 ) {
			response = "Why arth thou so negatively inclined?";
		} else if (findKeyword(statement, "mother") >= 0 || findKeyword(statement, "father") >= 0 || findKeyword(statement, "sister") >= 0 || findKeyword(statement, "brother") >= 0) {
			response = "Let us engage in more discourse regarding thy kin!";
		} else if (hasName(statement)) {
			String name = getName(statement);
			Random random = new Random();
			int abc = random.nextInt(100);
			if(abc > 50) {
				response = name + " is an most wondrous p'rson.";
			} else if (abc < 50) {
				response = name + " is an horrendous p'rson.";
			} else {
				response = name + " hates you, you are not good for anything, bye.";
			}
		} else if (findKeyword(statement, "hello") >= 0 || findKeyword(statement, "good evening") >= 0 || findKeyword(statement, "good day") >= 0 || findKeyword(statement, "good afternoon") >= 0){
			response = "Hello! The most glorious of days we're having is it not?";
		} else if (findKeyword(statement, "don't" ) >= 0 || findKeyword(statement, "do not") >= 0 || findKeyword(statement, "dont") >= 0) {
			response = "Wherefore not?";
		} else if (findKeyword(statement, "pineapple") >= 0 && findKeyword(statement, "pizza") >= 0) {
			response = "HEATHEN, FAIR THEE BACK TO THE DEPTHS OF THE INFERNO FROM WHENCE YOU CAME, THOU HAS'T MIXED FOODS WHICH SHOULD NOT BE MIXED";
		}

		// Responses which require transformations
		 else if (youPsn >= 0 && findKeyword(statement, "me", youPsn) >= 0) {
			 response = transformYouMeStatement(statement);
		 } else if (iPsn >= 0 && findKeyword(statement, "you", iPsn) >= 0) {
			 response = transformIYouStatement(statement);
		 }
		else if (findKeyword(statement, "I want to", 0) >= 0) {
			response = transformIWantToStatement(statement);
		} else if (findKeyword(statement, "I want", 0) >= 0) {
			response = transformIWantStatement(statement);
		} else {
			response = getRandomResponse();
		}
		return response;

	}

	@SuppressWarnings("resource")
	private String getName(String statement) {
		File names = new File(
				"\\\\busd.local\\instruction\\Student-Home-Directories\\19vtovmasian\\git\\AP-CS-A\\AP CS PER 0 2018\\Review\\MAGPIE\\Names");

		try {
			Scanner input = new Scanner(names);
			while (input.hasNextLine()) {
				String name = input.nextLine();
				if (statement.contains(name)) {
					return name;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Noone";
	}

	@SuppressWarnings("resource")
	private boolean hasName(String statement) {
		File names = new File(
				"\\\\busd.local\\instruction\\Student-Home-Directories\\19vtovmasian\\git\\AP-CS-A\\AP CS PER 0 2018\\Review\\MAGPIE\\Names");
		Scanner input;
		try {
			input = new Scanner(names);
			while (input.hasNextLine()) {
				String name = input.nextLine();
				if (statement.contains(name)) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Take a statement with "I want to <something>." and transform it into "What
	 * would it mean to <something>?"
	 * 
	 * @param statement
	 *            the user statement, assumed to contain "I want to"
	 * @return the transformed statement
	 */
	private String transformIWantToStatement(String statement) {
		// Remove the final period, if there is one
		statement = statement.toLowerCase().trim();
		String lastChar = statement.substring(statement.length() - 1);
		if (lastChar.equals(".") || lastChar.equals("!")) {
			statement = statement.substring(0, statement.length() - 1);
		}
		int psn = findKeyword(statement, "I want to", 0);
		String restOfStatement = statement.substring(psn + 9).trim();
		return "What would it mean to " + restOfStatement + "?";
	}

	/**
	 * Take a statement with "I want <something>." and transform it into "Would you
	 * really be happy if you had <something>?"
	 * 
	 * @param statement
	 *            the user statement, assumed to contain "I want <statement>"
	 * @return the transformed statement
	 */
	private String transformIWantStatement(String statement) {
		// Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement.length() - 1);
		if (lastChar.equals(".") || lastChar.equals("!")) {
			statement = statement.substring(0, statement.length() - 1);
		}
		int psn = findKeyword(statement, "I want", 0);
		String restOfStatement = statement.substring(psn + 6).trim();
		return "Would'st thou be so content if you were to have " + restOfStatement + "?";
	}

	/**
	 * Take a statement with "you <something> me" and transform it into "What makes
	 * you think that I <something> you?"
	 * 
	 * @param statement
	 *            the user statement, assumed to contain "you" followed by "me"
	 * @return the transformed statement
	 */
	private String transformYouMeStatement(String statement) {
		// Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement.length() - 1);
		if (lastChar.equals(".")) {
			statement = statement.substring(0, statement.length() - 1);
		}

		int psnOfYou = findKeyword(statement, "you", 0);
		int psnOfMe = findKeyword(statement, "me", psnOfYou + 3);

		String restOfStatement = statement.substring(psnOfYou + 3, psnOfMe).trim();
		return "What compells thee think that I " + restOfStatement + " you?";
	}

	/**
	 * Take a statement with "you <something> me" and transform it into "What makes
	 * you think that I <something> you?"
	 * 
	 * @param statement
	 *            the user statement, assumed to contain "you" followed by "me"
	 * @return the transformed statement
	 */
	private String transformIYouStatement(String statement) {
		// Remove the final period, if there is one
		statement = statement.trim();
		String lastChar = statement.substring(statement.length() - 1);
		if (lastChar.equals(".")) {
			statement = statement.substring(0, statement.length() - 1);
		}

		int psnOfI = findKeyword(statement, "I", 0);
		int psnOfYou = findKeyword(statement, "you", psnOfI + 1);

		String restOfStatement = statement.substring(psnOfI + 1, psnOfYou).trim();
		return "For what do you " + restOfStatement + " me?";
	}

	/**
	 * Search for one word in phrase. The search is not case sensitive. This method
	 * will check that the given goal is not a substring of a longer string (so, for
	 * example, "I know" does not contain "no").
	 * 
	 * @param statement
	 *            the string to search
	 * @param goal
	 *            the string to search for
	 * @param startPos
	 *            the character of the string to begin the search at
	 * @return the index of the first occurrence of goal in statement or -1 if it's
	 *         not found
	 */
	private int findKeyword(String statement, String goal, int startPos) {
		String phrase = statement.trim();
		// The only change to incorporate the startPos is in the line below
		int psn = phrase.toLowerCase().indexOf(goal.toLowerCase(), startPos);

		// Refinement--make sure the goal isn't part of a word
		while (psn >= 0) {
			// Find the string of length 1 before and after the word
			String before = " ", after = " ";
			if (psn > 0) {
				before = phrase.substring(psn - 1, psn).toLowerCase();
			}
			if (psn + goal.length() < phrase.length()) {
				after = phrase.substring(psn + goal.length(), psn + goal.length() + 1).toLowerCase();
			}

			// If before and after aren't letters, we've found the word
			if (((before.compareTo("a") < 0) || (before.compareTo("z") > 0)) // before is not a letter
					&& ((after.compareTo("a") < 0) || (after.compareTo("z") > 0))) {
				return psn;
			}

			// The last position didn't work, so let's find the next, if there is one.
			psn = phrase.indexOf(goal.toLowerCase(), psn + 1);

		}

		return -1;
	}

	/**
	 * Search for one word in phrase. The search is not case sensitive. This method
	 * will check that the given goal is not a substring of a longer string (so, for
	 * example, "I know" does not contain "no"). The search begins at the beginning
	 * of the string.
	 * 
	 * @param statement
	 *            the string to search
	 * @param goal
	 *            the string to search for
	 * @return the index of the first occurrence of goal in statement or -1 if it's
	 *         not found
	 */
	private int findKeyword(String statement, String goal) {
		return findKeyword(statement, goal, 0);
	}

	/**
	 * Pick a default response to use if nothing else fits.
	 * 
	 * @return a non-committal string
	 */
	private String getRandomResponse() {
		final int NUMBER_OF_RESPONSES = 4;
		double r = Math.random();
		int whichResponse = (int) (r * NUMBER_OF_RESPONSES);
		String response = "";

		if (whichResponse == 0) {
			response = "What marvelous discourse, tell me more.";
		} else if (whichResponse == 1) {
			response = "Hmmm.";
		} else if (whichResponse == 2) {
			response = "Dost thou really thinketh so?";
		} else if (whichResponse == 3) {
			response = "You say not.";
		}

		return response;
	}

}