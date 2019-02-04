package ralphzhang;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The test driver. Its main function is defined as the start point of the program.
 * @author Ralph
 *
 */
public class TestUrl {
	
	/**
	 *  If user does not provide a url, run this function.
	 */
	private static void autoTest() {
		Scanner sc = new Scanner(System.in);
		final String test1 = "https://www.rei.com/blog/camp/how-to-introduce-your-indoorsy-friend-to-the-outdoors";
		final String test2 = "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/";
		final String test3 = "https://www.amazon.com/Cuisinart-CPT-122-Compact-2-SliceToaster/dp/"
			     + "B009GQ034C/ref=sr_1_1?s=kitchen&ie=UTF8&qid=1431620315&sr=1-1&keywords=toaster";
		while (true) {
			Integer k = 20;
			UrlParser parser = new UrlParser();
			System.out.print("1 - Test a page of Rei blog\n"
				+ "2 - Test a page of CNN news\n"
				+ "3 - Test a page of Amazon item\n"
				+ "4 - Test user input url\n"
				+ "5 - exit\n" + "Enter a number: ");
			String input = sc.nextLine();
			
			if (input.equals("1")) {
				parser.parseText(test1);
				parser.printTopK(k);
			} else if (input.equals("2")) {
				parser.parseText(test2);
				parser.printTopK(k);
			} else if (input.equals("3")) {
				parser.parseText(test3);
				parser.printTopK(k);
			} else if (input.equals("4")) {
				while (true) {
					System.out.println("Please enter a valid url (e.g., start from https://)");
					String url = sc.nextLine();
					// Call the validator to verify the url
					if (!UrlValidator.validate(url)) {
						continue;
					};
					System.out.println("Please enter number k of top k words (default 20):");
					String kStr = sc.nextLine();
					Pattern pattern = Pattern.compile("[0-9]*");
					if (pattern.matcher(kStr).matches()) {
						k = Integer.parseInt(kStr);
					} 
					parser.parseText(url);
					parser.printTopK(k);
					break;
				}
			} else {
				System.out.println("Goodbye");
				break;
			}
			System.out.print("\nPress enter to continue");
			sc.nextLine();
		}
	}

	/**
	 *  Starting point of program.
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			autoTest();
		} else {
			// Parse the input url and return top 20 words
			UrlParser parser = new UrlParser();
			String url = args[0];
			if (!UrlValidator.validate(url)) {
				return;
			};
			System.out.println("Top 20 words:");
			parser.parseText(url);
			parser.printTopK(20);
		}
	}
}
