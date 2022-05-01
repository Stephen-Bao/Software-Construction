package edu.cmu.cs.cs214.rec04;

/**
 * InheritanceSortedIntList -- a variant of a SortedIntList that keeps 
 * count of the number of attempted element insertions (not to be confused
 * with the current size, which goes down when an element is removed) 
 * and exports an accessor (totalAdded) for this count.
 * 
 * @author Nora Shoemaker
 *
 */
public class InheritanceSortedIntList extends SortedIntList {
	// the number of attempted element insertions
	private int totalAdded;
	
	/**
	 * Gets the total number of attempted int insertions to the list since it
	 * was created.
	 * 
	 * @return total number of integers added to the list.
	 */
	public int getTotalAdded()
	{
		return this.totalAdded;
	}

	@Override
	public boolean add(int number) {
		boolean success = super.add(number);
		if (success) { totalAdded++; }

		return success;
	}

	@Override
	public boolean addAll(IntegerList list) {
		boolean success = false;

		for (int i = 0; i < list.size(); i++) {
			boolean flag = super.add(list.get(i));
			if (flag) {
				totalAdded++;
			}
			success |= flag;
		}

		return success;
	}

}









