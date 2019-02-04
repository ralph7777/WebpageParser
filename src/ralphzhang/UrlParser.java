package ralphzhang;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Core class. Implement the logic to parse and analyze a webpage content.
 * 
 * @author Ralph
 *
 */
public class UrlParser {

	private final String[] noise_words = { "from", "for", "to", "or", "out", "in", "by", "on", "of", "about", "with",
			"as", "at", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "not", "no", "and", "but", "all", "both", "none", "too", "again",
			"either", "can", "could", "would", "might", "may", "that", "this", "the", "i", "you", "your", "it", "he",
			"she", "his", "him", "her", "its", "how", "what", "why", "which", "if", "when", "whether", "has", "have",
			"do", "does", "did", "done", "be", "been", "is", "are", "was", "were", "will", "here", "there", "www",
			"com", "http", "https", "an", "co", "op", "alt", "item", "details", "contact", "reply", "try", "home",
			"please", "copyright", "email", "address" };

	private final int TITLE_BODY_RATIO = 5;
	private Map<String, WordNode> wordMap;
	private TreeSet<WordNode> wordSet;
	private Set<String> noiseSet;

	public UrlParser() {
		wordSet = new TreeSet<>();
		wordMap = new HashMap<>();
		noiseSet = new HashSet<>();
		for (String word : noise_words) {
			noiseSet.add(word);
		}
	}

	/**
	 * Parse the page and count word frequency. Build up map to store the
	 * information.
	 * 
	 * @param url
	 *            url string of a webpage
	 */
	public void parseText(String url) {
		Document doc;
		try {
			System.out.println("url: " + url);
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) Firefox/5.0" + "Chrome/26.0.1410.64 Safari/537.31")
					.maxBodySize(0).timeout(600000).get();
			String format = "[A-Za-z]+";
			Pattern pattern = Pattern.compile(format);

			// Parse page titile
			Elements title = doc.getElementsByTag("title");
			for (Element content : title) {
				String text = content.text();
				Matcher match = pattern.matcher(text);
				while (match.find()) {
					String word = match.group();
					insertWord(word, TITLE_BODY_RATIO - 1);
				}
			}

			// Parse page content
			String html = doc.text();
			Matcher match = pattern.matcher(html);
			while (match.find()) {
				String word = match.group();
				insertWord(word, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print out the top k words on page.
	 * 
	 * @param k
	 *            number of words to be printed out
	 */
	public void printTopK(int k) {
		// Input sanity check
		if (k <= 0) {
			System.out.println("Please provide a positive number.");
			return;
		}
		// Check if at least one parse is done
		if (wordMap.isEmpty()) {
			System.out.println("Please parse the page first.");
			return;
		}
		// Only update BST for the first time of invoking
		if (wordSet.isEmpty()) {
			sortByBST();
		}

		Iterator<WordNode> itr = wordSet.iterator();
		Set<String> dedup = new HashSet<>();
		System.out.println("Top words:");
		int count = 1;
		while (count <= k && itr.hasNext()) {
			WordNode node = itr.next();
			if (!dedup.add(node.getWord().toLowerCase())) {
				continue;
			}
			if (count % 5 == 0) {
				System.out.printf("%-14s\n", node.getWord());
			} else {
				System.out.printf("%-14s", node.getWord());
			}
			count++;
		}
	}

	// Store all WordNode into a BST to do the sorting
	private void sortByBST() {
		for (Entry<String, WordNode> entry : wordMap.entrySet()) {
			wordSet.add(entry.getValue());
		}
	}

	// Update hashmap according to input word and its weight
	private void insertWord(String word, int weight) {
		// Sanity check
		if (word == null || word.length() == 0 || weight < 0) {
			return;
			// throw new IllegalArgumentException("Illegal word detected");
		}
		if (!noiseSet.contains(word.toLowerCase())) {
			WordNode node = wordMap.get(word);
			if (node == null) {
				WordNode newNode = new WordNode(word, weight);
				wordMap.put(word, newNode);
			} else {
				node.setCount(node.getCount() + weight);
			}
		}
	}
}
