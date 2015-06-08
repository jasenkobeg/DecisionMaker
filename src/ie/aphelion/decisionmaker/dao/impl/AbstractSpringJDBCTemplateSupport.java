package ie.aphelion.decisionmaker.dao.impl;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Support for the Spring's JDBC template. The alternative is to use spring's
 * JdbcDaoSupport.
 * 
 * @author dude
 * @version 1.0 Jan 16, 2011
 */
abstract class AbstractSpringJDBCTemplateSupport {

	/** The logger instance for this class. */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractSpringJDBCTemplateSupport.class);

	/** The JDBC template object. */
	private SimpleJdbcTemplate jdbcTemplate;

	/**
	 * Sets dataSource value to dataSource and compiles the stored procedures.
	 * 
	 * @param aDataSource
	 *            The dataSource to set.
	 */
	public void setDataSource(final DataSource aDataSource) {
		LOG.trace("Setting data source...");
		this.jdbcTemplate = new SimpleJdbcTemplate(aDataSource);
		LOG.trace("Data source is set.");
	}

	/**
	 * Returns the jdbcTemplate
	 * 
	 * @return the jdbcTemplate
	 */
	protected SimpleJdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
}
