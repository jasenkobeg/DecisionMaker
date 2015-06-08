/**
 * 
 */
package ie.aphelion.decisionmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dude
 */
public class BallotSearchResults implements Serializable {

	private static final long serialVersionUID = 6663040975286918628L;

	private int pageNumber;
	private int numberOfResultsPerPage;
	private List<Ballot> ballots;
	

	/**
	 * @param pageNumber
	 * @param numberOfResultsPerPage
	 * @param ballots
	 */
	public BallotSearchResults(final int pageNumber, final int numberOfResultsPerPage,
			final List<Ballot> ballots) {
		super();
		this.pageNumber = pageNumber;
		this.numberOfResultsPerPage = numberOfResultsPerPage;

		// Set ballots 
		if (this.ballots == null) 
			this.ballots = new ArrayList<Ballot>();
		else 
			this.ballots.clear();
		
		if (ballots == null || ballots.isEmpty())
			return;
		
		this.ballots.addAll(ballots);
	}

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @return the ballots
	 */
	public List<Ballot> getBallots() {
		return ballots;
	}

	/**
	 * @return the numberOfResultsPerPage
	 */
	public int getNumberOfResultsPerPage() {
		return numberOfResultsPerPage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ballots == null) ? 0 : ballots.hashCode());
		result = prime * result + numberOfResultsPerPage;
		result = prime * result + pageNumber;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BallotSearchResults))
			return false;
		final BallotSearchResults other = (BallotSearchResults) obj;
		if (ballots == null) {
			if (other.ballots != null)
				return false;
		} else if (!ballots.equals(other.ballots))
			return false;
		if (numberOfResultsPerPage != other.numberOfResultsPerPage)
			return false;
		if (pageNumber != other.pageNumber)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 5;
		return String
				.format("BallotSearchResults [pageNumber=%s, numberOfResultsPerPage=%s, ballots (limited to 5 results)=%s]",
						pageNumber,
						numberOfResultsPerPage,
						ballots != null ? ballots.subList(0,
								Math.min(ballots.size(), maxLen)) : null);
	}
	
}