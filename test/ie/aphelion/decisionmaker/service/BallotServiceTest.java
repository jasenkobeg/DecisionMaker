/**
 * 
 */
package ie.aphelion.decisionmaker.service;

import ie.aphelion.decisionmaker.Ballot;
import ie.aphelion.decisionmaker.Option;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dude
 */

@ContextConfiguration({ "/test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class BallotServiceTest {

	@Autowired
	private BallotService service;
	
	@Test
	@Transactional
	public void testCreate() {
		final Ballot ballot = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option = new Option("test");
		final ArrayList<Option> options = new ArrayList<Option>();
		options.add(option);
		ballot.setOptions(options);
		final long id = this.service.create(ballot);
		Assert.assertTrue("Expected a new identifier but the dao returned " + id, id > 0);
	}
	
	@Test
	@Transactional
	public void testLoad() {
		final Ballot ballot = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option = new Option("test");
		final ArrayList<Option> options = new ArrayList<Option>();
		options.add(option);
		ballot.setOptions(options);
		final long id = this.service.create(ballot);
		final Ballot loaded = this.service.loadBallot(id);
		Assert.assertEquals("Loaded ballot is not as expected.", id, loaded.getId());
		Assert.assertEquals("Loaded ballot is not as expected.", ballot.getName(), loaded.getName());
		Assert.assertEquals("Loaded ballot is not as expected.", ballot.getDescription(), loaded.getDescription());
		Assert.assertEquals("Loaded ballot is not as expected.", ballot.getOptions().size(), loaded.getOptions().size());
	}
	
	@Test
	@Transactional
	public void testUpdate() {
		final Ballot ballot = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option = new Option("test");
		final ArrayList<Option> options = new ArrayList<Option>();
		options.add(option);
		ballot.setOptions(options);
		final long id = this.service.create(ballot);
		final Ballot loaded = this.service.loadBallot(id);
		Assert.assertNotNull("Could not load an ballot associated with the " + id + " identifier.", loaded);
		loaded.setOptions(null);
		this.service.update(loaded);
		final Ballot updated = this.service.loadBallot(id);
		Assert.assertTrue("Updated ballot should not contain any options", updated.getOptions().isEmpty());
	}
	
	@Test
	@Transactional
	public void testDelete() {
		final Ballot ballot = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option = new Option("test");
		final ArrayList<Option> options = new ArrayList<Option>();
		options.add(option);
		ballot.setOptions(options);
		final long id = this.service.create(ballot);
		Assert.assertTrue("Expected a new identifier but the dao returned " + id, id > 0);
		this.service.deleteBallot(id);
		final Ballot loaded = this.service.loadBallot(id);
		Assert.assertNull("It seems that the ballot has not been deleted. " + loaded, loaded);
	}
	
	
	
	@Test
	@Transactional
	public void testLoadAll() {
		final Ballot ballot = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option = new Option("test");
		final ArrayList<Option> options = new ArrayList<Option>();
		options.add(option);
		ballot.setOptions(options);
		final long id = this.service.create(ballot);
		
		final Ballot ballot2 = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option2 = new Option("test");
		final ArrayList<Option> options2 = new ArrayList<Option>();
		options2.add(option2);
		ballot2.setOptions(options2);
		final long id2 = this.service.create(ballot2);
		
		final List<Ballot> loaded = this.service.getBallots(10, 1);
		
		Assert.assertTrue("Some results might pre-exist in the database", loaded.size() > 2);
	}
	
	@Test
	@Transactional
	public void testGetOption() {
		final Ballot ballot = new Ballot(-1, "Test", "This is the create ballot test");
		final Option option = new Option("test");
		final ArrayList<Option> options = new ArrayList<Option>();
		options.add(option);
		ballot.setOptions(options);
		final long id = this.service.create(ballot);
		final Ballot loadedBallot = this.service.loadBallot(id);
		final Option loadedOption = this.service.getOption(loadedBallot.getOptions().get(0).getId());
		
		Assert.assertNotNull("The loaded option is not expected to be null", loadedOption);
		
	}
}
