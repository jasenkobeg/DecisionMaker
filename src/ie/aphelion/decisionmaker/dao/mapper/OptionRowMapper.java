/**
 * 
 */
package ie.aphelion.decisionmaker.dao.mapper;

import ie.aphelion.decisionmaker.Option;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author dude
 *
 */
public class OptionRowMapper implements RowMapper<Option> {
	
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(OptionRowMapper.class);


	@Override
	public Option mapRow(final ResultSet rs, final int counter) throws SQLException {
		
		LOG.debug("Mapping the option object");

		final long id = rs.getLong(1);
		final long ballotId = rs.getLong(2);
		final String name = rs.getString(3);
		
		final Option loaded = new Option(id, ballotId, name);

		LOG.debug("Mapping successfully completed");
		
		return loaded;
	}

}
