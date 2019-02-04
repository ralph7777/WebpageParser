package ralphzhang;

/**
 * Container for single word information.
 * 
 * @author Ralph
 *
 */
public class WordNode implements Comparable<WordNode> {
	String word;
	Integer count;

	public WordNode(String word, Integer count) {
		this.word = word;
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public int compareTo(WordNode other) {
		// Sort a wordnode based on its count
		if (this.count == other.count) {
			return this.word.compareTo(other.word);
		} else {
			return (this.count > other.count) ? (-1) : 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int code = 1;
		code = prime * code + ((word == null) ? 0 : word.hashCode());
		code = prime * code + ((count == null) ? 0 : count.hashCode());
		return code;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { // Check reference
			return true;
		}
		if (obj == null) { // Check null
			return false;
		}
		if (!(obj instanceof WordNode)) { // Check class
			return false;
		}
		WordNode other = (WordNode) obj;

		if (word == null) { // Check word field
			if (other.word != null) {
				return false;
			}
		} else if (!word.equals(other.word)) {
			return false;
		}
		if (count == null) { // Check count field
			if (other.count != null) {
				return false;
			}
		} else if (!count.equals(other.count)) {
			return false;
		}
		return true;
	}
}
