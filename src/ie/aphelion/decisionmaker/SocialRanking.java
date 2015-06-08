/**
 * 
 */
package ie.aphelion.decisionmaker;

import java.io.Serializable;

/**
 * @author dude
 * 
 */
public class SocialRanking implements Serializable {

	private static final long serialVersionUID = -8041953746689453911L;
	private String option;
	private int pointsReceived;
	private String consensusCoefficient;

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option
	 *            the option to set
	 */
	public void setOption(final String option) {
		this.option = option;
	}

	/**
	 * @return the pointsReceived
	 */
	public int getPointsReceived() {
		return pointsReceived;
	}

	/**
	 * @param pointsReceived
	 *            the pointsReceived to set
	 */
	public void setPointsReceived(final int pointsReceived) {
		this.pointsReceived = pointsReceived;
	}

	/**
	 * @return the consensusCoefficient
	 */
	public String getConsensusCoefficient() {
		return consensusCoefficient;
	}

	/**
	 * @param consensusCoefficient
	 *            the consensusCoefficient to set
	 */
	public void setConsensusCoefficient(final String consensusCoefficient) {
		this.consensusCoefficient = consensusCoefficient;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("SocialRanking [option=%s, pointsReceived=%s, consensusCoefficient=%s]",
						option, pointsReceived, consensusCoefficient);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((consensusCoefficient == null) ? 0 : consensusCoefficient
						.hashCode());
		result = prime * result + ((option == null) ? 0 : option.hashCode());
		result = prime * result + pointsReceived;
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
		if (!(obj instanceof SocialRanking))
			return false;
		SocialRanking other = (SocialRanking) obj;
		if (consensusCoefficient == null) {
			if (other.consensusCoefficient != null)
				return false;
		} else if (!consensusCoefficient.equals(other.consensusCoefficient))
			return false;
		if (option == null) {
			if (other.option != null)
				return false;
		} else if (!option.equals(other.option))
			return false;
		if (pointsReceived != other.pointsReceived)
			return false;
		return true;
	}

}