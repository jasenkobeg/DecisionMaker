/**
 * 
 */
package ie.aphelion.decisionmaker.dao.mapper;

import ie.aphelion.decisionmaker.Vote;

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
public class VoteRowMapper implements RowMapper<Vote> {
	
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(VoteRowMapper.class);


	@Override
	public Vote mapRow(final ResultSet rs, final int counter) throws SQLException {
		
		LOG.debug("Mapping the vote object");

		final long id = rs.getLong(1);
		final long ballotId = rs.getLong(2);
		final Date created = rs.getTimestamp(3);
		
		final Vote vote = new Vote();
		vote.setId(id);
		vote.setBallotId(ballotId);
		vote.setCreated(created);

		LOG.debug("Mapping successfully completed");
		
		return vote;
	}

}
