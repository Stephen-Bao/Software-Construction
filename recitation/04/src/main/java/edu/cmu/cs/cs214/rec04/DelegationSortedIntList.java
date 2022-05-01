package edu.cmu.cs.cs214.rec04;

/**
 * DelegationSortedIntList -- a variant of a SortedIntList that keeps 
 * count of the number of attempted element insertions (not to be confused
 * with the current size, which goes down when an element is removed) 
 * and exports an accessor (totalAdded) for this count.
 * 
 * @author Nora Shoemaker
 *
 */
									  
// HINT: Take a look at the UML diagram to see what DelegationSortedIntList 
//       should implement.
public class DelegationSortedIntList implements IntegerList{
	// the number of attempted element insertions
	private int totalAdded;
	private SortedIntList list;

	public DelegationSortedIntList() {
		list = new SortedIntList();
	}

	@Override
	public boolean add(int num) {
		boolean success = list.add(num);
		if (success) { totalAdded++; }
		return success;
	}

	@Override
	public boolean addAll(IntegerList list) {
		boolean success = this.list.addAll(list);
		if(success) { totalAdded += list.size(); }
		return success;
	}

	@Override
	public int get(int index) {
		return list.get(index);
	}

	@Override
	public boolean remove(int num) {
		return list.remove(num);
	}

	@Override
	public boolean removeAll(IntegerList list) {
		return this.list.removeAll(list);
	}

	@Override
	public int size() {
		return list.size();
	}
	/*
	 * Gets the total number of attempted int insertions to the list since it.
	 * was created.
	 *
	 * @return total number of integers added to the list.
	 */
	public int getTotalAdded()
	{
		return totalAdded;
	}

}









