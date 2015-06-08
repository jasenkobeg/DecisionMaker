/**
 * 
 */
package ie.aphelion.decisionmaker.dao;

import java.util.List;

import ie.aphelion.decisionmaker.Ballot;

/**
 * @author dude
 */
public interface BallotDAO {

	long create(Ballot ballot);
	
	Ballot load(long identifier);
	
	void update(Ballot ballot);
	
	void close(long identifier);
	
	void delete(long identifier);
	
	List<Ballot> get(int pageSize, int page);
}
