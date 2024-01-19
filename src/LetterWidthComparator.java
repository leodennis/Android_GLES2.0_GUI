import java.util.Comparator;


public class LetterWidthComparator implements Comparator<Letter>{

	@Override
	public int compare(Letter o1, Letter o2) {
		return o2.getWidth() - o1.getWidth();
	}
	

}
