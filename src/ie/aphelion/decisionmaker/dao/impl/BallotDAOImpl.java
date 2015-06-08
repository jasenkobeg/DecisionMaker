/**
 * 
 */
package ie.aphelion.decisionmaker.dao.impl;

import ie.aphelion.decisionmaker.Ballot;
import ie.aphelion.decisionmaker.BallotState;
import ie.aphelion.decisionmaker.dao.BallotDAO;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 *
 */
public class BallotDAOImpl extends AbstractDAO<Ballot> implements BallotDAO {

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(BallotDAOImpl.class);
	
	private static final String CREATE = "INSERT INTO dm_ballot SET name=:name, description=:description, status=:status, created=:created, updated=:updated";
	
	private static final String UPDATE = "UPDATE dm_ballot SET name=:name, description=:description, status=:status, updated=:updated where id=:id";
	
	private static final String CLOSE = "UPDATE dm_ballot SET status=:status, updated=:updated where id=:id";
	
	private static final String LOAD = "SELECT id, name, description, status, created, updated FROM dm_ballot where status!='DELETED' and id=%d";
	
	private static final String LOAD_PAGE = "SELECT id, name, description, status, created, updated FROM dm_ballot where status!='DELETED' ORDER BY id LIMIT %d, %d";
	
	private static final String DELETE = "DELETE FROM dm_ballot where id=?";

	private RowMapper<Ballot> rowMapper;
	
	/**
	 * @param mapper
	 */
	public BallotDAOImpl(final RowMapper<Ballot> mapper) {
		super();
		this.rowMapper = mapper;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.BallotDAO#create(ie.aphelion.decisionmaker.Ballot)
	 */
	@Override
	public long create(final Ballot ballot) {
		LOG.debug("Creating {}", ballot);
		final Date now = new Date();
		ballot.setCreated(now);
		ballot.setUpdated(now);
		ballot.setState(BallotState.OPEN);
		this.store(CREATE, ballot);
		final long id = this.getLastInsertedId();
		LOG.debug("Created ballot with {} identifier.", id);
		return id;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.BallotDAO#load(long)
	 */
	@Override
	public Ballot load(final long identifier) {
		LOG.debug("Loading ballot associated with the {} identifier", identifier);
		final String sql = String.format(LOAD, identifier);
		LOG.debug("Generated sql {}", sql);
		final Ballot loaded = this.get(sql, this.rowMapper);
		LOG.debug("Loaded {}", loaded);
		return loaded;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.BallotDAO#update(ie.aphelion.decisionmaker.Ballot)
	 */
	@Override
	public void update(final Ballot ballot) {
		LOG.debug("Updating {}", ballot);
		final Date now = new Date();
		ballot.setUpdated(now);
		this.store(UPDATE, ballot);
		LOG.debug("The {} ballot has been updated", ballot);
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.BallotDAO#delete(long)
	 */
	@Override
	public void delete(final long identifier) {
		LOG.debug("Deleting ballot associated with the {} identifier", identifier);
		this.store(DELETE, new Object[] {identifier});
		LOG.debug("The ballot associated with the {} identifier has been deleted", identifier);
	}

	/*
	 * (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.BallotDAO#find(int, int)
	 */
	@Override
	public List<Ballot> get(final int pageSize, final int page) {
		LOG.debug("Loading ballots; page size {} and page number {}", pageSize, page);
		final String sql = String.format(LOAD_PAGE, ((page - 1) * pageSize), pageSize);
		LOG.debug("Generated sql {}", sql);
		final List<Ballot> loaded = this.find(sql, this.rowMapper);
		LOG.debug("Found {} ballots", loaded == null ? 0 : loaded.size());
		return loaded;
	}

	/*
	 * (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.BallotDAO#close(long)
	 */
	@Override
	public void close(final long identifier) {
		LOG.debug("Closing ballot associated with the {} identifier", identifier);
		final Date now = new Date();
		final Ballot ballot = this.load(identifier);
		ballot.setState(BallotState.CLOSED);
		ballot.setUpdated(now);
		this.store(CLOSE, ballot);
		LOG.debug("The {} ballot has been updated", ballot);
	}

}