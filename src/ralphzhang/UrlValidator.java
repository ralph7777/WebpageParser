package ralphzhang;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class. Do pre-processing to check the availability of a url.
 * 
 * @author Ralph
 *
 */
public class UrlValidator {
	private static URL url;
	private static HttpURLConnection connect;
	private static int state = -1;
	private static final int RETRY_LIMIT = 5;

	/**
	 * Test if url is valid, test at most 5 times, if it fails mark it as
	 * invalid
	 * 
	 * @param urlStr
	 *            specified url of a website
	 * @return boolean value indicating the url is valid
	 */
	public static boolean validate(String urlStr) {
		int retry = 0;
		if (urlStr == null || urlStr.length() <= 0) {
			return false;
		}
		while (retry < RETRY_LIMIT) {
			try {
				url = new URL(urlStr);
				connect = (HttpURLConnection) url.openConnection();
				state = connect.getResponseCode();
				System.out.println("state code: " + state);
				if (state == 200) {
					System.out.println("url is valid.");
					return true;
				}
			} catch (Exception ex) {
				retry++;
				System.out.println("Retry " + retry + " times...");
				continue;
			}
			retry++;
		}
		return false;
	}
}
