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
public class Ballot implements Serializable {

	private static final long serialVersionUID = 5101836347463513052L;

	private long id;
	private String name;
	private String description;
	private ArrayList<Option> options = new ArrayList<Option>();
	private BallotState state = BallotState.OPEN;
	private String status;
	private Date created;
	private Date updated;
	private boolean auditable;

	/**
	 * @param id
	 * @param name
	 * @param description
	 */
	public Ballot(final String name, final String description) {
		super();
		this.name = name;
		this.description = description;
	}
	
	/**
	 * @param id
	 * @param name
	 * @param description
	 */
	public Ballot(final long id, final String name, final String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the options
	 */
	public List<Option> getOptions() {
		return options;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}
	
	/**
	 * @param options the options to set
	 */
	public void setOptions(final List<Option> options) {
		this.options.clear();
		if (options != null && !options.isEmpty())
			this.options.addAll(options);
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(final Date created) {
		this.created = created;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the state
	 */
	public BallotState getState() {
		return state;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		this.status = this.state.name();
		return this.status;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(final BallotState state) {
		this.state = state;
	}
	
	/**
	 * @param votes the votes to set
	 */
	public void setVotes(final List<Vote> votes) {
		if (votes == null || votes.isEmpty()) {
			this.auditable = false;
		} else {
			this.auditable = true;
		}
	}
	
	/**
	 * @return the auditable
	 */
	public boolean isAuditable() {
		return this.auditable;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
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
		if (!(obj instanceof Ballot))
			return false;
		final Ballot other = (Ballot) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (state != other.state)
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Ballot [id=%s, name=%s, description=%s, options=%s, state=%s, hasVotes=%s, created=%s, updated=%s]",
						id, name, description, options, state, this.isAuditable(), created, updated);
	}


}