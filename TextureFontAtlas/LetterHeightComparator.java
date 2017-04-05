import java.util.Comparator;


public class LetterHeightComparator implements Comparator<Letter>{

	@Override
	public int compare(Letter o1, Letter o2) {
		// TODO Auto-generated method stub
		return o2.getHight() - o1.getHight();
	}
	

}
