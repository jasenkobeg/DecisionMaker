/**
 * 
 */
package ie.aphelion.decisionmaker.dao;

import ie.aphelion.decisionmaker.Option;

import java.util.List;

/**
 * @author dude
 *
 */
public interface OptionDAO {


	long create(Option option);
	
	List<Option> load(long ballotId);
	
	void delete(long ballotId);
	
	Option getOption(long optionId);
}
