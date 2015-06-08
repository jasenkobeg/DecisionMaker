/**
 * 
 */
package ie.aphelion.decisionmaker.dao;

import ie.aphelion.decisionmaker.VoteData;

import java.util.List;

/**
 * @author dude
 */
public interface VoteDataDAO {

	long add(VoteData data);
	
	List<VoteData> findBy(long voteId);
}
