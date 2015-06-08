/**
 * 
 */
package ie.aphelion.decisionmaker.service.impl;

import ie.aphelion.decisionmaker.Ballot;
import ie.aphelion.decisionmaker.Option;
import ie.aphelion.decisionmaker.dao.BallotDAO;
import ie.aphelion.decisionmaker.dao.OptionDAO;
import ie.aphelion.decisionmaker.service.BallotService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dude
 *
 */
public class BallotServiceImpl implements BallotService {
	
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(BallotServiceImpl.class);

	private BallotDAO ballotDAO;
	private OptionDAO optionDAO;
	
	/**
	 * @param ballotDAO
	 */
	public BallotServiceImpl(final BallotDAO ballotDAO, final OptionDAO optionDAO) {
		super();
		this.ballotDAO = ballotDAO;
		this.optionDAO = optionDAO;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.service.BallotService#create(ie.aphelion.decisionmaker.Ballot)
	 */
	@Override
	public long create(Ballot ballot) {
		LOG.debug("Storing {}", ballot);
		final long ballotId = this.ballotDAO.create(ballot);
		this.createOptions(ballot.getOptions(), ballotId);
		return ballotId;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.service.BallotService#loadBallot(long)
	 */
	@Override
	public Ballot loadBallot(long identifier) {
		LOG.debug("Loading ballot associated with the {} identifier", identifier);
		final Ballot ballot = this.ballotDAO.load(identifier);
		// Sanity check
		if (ballot == null) {
			LOG.debug("Unable to find an ballot associated with the {} identifier", identifier);
			return null;
		}
		ballot.setOptions(this.optionDAO.load(identifier));
		return ballot;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.service.BallotService#findBallots(int, int)
	 */
	@Override
	public List<Ballot> getBallots(final int pageSize, final int page) {
		LOG.debug("Loading ballots; page size {} and page number {}", pageSize, page);
		final List<Ballot> ballots = this.ballotDAO.get(pageSize, page);
		LOG.debug("Found {} ballots", ballots == null ? 0 : ballots.size());
		return ballots;
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.service.BallotService#update(ie.aphelion.decisionmaker.Ballot)
	 */
	@Override
	public void update(Ballot ballot) {
		LOG.debug("Updating {}", ballot);
		this.ballotDAO.update(ballot);
		this.optionDAO.delete(ballot.getId());
		this.createOptions(ballot.getOptions(), ballot.getId());
	}

	/* (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.service.BallotService#deleteBallot(long)
	 */
	@Override
	public void deleteBallot(long identifier) {
		LOG.debug("Deleting ballot associated with the {} identifier", identifier);
		this.ballotDAO.delete(identifier);
		this.optionDAO.delete(identifier);
	}

	/**
	 * Stores options associated with an ballot.
	 * 
	 * @param options To store.
	 * @param ballotId The identifier of the ballot with options.
	 */
	private void createOptions(final List<Option> options, final long ballotId) {
		LOG.debug("Storing options {}", options);
		// Sanity check.
		if (options == null || options.isEmpty()) {
			LOG.debug("No option to store for the ballot associated with the {} identifier", ballotId);
			return;
		}
					
		for (final Option option : options) {
			option.setBallotId(ballotId);
			this.optionDAO.create(option);
		}		
	}

	/**
	 * Loads the options associated with the given identifier.
	 * @param identifier The option identifier.
	 */
	@Override
	public Option getOption(final long identifier) {
		LOG.debug("Obtaining the options associated with {} identifier", identifier);
		return this.optionDAO.getOption(identifier);
	}

	/**
	 * Close the ballot.
	 * @param identifier The ballot identifier.
	 */
	@Override
	public void close(final long identifier) {
		LOG.debug("Closing ballot associated with the {} identifier", identifier);
		this.ballotDAO.close(identifier);
	}

}