package beans;

import java.util.ArrayList;
import java.util.List;

public class Bids{
		
	private List<Bid> bids = new ArrayList<Bid>();

	public List<Bid> getBids() {
		return bids;
	}

	public void addBids(Bid bid) {
		this.bids.add(bid);
	}
	
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	
	
}
	
	