package de.tilman_neumann.util;

import java.util.ArrayList;

public class IndexList extends ArrayList<Integer> {

	private static final long serialVersionUID = 2635776541035770856L;

	public IndexList() {
		super();
	}
	
	public IndexList(int initialLoadSize) {
		super(initialLoadSize);
	}
}
