/**
 * 
 */
package ie.aphelion.decisionmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dude
 */
public class BallotAnalysis implements Serializable {
	
	private static final long serialVersionUID = 7353331649969420583L;
	
	private int numberOfVotes;
	private List<String> simpleMajority;
	private List<String> twoRound;
	private List<String> alternative;
	private List<String> approval;
	private List<String> bordaCount;
	private List<String> mbc;
	private List<String> serial;
	private List<String> condorcet;
	private Ballot ballot;


	/**
	 * @return the numberOfVotes
	 */
	public int getNumberOfVotes() {
		return this.numberOfVotes;
	}

	/**
	 * @param numberOfVotes the numberOfVotes to set
	 */
	public void setNumberOfVotes(final int numberOfVotes) {
		this.numberOfVotes = numberOfVotes;
	}

	public void setSimpleMajority(final List<String> simpleMajority) {
		if (this.simpleMajority == null)
			this.simpleMajority = new ArrayList<String>();
		else		
			this.simpleMajority.clear();
		
		this.simpleMajority.addAll(simpleMajority);
		
	}

	/**
	 * @return the simpleMajority
	 */
	public List<String> getSimpleMajority() {
		return this.simpleMajority;
	}

	public void setTwoRound(final List<String> twoRoundMV) {
		if (this.twoRound == null)
			this.twoRound = new ArrayList<String>();
		else		
			this.twoRound.clear();
		
		this.twoRound.addAll(twoRoundMV);
	}

	/**
	 * @return the twoRound
	 */
	public List<String> getTwoRound() {
		return this.twoRound;
	}

	/**
	 * @return the alternative
	 */
	public List<String> getAlternative() {
		return this.alternative;
	}

	/**
	 * @param alternative the alternative to set
	 */
	public void setAlternative(final List<String> alternative) {
		if (this.alternative == null)
			this.alternative = new ArrayList<String>();
		else		
			this.alternative.clear();
		
		this.alternative.addAll(alternative);
	}

	/**
	 * @return the approval
	 */
	public List<String> getApproval() {
		return this.approval;
	}

	/**
	 * @param approval the approval to set
	 */
	public void setApproval(final List<String> approval) {
		if (this.approval == null)
			this.approval = new ArrayList<String>();
		else		
			this.approval.clear();
		
		this.approval.addAll(approval);
	}

	/**
	 * @return the bordaCount
	 */
	public List<String> getBordaCount() {
		return this.bordaCount;
	}

	/**
	 * @param bordaCount the bordaCount to set
	 */
	public void setBordaCount(final List<String> bordaCount) {
		if (this.bordaCount == null)
			this.bordaCount = new ArrayList<String>();
		else		
			this.bordaCount.clear();
		
		this.bordaCount.addAll(bordaCount);
	}

	/**
	 * @return the mbc
	 */
	public List<String> getMBC() {
		return this.mbc;
	}

	/**
	 * @param mbc the mbc to set
	 */
	public void setMBC(final List<String> mbc) {
		if (this.mbc == null)
			this.mbc = new ArrayList<String>();
		else		
			this.mbc.clear();
		
		this.mbc.addAll(mbc);
	}

	/**
	 * @return the serial
	 */
	public List<String> getSerial() {
		return this.serial;
	}

	/**
	 * @param serial the serial to set
	 */
	public void setSerial(final List<String> serial) {
		if (this.serial == null)
			this.serial = new ArrayList<String>();
		else		
			this.serial.clear();
		
		this.serial.addAll(serial);
	}

	/**
	 * @return the condorcet
	 */
	public List<String> getCondorcet() {
		return this.condorcet;
	}

	/**
	 * @param condorcet the condorcet to set
	 */
	public void setCondorcet(final List<String> condorcet) {
		if (this.condorcet == null)
			this.condorcet = new ArrayList<String>();
		else		
			this.condorcet.clear();
		
		this.condorcet.addAll(condorcet);
	}

	/**
	 * @return the ballot
	 */
	public Ballot getBallot() {
		return this.ballot;
	}

	/**
	 * @param ballot the ballot to set
	 */
	public void setBallot(final Ballot ballot) {
		this.ballot = ballot;
	}
	
	
	
	// TODO finish the ballot object

}
