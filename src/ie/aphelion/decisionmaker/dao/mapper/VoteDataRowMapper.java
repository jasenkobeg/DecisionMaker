/**
 * 
 */
package ie.aphelion.decisionmaker.dao.mapper;

import ie.aphelion.decisionmaker.VoteData;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 *
 */
public class VoteDataRowMapper implements RowMapper<VoteData> {
	
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(VoteDataRowMapper.class);


	@Override
	public VoteData mapRow(final ResultSet rs, final int counter) throws SQLException {
		
		LOG.debug("Mapping the vote data object");

		final long id = rs.getLong(1);
		final long voteId = rs.getLong(2);
		final long optionId = rs.getLong(3);
		final int value = rs.getInt(4);
		
		final VoteData data = new VoteData();
		data.setId(id);
		data.setVoteId(voteId);
		data.setOptionId(optionId);
		data.setValue(value);

		LOG.debug("Mapping successfully completed");
		
		return data;
	}

}
