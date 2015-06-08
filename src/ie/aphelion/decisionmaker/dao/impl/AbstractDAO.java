package ie.aphelion.decisionmaker.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * Common data access object methods.
 * 
 * @author dude
 * @version 1.0 Mar 21, 2011
 */
abstract class AbstractDAO<T extends Serializable> extends
		AbstractSpringJDBCTemplateSupport {

	/** The logger instance for this class. */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDAO.class);

	/** Represents no result. */
	private static final Object NO_RESULT = null;

	/** The SQL statement to obtain a timestamp from the underlying data source. */
	private static final String GET_DATE_TIME_NOW = "select NOW();";
	/** The SQL statement to obtain the last inserted identifier. */
	private static final String LAST_INSERT_ID = "select LAST_INSERT_ID();";

	/**
	 * Finds the parameterized by a given value. Null value is returned if the
	 * no match has been found for the given value.
	 * 
	 * @param aSqlStatement
	 *            The statement to execute.
	 * @param aRowMapper
	 *            The row mapper.
	 * @param aParameterName
	 *            The name of the parameter corresponding to the SQL statement
	 *            and the value.
	 * @param aValue
	 *            The value to match.
	 * @return The parameterized object if found, otherwise null.
	 */
	@SuppressWarnings("unchecked")
	protected T find(final String aSqlStatement, final RowMapper<T> aRowMapper,
			final String aParameterName, final Object aValue) {

		LOG.debug("Looking for a ['{}'] parameter name and ['{}'] value.",
				aParameterName, aValue);

		final SqlParameterSource namedParameters = new MapSqlParameterSource(
				aParameterName, aValue);

		try {
			return this.getJdbcTemplate().queryForObject(aSqlStatement,
					aRowMapper, namedParameters);
		} catch (final EmptyResultDataAccessException ex) {
			LOG.debug(String.format("No results matching the given  "
					+ "value ['%s'] for ['%s'] parameter name. %s", aValue,
					aParameterName, ex.getMessage()));
			return (T) NO_RESULT;
		}

	}

	/**
	 * Finds the parameterized by a given SQL statement. Empty list is returned
	 * if the no match has been found.
	 * 
	 * @param aSqlStatement
	 *            The statement to execute.
	 * @param aRowMapper
	 *            The row mapper.
	 * @return The parameterized object if found, otherwise null.
	 */
	protected List<T> find(final String aSqlStatement,
			final RowMapper<T> aRowMapper) {

		LOG.debug("Executing following SQL [{}]", aSqlStatement);
		// Internally used list.
		final ArrayList<T> items = new ArrayList<T>();
		try {
			items.addAll(this.getJdbcTemplate()
					.query(aSqlStatement, aRowMapper));
		} catch (final EmptyResultDataAccessException ex) {
			LOG.debug(String.format("No results matching the given  "
					+ "value ['%s'] for ['%s'] parameter name. %s",
					ex.getMessage()));
			// Nothing is written to the list, returning an empty list.
			return Collections.unmodifiableList(items);
		}
		LOG.debug("Found [{}] items after SQL [{}] execution", items.size(),
				aSqlStatement);

		return items;
		//return Collections.unmodifiableList(items);
	}
	
	/**
	 * Gets a value the for the sql; null is returned
	 * if the no match has been found.
	 * 
	 * @param aSqlStatement
	 *            The statement to execute.
	 * @param aRowMapper
	 *            The row mapper.
	 * @return The parameterized object if found, otherwise null.
	 */
	protected T get(final String aSqlStatement,
			final RowMapper<T> aRowMapper) {
		LOG.debug("Executing following SQL [{}]", aSqlStatement);
		try {
			return this.getJdbcTemplate().queryForObject(aSqlStatement, aRowMapper);
		} catch (final EmptyResultDataAccessException ex) {
			LOG.debug(String.format("No results found for the given SQL statement [%s]. %s",
					aSqlStatement,
					ex.getMessage()));
			return null;
		}
	}

	/**
	 * Executes given SQL statement and stores the given information to the
	 * underlying data source.
	 * 
	 * @param aSQLStatement
	 *            The SQL statement to execute.
	 * @param aObject
	 *            The object containing values to be stored.
	 * @return The number of rows affected.
	 */
	protected int store(final String aSQLStatement, final T aObject) {

		LOG.debug("Storing {}.", aObject);

		final SqlParameterSource namedArguments = new BeanPropertySqlParameterSource(
				aObject);

		final int result = this.getJdbcTemplate().update(aSQLStatement,
				namedArguments);

		LOG.debug("Data have been successfully stored.");

		return result;
	}

	/**
	 * Executes given SQL statement and stores the given information to the
	 * underlying data source.
	 * 
	 * @param aSQLStatement
	 *            The SQL statement to execute.
	 * @param aArguments
	 *            The object array containing values to be stored.
	 * @return The number of rows affected.
	 */
	protected int store(final String aSQLStatement, final Object[] aArguments) {

		LOG.debug("Storing {}.", Arrays.toString(aArguments));

		final int result = this.getJdbcTemplate().update(aSQLStatement,
				aArguments);

		LOG.debug("Data have been successfully stored.");

		return result;
	}

	/**
	 * Obtains current data and time from the underlying data source.
	 * 
	 * @return The current data and time.
	 */
	protected Date getTimestamp() {
		return (Date) this.getJdbcTemplate()
				.queryForObject(GET_DATE_TIME_NOW, Date.class).clone();
	}

	/**
	 * Obtains last inserted identifier.
	 * 
	 * @return The last inserted identifier.
	 */
	protected Long getLastInsertedId() {
		return this.getJdbcTemplate().queryForLong(LAST_INSERT_ID);
	}

}
