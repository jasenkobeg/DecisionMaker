package ie.aphelion.decisionmaker.engine;

import java.text.DecimalFormat;
import java.util.Vector;

public class BallotEngineTest
{
	public static void main ( String args[] )
	{
		DecimalFormat d = new DecimalFormat("0.00");
		Vector t = new Vector(0, 1);
		Vector p = new Vector(0, 1);
		BallotEngine b = new BallotEngine(10);
		int total = 0;

		b.setNumPrefs(5);

 	 Vector voteJ = new Vector(); // creating voteJ
 	 voteJ.add(0, "1");
 	 voteJ.add(1, "2");
 	 voteJ.add(2, "3");
 	 voteJ.add(3, "4");
 	 voteJ.add(4, "5");
 	 b.recordVote(voteJ);

 	 Vector voteK = new Vector(); // etc
 	 voteK.add(0, "1");
 	 voteK.add(1, "3");
 	 voteK.add(2, "4");
 	 voteK.add(3, "2");
 	 voteK.add(4, "5");
 	 b.recordVote(voteK);

 	 Vector voteL = new Vector();
 	 voteL.add(0, "1");
 	 voteL.add(1, "2");
 	 voteL.add(2, "4");
 	 voteL.add(3, "3");
 	 voteL.add(4, "5");
 	 b.recordVote(voteL);

 	 Vector voteM = new Vector();
 	 voteM.add(0, "1");
 	 voteM.add(1, "5");
 	 voteM.add(2, "4");
 	 voteM.add(3, "2");
 	 voteM.add(4, "3");
 	 b.recordVote(voteM);

 	 Vector voteN = new Vector();
 	 voteN.add(0, "5");
 	 voteN.add(1, "1");
 	 voteN.add(2, "2");
 	 voteN.add(3, "3");
 	 voteN.add(4, "4");
 	 b.recordVote(voteN);

          Vector voteO = new Vector();
          voteO.add(0, "5");
          voteO.add(1, "1");
          voteO.add(2, "2");
          voteO.add(3, "3");
          voteO.add(4, "4");
          b.recordVote(voteO);

 	 Vector voteP = new Vector();
 	 voteP.add(0, "5");
 	 voteP.add(1, "3");
 	 voteP.add(2, "1");
 	 voteP.add(3, "2");
 	 voteP.add(4, "4");
 	 b.recordVote(voteP);

 	 Vector voteQ = new Vector();
 	 voteQ.add(0, "5");
 	 voteQ.add(1, "2");
 	 voteQ.add(2, "4");
 	 voteQ.add(3, "3");
 	 voteQ.add(4, "1");
 	 b.recordVote(voteQ);

  	Vector voteR = new Vector();
        voteR.add(0, "5");
  	voteR.add(1, "4");
  	voteR.add(2, "3");
  	voteR.add(3, "2");
  	voteR.add(4, "1");
  	b.recordVote(voteR);

        Vector voteS = new Vector();
        voteS.add(0, "5");
        voteS.add(1, "4");
        voteS.add(2, "3");
        voteS.add(3, "2");
        voteS.add(4, "1");
        b.recordVote(voteS);

		//Vector voteT = new Vector();
		//voteT.add(0, "5");
		//voteT.add(1, "4");
		//voteT.add(2, "3");
		//voteT.add(3, "2");
		//voteT.add(4, "1");
		//b.recordVote(voteT);

		/*Vector voteU = new Vector();
		voteU.add(0, "5");
		voteU.add(1, "4");
		voteU.add(2, "3");
		voteU.add(3, "1");
		voteU.add(4, "2");
		b.recordVote(voteU);

  	Vector voteV = new Vector();
  	voteV.add(0, "5");
  	voteV.add(1, "4");
  	voteV.add(2, "3");
  	voteV.add(3, "2");
  	voteV.add(4, "1");
  	b.recordVote(voteV);

	  Vector voteW = new Vector();
 	 voteW.add(0, "5");
 	 voteW.add(1, "4");
 	 voteW.add(2, "3");
 	 voteW.add(3, "2");
 	 voteW.add(4, "1");
 	 b.recordVote(voteW);

  	Vector voteX = new Vector();
  	voteX.add(0, "4");
  	voteX.add(1, "2");
  	voteX.add(2, "5");
  	voteX.add(3, "3");
  	voteX.add(4, "1");
  	b.recordVote(voteX);

  	Vector voteY = new Vector();
  	voteY.add(0, "3");
  	voteY.add(1, "2");
  	voteY.add(2, "5");
  	voteY.add(3, "4");
  	voteY.add(4, "1");
  	b.recordVote(voteY);*/

        // Output entire Ballot vector, original votes, validated votes, and related points
		System.out.println("Ballot b : " + b + "\n");
		for ( int i = 0; i < b.getNumVotes(); i++ )
		{
			System.out.println("returnVote(" + i +")   : " + b.returnVote(i));
			System.out.println("auditVote(" + i +")    : " + b.auditVote(i));
			System.out.println("returnPoints(" + i +") : " + b.returnPoints(i));
			System.out.println("returnBPoints(" + i +"): " + b.returnBPoints(i) + "\n");
		}

		t = (Vector)b.simpleMajorityVote();
		p.clear(); total = 0;
		System.out.println("\nTALLY (SMV)   : " + t);
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
		System.out.println(p.toString());

		t = (Vector)b.alternativeVote();
		p.clear(); total = 0;
		System.out.println("\nTALLY (AltV)   : " + t);
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
		System.out.println(p.toString());

		t = (Vector)b.approvalVote();
		p.clear(); total = 0;
		System.out.println("\nTALLY (AppV)   : " + t);
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
		System.out.println(p.toString());

		t = (Vector)b.condorcet();
		p.clear(); total = 0;
		System.out.println("\nTALLY (cond)   : " + t);

		t = (Vector)b.borda();
		p.clear(); total = 0;
		System.out.println("\nTALLY (borda)   : " + t);
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
		System.out.println(p.toString());

		t = (Vector)b.bordaP();
		p.clear(); total = 0;
		System.out.println("\nTALLY (bordaP)   : " + t);
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
		System.out.println(p.toString());

		t = (Vector)b.serialVote();
		p.clear(); total = 0;
		System.out.println("\nTALLY (serial)   : " + t);
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
		System.out.println(p.toString());

		t = (Vector)b.twoRoundMV();
		p.clear(); total = 0;
		System.out.println("\nTALLY (two-round)   : " + t);
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
		System.out.println(p.toString());
	}
}
