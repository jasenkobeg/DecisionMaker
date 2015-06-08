/**
 * 
 */
package ie.aphelion.decisionmaker.service;

import java.util.List;

import ie.aphelion.decisionmaker.Ballot;
import ie.aphelion.decisionmaker.Option;

/**
 * @author dude
 */
public interface BallotService {
	
	long create(Ballot ballot);
	
	Ballot loadBallot(long identifier);
	
	void update(Ballot ballot);
	
	void close(long identifier);
	
	void deleteBallot(long identifier);
	
	List<Ballot> getBallots(int pageSize, int page);
	
	Option getOption(long identifier);

}
