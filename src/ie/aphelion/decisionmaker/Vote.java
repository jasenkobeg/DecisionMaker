/**
 * 
 */
package ie.aphelion.decisionmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dude
 */
public class Vote implements Serializable {

	private static final long serialVersionUID = 2380006098123291683L;

	private long id;
	private long ballotId;
	private List<VoteData> voteData = new ArrayList<VoteData>(); 
	private Date created;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return the ballotId
	 */
	public long getBallotId() {
		return ballotId;
	}

	/**
	 * @param ballotId
	 *            the ballotId to set
	 */
	public void setBallotId(final long ballotId) {
		this.ballotId = ballotId;
	}


	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(final Date created) {
		this.created = created;
	}

	/**
	 * @return the voteData
	 */
	public List<VoteData> getVoteData() {
		return voteData;
	}

	/**
	 * @param voteData the voteData to set
	 */
	public void setVoteData(final List<VoteData> voteData) {
		if (voteData == null || voteData.isEmpty())
			return;
		
		this.voteData.clear();
		this.voteData.addAll(voteData);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (ballotId ^ (ballotId >>> 32));
		result = prime * result
				+ ((voteData == null) ? 0 : voteData.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vote))
			return false;
		Vote other = (Vote) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (ballotId != other.ballotId)
			return false;
		if (voteData == null) {
			if (other.voteData != null)
				return false;
		} else if (!voteData.equals(other.voteData))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Vote [id=%s, ballotId=%s, voteData=%s, created=%s]", id,
				ballotId, voteData, created);
	}

}
