/**
 * 
 */
package ie.aphelion.decisionmaker.service.impl;

import ie.aphelion.decisionmaker.BallotAnalysis;
import ie.aphelion.decisionmaker.Vote;
import ie.aphelion.decisionmaker.VoteData;
import ie.aphelion.decisionmaker.dao.VoteDAO;
import ie.aphelion.decisionmaker.dao.VoteDataDAO;
import ie.aphelion.decisionmaker.engine.BallotEngine;
import ie.aphelion.decisionmaker.service.VoterService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dude
 * 
 */
public class VoterServiceImpl implements VoterService {

	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(VoterServiceImpl.class);

	private VoteDAO voteDAO;
	private VoteDataDAO voteDataDAO;

	/**
	 * @param voteDAO
	 */
	public VoterServiceImpl(final VoteDAO voteDAO, final VoteDataDAO voteDataDAO) {
		super();
		this.voteDAO = voteDAO;
		this.voteDataDAO = voteDataDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ie.aphelion.decisionmaker.service.VoterService#cast(ie.aphelion.decisionmaker
	 * .Vote)
	 */
	@Override
	public long cast(final Vote vote) {
		LOG.debug("Casting a {}", vote);
		final long voteId = this.voteDAO.add(vote);
		for (final VoteData data : vote.getVoteData()) {
			data.setVoteId(voteId);
			this.voteDataDAO.add(data);
		}
		vote.setId(voteId);
		LOG.debug("The {} has been casted", vote);
		return voteId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.aphelion.decisionmaker.service.VoterService#getVotesFor(long)
	 */
	@Override
	public List<Vote> getVotesFor(final long ballotId) {
		LOG.debug(
				"Getting current votes for the ballot associated with the {} identifier",
				ballotId);
		final List<Vote> votes = new ArrayList<Vote>();
		final List<Vote> loaded = this.voteDAO.findBy(ballotId);
		for (final Vote vote : loaded) {
			final List<VoteData> data = this.voteDataDAO.findBy(vote.getId());
			vote.setVoteData(data);
			votes.add(vote);
		}
		LOG.debug("Found {} votes associated with the {} identifier",
				(votes == null ? 0 : votes.size()), ballotId);
		return votes;
	}

	/*
	 * (non-Javadoc)
	 * @see ie.aphelion.decisionmaker.service.VoterService#getBallotEngine(java.util.List, int)
	 */
	@Override
	public BallotEngine getBallotEngine(final List<Vote> votes,
			final int numberOfBallotOptions) {
		LOG.debug("Getting ballot for {} and number of ballot options {}", votes, numberOfBallotOptions);

		// Sanity check
		if (votes == null || votes.isEmpty()) {
			LOG.warn("Unable to create a ballots; no votes are given.");
			return null;
		}

		// Create a ballot
		final int size = votes.size();
		final BallotEngine ballotEngine = new BallotEngine(size);
		ballotEngine.setNumPrefs(numberOfBallotOptions); // Number of options

		// Loop through votes
		for (final Vote vote : votes) {
			int index = 0;
			final Vector<String> voteVector = new Vector<String>();
			// Loop through vote data and add vote values to the vote vector
			for (final VoteData data : vote.getVoteData()) {
				voteVector.add(index++, String.valueOf(data.getValue()));
			}

			// Register votes with ballot
			ballotEngine.recordVote(voteVector);
		}

		LOG.debug("Ballot created {}", ballotEngine);
		return ballotEngine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ie.aphelion.decisionmaker.service.VoterService#getBallotAnalysis(ie.aphelion
	 * .decisionmaker.engine.BallotEngine, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BallotAnalysis getBallotAnalysis(final BallotEngine engine, final int numberOfBallotOptions) {
		LOG.debug("Creating a ballot from {} and number of ballot options", engine, numberOfBallotOptions);

		final BallotAnalysis ballot = new BallotAnalysis();
		ballot.setNumberOfVotes(engine.getNumVotes());
		ballot.setSimpleMajority(engine.simpleMajorityVote());
		ballot.setTwoRound(engine.twoRoundMV());
		ballot.setAlternative(engine.alternativeVote());
		ballot.setApproval(engine.approvalVote());
		ballot.setBordaCount(engine.borda());
		ballot.setMBC(engine.bordaP());
		ballot.setSerial(engine.serialVote());
		ballot.setCondorcet(engine.condorcet());

		// TODO finish
		//System.out.println( "TOTAL " + this.getTotal(ballot.getCondorcet(), numberOfBallotOptions));

		LOG.debug("Generated {}", ballot);
		return ballot;
	}

	public static void main(String[] args) {
		VoterServiceImpl c = new VoterServiceImpl(null, null);
		String[] list = new String[] {"0","3","0","3","0","3","3","0","0","3","0","3"};

		double[] total = c.getTotal(Arrays.asList(list), 4);
		for (int i=0; i<total.length; i++) {
			System.err.println(total[i]);
		}
	}
	
	private double[] getTotal(final List<String> condorcet, final int numberOfPreferances) {
		
		System.err.println(condorcet);
		final int max = Integer.valueOf(Collections.max(condorcet));
		final double[] total = new double[numberOfPreferances];
		final int size = condorcet.size();
		int index = 0;
		double numberOfOccurrence = 0.0d;
		for (int i=0; i<size; i++) {
			final int value = Integer.valueOf(condorcet.get(i));
			if (i % (numberOfPreferances - 1) == 1) {
				total[index++] = numberOfOccurrence;
				numberOfOccurrence = 0.0d;
				System.err.println("reinitialized");
			}
			if (value == max) {
				numberOfOccurrence = numberOfOccurrence + 1.0d;
				System.err.println("numberOfOccurrence "+numberOfOccurrence);
			}
		}
		
		/**
		double totalC = 0;
		String voteC[][] = new String[numberOfPreferances][numberOfPreferances];

		int element = 0;
		for (int y = 0; y < numberOfPreferances; y++) { // y is row
			for (int x = y; x < numberOfPreferances; x++) { // x is column
				// If x and y coords in table are equal, mark with 'dividing
				// line'
				final String value = condorcet.get(element++);
				if (x == y) {
					voteC[x][y] = " - ";
				} else {
					voteC[y][x] = value;
					voteC[x][y] = value;
				}
			}
		}
		// Now, iterate through table, adding up winners to see which is the
		// most popular option
		for (int y = 0; y < numberOfPreferances; y++) {
			for (int x = 0; x < numberOfPreferances; x++) {
				String s1 = voteC[y][x];
				String s2 = voteC[x][y];
				if (s1 != "" && s1 != " - " && s2 != "" & s2 != " ") {
					int i1 = Integer.parseInt(s1);
					int i2 = Integer.parseInt(s2);
					if (i1 > i2) {
						totalC += 1;
					}
					if (i1 == i2 && i1 > 0) {
						totalC += 0.5;
					}
				}
			}
		}
		System.err.println("TOTAL " + totalC);
		return totalC;
		*/
		return total;
	}

}