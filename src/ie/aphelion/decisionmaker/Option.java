/**
 * 
 */
package ie.aphelion.decisionmaker;

import java.io.Serializable;

/**
 * @author dude
 */
public class Option implements Serializable {

	private static final long serialVersionUID = 6359681170959811917L;

	private long id;
	private long ballotId;
	private String name;

	/**
	 * @param ballotId
	 * @param name
	 */
	public Option(String name) {
		super();
		this.name = name;
	}

	/**
	 * @param id
	 * @param ballotId
	 * @param name
	 */
	public Option(final long id, final long ballotId, final String name) {
		super();
		this.id = id;
		this.ballotId = ballotId;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the ballotId
	 */
	public long getBallotId() {
		return ballotId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @param ballotId
	 *            the ballotId to set
	 */
	public void setBallotId(final long ballotId) {
		this.ballotId = ballotId;
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
		result = prime * result + (int) (ballotId ^ (ballotId >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Option))
			return false;
		final Option other = (Option) obj;
		if (id != other.id)
			return false;
		if (ballotId != other.ballotId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		return String.format("Option [id=%s, ballotId=%s, name=%s]", id,
				ballotId, name);
	}
}
