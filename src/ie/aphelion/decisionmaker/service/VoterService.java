/**
 * 
 */
package ie.aphelion.decisionmaker.service;

import java.util.List;

import ie.aphelion.decisionmaker.BallotAnalysis;
import ie.aphelion.decisionmaker.Vote;
import ie.aphelion.decisionmaker.engine.BallotEngine;

/**
 * @author dude
 */
public interface VoterService {

	long cast(Vote vote);
	
	List<Vote> getVotesFor(long ballotId);
	
	BallotEngine getBallotEngine(List<Vote> votes, int numberOfBallotOptions);
	
	BallotAnalysis getBallotAnalysis(BallotEngine engine, int numberOfBallotOptions);
	
}
