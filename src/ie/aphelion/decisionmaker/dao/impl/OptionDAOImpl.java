/**
 * 
 */
package ie.aphelion.decisionmaker.dao.impl;

import ie.aphelion.decisionmaker.Option;
import ie.aphelion.decisionmaker.dao.OptionDAO;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 */
public class OptionDAOImpl extends AbstractDAO<Option> implements OptionDAO {

	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(BallotDAOImpl.class);

	private static final String CREATE = "INSERT INTO dm_option SET name=:name, ballotid=:ballotId";

	private static final String LOAD = "SELECT id, ballotId, name FROM dm_option where ballotId=%d";
	
	private static final String GET = "SELECT id, ballotId, name FROM dm_option where id=%d";

	private static final String DELETE = "DELETE FROM dm_option where ballotId=?";

	private RowMapper<Option> rowMapper;

	/**
	 * @param rowMapper
	 */
	public OptionDAOImpl(final RowMapper<Option> rowMapper) {
		super();
		this.rowMapper = rowMapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ie.aphelion.decisionmaker.dao.OptionDAO#create(ie.aphelion.decisionmaker
	 * .Option)
	 */
	@Override
	public long create(final Option option) {
		LOG.debug("Creating {}", option);
		this.store(CREATE, option);
		final long id = this.getLastInsertedId();
		LOG.debug("Created an option with {} identifier.", id);
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.aphelion.decisionmaker.dao.OptionDAO#load(long)
	 */
	@Override
	public List<Option> load(final long ballotId) {
		LOG.debug(
				"Loading options associated with the ballot with {} identifier",
				ballotId);
		final String sql = String.format(LOAD, ballotId);
		LOG.debug("Generated sql {}", sql);
		final List<Option> loaded = this.find(sql, this.rowMapper);
		LOG.debug("Loaded {} options", loaded == null ? 0 : loaded.size());
		return loaded;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.aphelion.decisionmaker.dao.OptionDAO#delete(long)
	 */
	@Override
	public void delete(final long ballotId) {
		LOG.debug(
				"Deleting options associated with the {} ballot identifier",
				ballotId);
		this.store(DELETE, new Object[] { ballotId });
		LOG.debug(
				"The options associated with the {} ballot identifier has been deleted",
				ballotId);
	}

	/*
	 * (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.dao.OptionDAO#getOption(long)
	 */
	@Override
	public Option getOption(final long optionId) {
		LOG.debug(
				"Getting the option associated with the option id {}",
				optionId);
		final String sql = String.format(GET, optionId);
		LOG.debug("Generated sql {}", sql);
		final Option option = this.get(sql, this.rowMapper);
		LOG.debug("Obtained {}", option);
		return option;
	}

}
