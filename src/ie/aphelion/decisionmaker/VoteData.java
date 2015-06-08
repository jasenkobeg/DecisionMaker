/**
 * 
 */
package ie.aphelion.decisionmaker;

import java.io.Serializable;

/**
 * @author dude
 * 
 */
public class VoteData implements Serializable {

	private static final long serialVersionUID = -3337396741910465472L;

	private long id;
	private long voteId;
	private long optionId;
	private int value;
	private int points;

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
	 * @return the voteId
	 */
	public long getVoteId() {
		return voteId;
	}

	/**
	 * @param voteId
	 *            the voteId to set
	 */
	public void setVoteId(final long voteId) {
		this.voteId = voteId;
	}

	/**
	 * @return the optionId
	 */
	public long getOptionId() {
		return optionId;
	}

	/**
	 * @param optionId
	 *            the optionId to set
	 */
	public void setOptionId(final long optionId) {
		this.optionId = optionId;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final int value) {
		this.value = value;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @param points
	 *            the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (optionId ^ (optionId >>> 32));
		result = prime * result + points;
		result = prime * result + value;
		result = prime * result + (int) (voteId ^ (voteId >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VoteData))
			return false;
		VoteData other = (VoteData) obj;
		if (id != other.id)
			return false;
		if (optionId != other.optionId)
			return false;
		if (points != other.points)
			return false;
		if (value != other.value)
			return false;
		if (voteId != other.voteId)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("VoteData [id=%s, voteId=%s, optionId=%s, value=%s, points=%s]",
						id, voteId, optionId, value, points);
	}

}
