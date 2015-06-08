/**
 * 
 */
package ie.aphelion.decisionmaker.service;

import ie.aphelion.decisionmaker.Vote;
import ie.aphelion.decisionmaker.VoteData;
import ie.aphelion.decisionmaker.engine.BallotEngine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dude
 *
 */

@ContextConfiguration({ "/test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class VoterServiceTest {

	@Autowired
	private VoterService service;
	
	@Test
	@Transactional
	public void testCreate() {
		
		final Vote vote = new Vote();
		vote.setBallotId(99);
		vote.setVoteData(this.getVoteDataList(1, 3, 2, 2, 1, 1));
		
		final long id = this.service.cast(vote);
		Assert.assertTrue("Expected a new identifier but the dao returned " + id, id > 0);
	}
	
	@Test
	@Transactional
	public void testLoad() {
		
		final long ballotId = 99;
		
		this.registerVotes();
		
		final List<Vote> loadedVotes = this.service.getVotesFor(ballotId);
		
		for (final Vote v :loadedVotes) {
			Assert.assertEquals("Loaded vote is not as expected.", ballotId, v.getBallotId());
			Assert.assertNotNull("Loaded vote is not as expected.", v.getCreated());
			Assert.assertNotNull("Loaded vote is not as expected.", v.getVoteData());
		}
	}
	
	@Test
	@Transactional
	public void testConversionToBallot() {
		
		final long ballotId = 99;
		this.registerVotes();
		final List<Vote> loadedVotes = this.service.getVotesFor(ballotId);
		final BallotEngine b = this.service.getBallotEngine(loadedVotes, 3);
		
		final Vector<String> v1 = new Vector<String>(3);
		v1.add(0, "3");
		v1.add(1, "2");
		v1.add(2, "1");
		final Vector<String> v2 = new Vector<String>(3);
		v2.add(0, "1");
		v2.add(1, "2");
		v2.add(2, "3");
		final Vector<String> v3 = new Vector<String>(3);
		v3.add(0, "2");
		v3.add(1, "1");
		v3.add(2, "3");
		
		final HashMap<Integer, Vector<String>> voteAndAuditVoteExpected = new HashMap<Integer, Vector<String>>();
		voteAndAuditVoteExpected.put(0, v1);
		voteAndAuditVoteExpected.put(1, v2);
		voteAndAuditVoteExpected.put(2, v3);
		
		
		DecimalFormat d = new DecimalFormat("0.00");
		Vector t = new Vector(0, 1);
		Vector p = new Vector(0, 1);
		int total = 0;
		
		Assert.assertEquals("[[3, 2, 1], [1, 2, 3], [2, 1, 3]]", b.toString());
		
		for ( int i = 0; i < b.getNumVotes(); i++ ) {
			Assert.assertEquals(voteAndAuditVoteExpected.get(i), b.returnVote(i));
			Assert.assertEquals(voteAndAuditVoteExpected.get(i), b.auditVote(i));
//			System.out.println("returnPoints(" + i +") : " + b.returnPoints(i));
//			System.out.println("returnBPoints(" + i +"): " + b.returnBPoints(i) + "\n");
		}
		
		final Vector<String> tallySVM = new Vector<String>(3);
		tallySVM.add(0, "1");
		tallySVM.add(1, "1");
		tallySVM.add(2, "1");
		final Vector<String> tallySVMValues = new Vector<String>(3);
		tallySVMValues.add(0, "33.33%");
		tallySVMValues.add(1, "33.33%");
		tallySVMValues.add(2, "33.33%");
		
		t = (Vector)b.simpleMajorityVote();
		p.clear(); total = 0;
		Assert.assertEquals(tallySVM, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tallySVMValues, p);

		
		final Vector<String> tallyAltV = new Vector<String>(3);
		tallyAltV.add(0, "1");
		tallyAltV.add(1, "2");
		tallyAltV.add(2, "0");
		final Vector<String> tallyAltVValues = new Vector<String>(3);
		tallyAltVValues.add(0, "33.33%");
		tallyAltVValues.add(1, "66.67%");
		tallyAltVValues.add(2, "0.00%");
		
		t = (Vector)b.alternativeVote();
		p.clear(); total = 0;
		Assert.assertEquals(tallyAltV, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tallyAltVValues, p);

		
		final Vector<String> tallyAppV = new Vector<String>(3);
		tallyAppV.add(0, "3");
		tallyAppV.add(1, "3");
		tallyAppV.add(2, "3");
		final Vector<String> tallyAppVValues = new Vector<String>(3);
		tallyAppVValues.add(0, "33.33%");
		tallyAppVValues.add(1, "33.33%");
		tallyAppVValues.add(2, "33.33%");
		
		t = (Vector)b.approvalVote();
		p.clear(); total = 0;
		Assert.assertEquals(tallyAppV, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tallyAppVValues, p);

		final Vector<String> tallyCond = new Vector<String>(6);
		tallyCond.add(0, "1");
		tallyCond.add(1, "2");
		tallyCond.add(2, "2");
		tallyCond.add(3, "1");
		tallyCond.add(4, "2");
		tallyCond.add(5, "1");
		final Vector<String> tallyBorda = new Vector<String>(3);
		tallyBorda.add(0, "6");
		tallyBorda.add(1, "7");
		tallyBorda.add(2, "5");
		final Vector<String> tallyBordaValues = new Vector<String>(3);
		tallyBordaValues.add(0, "33.33%");
		tallyBordaValues.add(1, "38.89%");
		tallyBordaValues.add(2, "27.78%");
		
		t = (Vector)b.condorcet();
		p.clear(); total = 0;
		Assert.assertEquals(tallyCond, t);

		t = (Vector)b.borda();
		p.clear(); total = 0;
		Assert.assertEquals(tallyBorda, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tallyBordaValues, p);

		
		final Vector<String> tallyBordaP = new Vector<String>(3);
		tallyBordaP.add(0, "6");
		tallyBordaP.add(1, "7");
		tallyBordaP.add(2, "5");
		final Vector<String> tallyBordaPValues = new Vector<String>(3);
		tallyBordaPValues.add(0, "33.33%");
		tallyBordaPValues.add(1, "38.89%");
		tallyBordaPValues.add(2, "27.78%");
		
		t = (Vector)b.bordaP();
		p.clear(); total = 0;
		Assert.assertEquals(tallyBordaP, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tallyBordaPValues, p);

		
		final Vector<String> tallySerial = new Vector<String>(3);
		tallySerial.add(0, "1");
		tallySerial.add(1, "2");
		tallySerial.add(2, "0");
		final Vector<String> tallySerialValues = new Vector<String>(3);
		tallySerialValues.add(0, "33.33%");
		tallySerialValues.add(1, "66.67%");
		tallySerialValues.add(2, "0.00%");
		
		t = (Vector)b.serialVote();
		p.clear(); total = 0;
		Assert.assertEquals(tallySerial, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tallySerialValues, p);
		
		final Vector<String> tally2Round = new Vector<String>(3);
		tally2Round.add(0, "1");
		tally2Round.add(1, "2");
		tally2Round.add(2, "0");
		final Vector<String> tally2RoundValues = new Vector<String>(3);
		tally2RoundValues.add(0, "33.33%");
		tally2RoundValues.add(1, "66.67%");
		tally2RoundValues.add(2, "0.00%");

		t = (Vector)b.twoRoundMV();
		p.clear(); total = 0;
		Assert.assertEquals(tally2Round, t);
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			total = total + Integer.parseInt(s1);
		}
		for ( int i = 0; i < t.size(); i++ )
		{
			String s1 = (String)t.elementAt(i);
			double i1 = Float.parseFloat(s1);
			p.add(String.valueOf(d.format(i1 / total * 100)) + "%");
		}
		Assert.assertEquals(tally2RoundValues, p);
		
	}
	
	private void registerVotes() {

		final long ballotId = 99;
		
		final Vote vote = new Vote();
		vote.setBallotId(ballotId);
		vote.setVoteData(this.getVoteDataList(2, 2, 3, 1, 1, 3));
		final long id = this.service.cast(vote);
		Assert.assertTrue("Expected a new identifier but the dao returned " + id, id > 0);

		final Vote vote2 = new Vote();
		vote2.setBallotId(ballotId);
		vote2.setVoteData(this.getVoteDataList(1, 1, 2, 2, 3, 3));
		final long id2 = this.service.cast(vote2);
		Assert.assertTrue("Expected a new identifier but the dao returned " + id2, id2 > 0);
		
		final Vote vote3 = new Vote();
		vote3.setBallotId(ballotId);
		vote3.setVoteData(this.getVoteDataList(1, 2, 3, 3, 2, 1));
		final long id3 = this.service.cast(vote3);
		Assert.assertTrue("Expected a new identifier but the dao returned " + id3, id3 > 0);
	}
		
	private VoteData getVoteData(final long optionId, final int value) {
		final VoteData data = new VoteData();
		data.setOptionId(optionId);
		data.setValue(value);
		return data;
	}
	
	private List<VoteData> getVoteDataList(final long optionId, final int value, final long optionId1, final int value1, final long optionId2, final int value2) {
		final ArrayList<VoteData> data = new ArrayList<VoteData>();
		data.add(this.getVoteData(optionId, value));
		data.add(this.getVoteData(optionId1, value1));
		data.add(this.getVoteData(optionId2, value2));
		return data;
	}
	
}