/**
 * 
 */
package ie.aphelion.decisionmaker.dao.impl;

import ie.aphelion.decisionmaker.Vote;
import ie.aphelion.decisionmaker.dao.VoteDAO;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 */
public class VoteDAOImpl extends AbstractDAO<Vote> implements VoteDAO {
	
	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(VoteDAOImpl.class);

	private static final String CREATE = "INSERT INTO dm_vote SET ballotid=:ballotId, created=:created";

	private static final String FIND = "SELECT id, ballotId, created FROM dm_vote where ballotId=%d";

	private RowMapper<Vote> rowMapper;

	/**
	 * @param rowMapper
	 */
	public VoteDAOImpl(final RowMapper<Vote> rowMapper) {
		super();
		this.rowMapper = rowMapper;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.VoterDAO#add(ie.aphelion.decisionmaker.Vote)
	 */
	@Override
	public long add(final Vote vote) {
		LOG.debug("Adding {}", vote);
		final Date now = new Date();
		vote.setCreated(now);
		this.store(CREATE, vote);
		final long id = this.getLastInsertedId();
		LOG.debug("Added a vote with {} identifier.", id);
		return id;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.VoterDAO#findBy(long)
	 */
	@Override
	public List<Vote> findBy(final long ballotId) {
		LOG.debug(
				"Loading votes associated with the ballot with {} identifier",
				ballotId);
		final String sql = String.format(FIND, ballotId);
		LOG.debug("Generated sql {}", sql);
		final List<Vote> loaded = this.find(sql, this.rowMapper);
		LOG.debug("Loaded {} votes", loaded == null ? 0 : loaded.size());
		return loaded;
	}

}
