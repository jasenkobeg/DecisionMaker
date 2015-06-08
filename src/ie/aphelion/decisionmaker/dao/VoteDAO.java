/**
 * 
 */
package ie.aphelion.decisionmaker.dao;

import ie.aphelion.decisionmaker.Vote;

import java.util.List;

/**
 * @author dude
 */
public interface VoteDAO {

	long add(Vote vote);
	
	List<Vote> findBy(long ballotId);
}
