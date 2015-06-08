/**
 * 
 */
package ie.aphelion.decisionmaker.dao.impl;

import ie.aphelion.decisionmaker.VoteData;
import ie.aphelion.decisionmaker.dao.VoteDataDAO;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 * 
 */
public class VoteDataDAOImpl extends AbstractDAO<VoteData> implements
		VoteDataDAO {

	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(VoteDataDAOImpl.class);

	private static final String CREATE = "INSERT INTO dm_vote_data SET voteid=:voteId, optionid=:optionId, value=:value";

	private static final String FIND = "SELECT id, voteid, optionid, value FROM dm_vote_data where voteid=%d order by optionid";

	private RowMapper<VoteData> rowMapper;

	/**
	 * @param rowMapper
	 */
	public VoteDataDAOImpl(final RowMapper<VoteData> rowMapper) {
		super();
		this.rowMapper = rowMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ie.aphelion.decisionmaker.dao.VoterDataDAO#add(ie.aphelion.decisionmaker
	 * .VoteData)
	 */
	@Override
	public long add(final VoteData data) {
		LOG.debug("Adding {}", data);
		this.store(CREATE, data);
		final long id = this.getLastInsertedId();
		LOG.debug("Added a vote data with {} identifier.", id);
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.aphelion.decisionmaker.dao.VoterDataDAO#findBy(long)
	 */
	@Override
	public List<VoteData> findBy(long voteId) {
		LOG.debug("Loading voting data associated with the vote id={}", voteId);
		final String sql = String.format(FIND, voteId);
		LOG.debug("Generated sql {}", sql);
		final List<VoteData> loaded = this.find(sql, this.rowMapper);
		LOG.debug("Loaded {} voting data", loaded == null ? 0 : loaded.size());
		return loaded;
	}
}