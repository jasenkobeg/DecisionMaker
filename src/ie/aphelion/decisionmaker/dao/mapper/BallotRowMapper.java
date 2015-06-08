/**
 * 
 */
package ie.aphelion.decisionmaker.dao.mapper;

import ie.aphelion.decisionmaker.Ballot;
import ie.aphelion.decisionmaker.BallotState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 *
 */
public class BallotRowMapper implements RowMapper<Ballot> {
	
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(BallotRowMapper.class);

	@Override
	public Ballot mapRow(final ResultSet rs, final int counter) throws SQLException {
		
		LOG.debug("Mapping the ballot object");

		final long id = rs.getLong(1);
		final String name = rs.getString(2);
		final String description = rs.getString(3);
		final String status = rs.getString(4);
		final Date created = rs.getTimestamp(5);
		final Date updated = rs.getTimestamp(6);
		
		final BallotState state = BallotState.valueOf(status);
		
		final Ballot loaded = new Ballot(id, name, description);
		loaded.setCreated(created);
		loaded.setUpdated(updated);
		loaded.setState(state);

		LOG.debug("Mapping successfully completed");
		
		return loaded;
	}

}
