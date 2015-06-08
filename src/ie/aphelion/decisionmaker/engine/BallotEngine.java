package ie.aphelion.decisionmaker.engine;

// Ballot paper ADT, for MSc summerr project
// and the de Borda Institute, (c) 2001, Alan M Scott

import java.util.Vector;

public class BallotEngine implements Cloneable {
    private Vector vote; // 1, 4, 3, 2
    private Vector oVoteStore; // Storage for original votes
    private Vector vVoteStore; // Storage for validated votes
    private Vector pointStore; // Storage for points calculated for valid votes
    private Vector bPointStore; // Storage for points calculated for valid votes in Borda preferendum
    // Storage vectors for individual methods of calculating outcomes
    private Vector talliedSMV, talliedTwoRoundMV, talliedAltV, talliedAppV,
            talliedCondorcet,
            talliedBorda, talliedBordaP, talliedSV;
    private boolean serialDraw = false;
    private boolean twoRoundDraw = false;
    private boolean AVDraw = false;

    private int numPrefs = 0; // Number of preferences in this vote
    private final int MAX_VALUE = 14; // Maximum number of preferences allowed in a votes
    // TODO changed should not be static private static int voteNumber = 0; // The number (in casting order) of the vote under manipulation
    private int voteNumber = 0; // The number (in casting order) of the vote under manipulation

    // Simple constructor for Ballot object
    public BallotEngine() {
        vote = new Vector(14, 1);
        oVoteStore = new Vector(0, 1);
        vVoteStore = new Vector(0, 1);
        pointStore = new Vector(0, 1);
        bPointStore = new Vector(0, 1);
        talliedSMV = new Vector(0, 1);
        talliedTwoRoundMV = new Vector(0, 1);
        talliedAltV = new Vector(0, 1);
        talliedAppV = new Vector(0, 1);
        talliedCondorcet = new Vector(0, 1);
        talliedBorda = new Vector(0, 1);
        talliedBordaP = new Vector(0, 1);
        talliedSV = new Vector(0, 1);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Constructor that takes size argument to indicate number of available preferences
    public BallotEngine(int size) {
        vote = new Vector(((size > 14 || size < 1) ? size = 10 : size), 1);
        oVoteStore = new Vector(0, 1);
        vVoteStore = new Vector(0, 1);
        pointStore = new Vector(0, 1);
        bPointStore = new Vector(0, 1);
        talliedSMV = new Vector(0, 1);
        talliedTwoRoundMV = new Vector(0, 1);
        talliedAltV = new Vector(0, 1);
        talliedAppV = new Vector(0, 1);
        talliedCondorcet = new Vector(0, 1);
        talliedBorda = new Vector(0, 1);
        talliedBordaP = new Vector(0, 1);
        talliedSV = new Vector(0, 1);
    }

    // Test whether a Ballot is empty
    public boolean isEmpty(Vector v) {
        if (v.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    // Clear all vectors, reset the vote counter to zero
    public void clear() {
        oVoteStore.clear();
        vVoteStore.clear();
        pointStore.clear();
        bPointStore.clear();
        talliedSMV.clear();
        talliedTwoRoundMV.clear();
        talliedAltV.clear();
        talliedAppV.clear();
        talliedCondorcet.clear();
        talliedBorda.clear();
        talliedBordaP.clear();
        talliedSV.clear();
        voteNumber = 0;
    }

    // Note: this sets the initial CAPACITY of the Vector, which
    // will still however start out containing 0 elements
    public void setNumPrefs(int i) {
        numPrefs = i;
        // Must be initialised to avoid null values
        if (voteNumber == 1) {
            for (int count = 0; count < numPrefs; count++) {
                talliedTwoRoundMV.add(count, "0");
            }
        }
    }

    // Return the number of preferences in the current Ballot
    public int getNumPrefs() {
        return numPrefs;
    }

    // When the vector is resized, it may be necessary to ascertain new validated totals:
    // indeed, a previously valid Ballot may be rendered invalid or partially invalid if
    // several preferences are truncated (eg a first preference could be eliminated).
    // This method makes revalidation possible under such circumstances
    public void truncatePrefs(int truncateFrom) {
        Vector validNew, thisVoteCopy, points, bPoints;
        pointStore.clear(); // Points for most tallying methods
        bPointStore.clear(); // Points for Borda preferendum

        // Go through the original store of votes...
        for (int i = 0; i < oVoteStore.size(); i++) {
            // ...making a a temporary copy of each
            Vector thisVote = (Vector) oVoteStore.elementAt(i);
            thisVoteCopy = (Vector) thisVote.clone();
            // Remove any excess elements
            if (truncateFrom > thisVoteCopy.size()) {
                for (int j = thisVoteCopy.size(); j < truncateFrom; j++) {
                    thisVoteCopy.add(j, "0");
                }
            }
            if (truncateFrom < thisVoteCopy.size()) {
                for (int j = (thisVoteCopy.size() - 1); j >= truncateFrom; j--) {
                    thisVoteCopy.removeElementAt(j);
                }
            }
            thisVoteCopy.trimToSize();
            Vector thisVoteCopy2 = (Vector) thisVoteCopy.clone();
            // Validate this new 'trimmed' version of the vote
            validNew = validate(thisVoteCopy);
            Vector validNew2 = validate(thisVoteCopy2);
            // Store the re-validated vote (without affecting the *original* vote)
            vVoteStore.set(i, validNew);
            points = (Vector) tally(validNew);
            bPoints = (Vector) tallyB(validNew2);
            // And store the points calcluated on the basis of this re-validation
            pointStore.add(points);
            bPointStore.add(bPoints);
        }
    }

    // When a vote is submitted...
    public void recordVote(Vector v) {
        Vector vCopy = (Vector) v.clone();
        int count = 0, maxCount = v.size(), flag = -1, target = 0;
        voteNumber++; // increment number of votes

        // Original vote example: { 5, 3, 0, 3, 2, 6, 1 }
        // After sort:            { 0, 1, 2, 3, 3, 5, 6 }
        // Loop through, check for invalidities (in this
        // instance, the invalidity arises at '3', because
        // 3 is duplicated). Eliminate all values above
        // invalidFlag:           { 0, 0, 0, 0, 2, 0, 1 }

        oVoteStore.add(v); // add original vote to storage
        vCopy = validate(vCopy); // and validate it
        vVoteStore.add(vCopy); // store valid copy
        Vector tempCopy = (Vector) vCopy.clone();
        Vector tempCopy2 = (Vector) vCopy.clone();
        tempCopy = (Vector) tally(tempCopy);
        tempCopy2 = (Vector) tallyB(tempCopy2);
        pointStore.add(tempCopy); // store the points
        bPointStore.add(tempCopy2);
    }

    // Recording a vote at a specific position in the overall store of votes: works as above
    public void recordVote(int element, Vector v) {
        Vector vCopy = (Vector) v.clone();
        int count = 0, maxCount = v.size(), flag = -1, target = 0;

        oVoteStore.set(element, v);
        vCopy = validate(vCopy);
        vVoteStore.set(element, vCopy);
        Vector tempCopy = (Vector) vCopy.clone();
        Vector tempCopy2 = (Vector) vCopy.clone();
        tempCopy = (Vector) tally(tempCopy);
        tempCopy2 = (Vector) tallyB(tempCopy2);
        pointStore.set(element, tempCopy);
        bPointStore.set(element, tempCopy2);
    }

    public Vector validate(Vector vCopy) {
        int count = 0, maxCount = vCopy.size(), flag = -1, target = 0;

        // Ensure that there are no null elements in the Vector (left over from size readjustments)
        for (int i = vCopy.size(); i < numPrefs; i++) {
            vCopy.add(i, "0");
        }

        // Numerical validation: i.e. render vector purely numeric
        for (int i = 0; i < numPrefs; i++) {
            char[] source = vCopy.elementAt(i).toString().toCharArray();
            for (int j = 0; j < source.length; j++) {
                if (!Character.isDigit(source[j]) &&
                    !Character.isIdentifierIgnorable(source[j]) &&
                    !Character.isISOControl(source[j]) ||
                    Character.isSpaceChar(source[j]) ||
                    Character.isWhitespace(source[j])) {
                    vCopy.set(i, "0");
                }
                if (vCopy.elementAt(i).equals("1")) {
                    flag = 3;
                }
            }
        }

        // If no first-preference vote detected, render the entire vote invalid
        if (flag != 3) {
            for (int i = 0; i < numPrefs; i++) {
                vCopy.set(i, "0");
            }
        } else {
            flag = -1;
        }

        // tempCopy will retain a temporarily sorted vector, to eliminate
        // mistakes in the ordering of preferences
        Vector tempCopy = (Vector) vCopy.clone();
        sortVote(tempCopy);

        // CHECK:
        // Establish sequence of values, determine at what point, if any,
        // there is an invalidity, eg, as above, 1, 2, 3*, 5, 6.  Breaks
        // out of 'while' loop when invalidity reached (position recorded
        // in 'count')
        while (flag == -1 && count < tempCopy.size() - 1) {
            String s1 = (String) tempCopy.elementAt(count);
            int i1 = Integer.parseInt(s1);
            String s2 = (String) tempCopy.elementAt(count + 1);
            int i2 = Integer.parseInt(s2);

            // test if numbers equal (ignore zeros essential otherwise count stops at zero!)
            if (i1 == i2 && i2 != 0) {
                flag = 0;
                count -= 2;
            }
            // test if invalid order eg 1,2,*4
            if (i2 - i1 != 1 && i2 - i1 != 0) {
                flag = 1;
                count -= 2;
            }
            // test if two first preferences
            if (i1 == i2 && i2 == 1) {
                flag = 2;
                count -= 2;
            }

            count++;
        }

        // ACTION:
        // Now that invalidities have been detected (flag = 0, 1 or 2), render
        // the vector valid.
        if (flag == 0 || flag == 1 || flag == 2) {
            for (int i = 0; i < tempCopy.size(); i++) {
                String s1 = (String) tempCopy.elementAt(i);
                int i1 = Integer.parseInt(s1);

                if (i1 <= 0) {
                    tempCopy.set(i, "0"); // eliminate negatives
                }
                if (flag == 0 && i > count) {
                    tempCopy.set(i, "0"); // fix equal number problem (1, 2, --3, 3,-- 4)
                }
                if (flag == 1 && i > count + 1) {
                    tempCopy.set(i, "0"); // fix order problem (1, 2, --4,-- 5)
                }
                if (flag == 2) {
                    // Render vector wholly 'empty' if there are two first preferences
                    for (int c = 0; c < tempCopy.size(); c++) {
                        tempCopy.set(c, "0");
                    }
                }
            }
            // Write validated values back to vCopy.  Those that are greater than the valid
            // maximum are invalid, and thus rendered as zero values
            for (int i = 0; i < tempCopy.size(); i++) {
                int i1 = maximum(tempCopy);
                String s2 = (String) vCopy.elementAt(i);
                int i2 = Integer.parseInt(s2);

                if (i2 > i1) {
                    vCopy.set(i, "0");
                }
            }
        }
        return vCopy;
    }

    // For calculating points accorded to individual preferences.
    // Using the Borda preferendum system for partial votes, the maximum points
    // that can be awarded depend on the number of preferences filled in
    // eg the first preference in a 5 preference vote receives 5 points,
    // but if only 3 preferences are filled in, 3 points are awarded
    public Vector tallyB(Vector vCopy) {
        int leastPreferred = maximum(vCopy); // least preferred option (has lowest ranking in vote)
        int points = 1; // points to be awarded
        Vector tempCopy = (Vector) vCopy.clone();

        // While there are still any ranked preferences to be considered...
        while (leastPreferred > 0) {
            // Iterate through the vote
            for (int i = 0; i < vCopy.size(); i++) {
                String s1 = (String) vCopy.elementAt(i);
                int i1 = Integer.parseInt(s1);

                // If least preferred option found
                if (i1 == leastPreferred && leastPreferred > 0) {
                    // Award it the correct number of points (starting at 1 for the least preferred, 2, 3, etc)
                    tempCopy.set(i, String.valueOf(points));
                    // Eliminate the last maximum so that a new one can be established on next iteration
                    vCopy.set(i, "0");
                    // Increment the number of points to be awarded to next least-preferred option
                    points++;
                    // Ascertain the new least preferred option
                    leastPreferred = maximum(vCopy);
                }
            }
        }
        return tempCopy;
    }

    // For calculating points accorded to individual preferences (non-Borda preferendum)
    public Vector tally(Vector vCopy) {
        int count = 0;
        int target = maximum(vCopy); // maximum value in the validated vote (the least preferred option)
        int score = numPrefs + 1; // The number of points to be given to it
        Vector tempCopy = (Vector) vCopy.clone(); // Copy of validated vote
        Vector tempCopy2 = (Vector) vCopy.clone();

        // While there are still any preferences to process
        while (target > 0 || count < tempCopy.size()) {
            target = maximum(tempCopy2); // Determine the new highest value

            // So long as that value is not 0, allocate a score to it
            if (target > 0) {
                for (int i = 0; i < vCopy.size(); i++) {
                    String s1 = (String) vCopy.elementAt(i);
                    int i1 = Integer.parseInt(s1);

                    if (i1 == target) { // If the number being processed is the target
                        tempCopy.set(i, String.valueOf(score - target));
                        tempCopy2.set(i, "0");
                    }
                }
            }
            count++;
        }
        return tempCopy;
    }

    // Bubble-sort is not the most efficient kind, but this is fairly inconsequential given the
    // small number of preferences in any given vote to be sorted
    public Vector sortVote(Vector vec) {
        for (int top = vec.size() - 1; top > 0; top--) {
            for (int i = 0; i < top; i++) {
                String s1 = (String) vec.elementAt(i);
                int i1 = Integer.parseInt(s1);
                String s2 = (String) vec.elementAt(i + 1);
                int i2 = Integer.parseInt(s2);

                if (i2 < i1) {
                    int temp = i1;
                    i1 = i2;
                    i2 = temp;
                    String sOne = String.valueOf(i1);
                    String sTwo = String.valueOf(i2);
                    vec.set(i, sOne);
                    vec.set(i + 1, sTwo);
                }
            }
        }
        return vec;
    }


    // Return the number of votes cast
    public int getNumVotes() {
        return voteNumber;
    }

    // Return the original vote cast
    public Vector auditVote(int ballotNumber) {
        return (Vector) vVoteStore.elementAt(ballotNumber);
    }

    // Return a validated copy of the original vote cast
    public Vector returnVote(int ballotNumber) {
        return (Vector) oVoteStore.elementAt(ballotNumber);
    }

    // Return the points attached to the validated vote
    protected Vector returnPoints(int ballotNumber) {
        return (Vector) pointStore.elementAt(ballotNumber);
    }

    // Return the points attached to the validated vote
    protected Vector returnBPoints(int ballotNumber) {
        return (Vector) bPointStore.elementAt(ballotNumber);
    }

    // I tried using Collections.max(Collection), but it does not correctly determine that 10
    // is greater than 9, 20 is greater than 2, and so on.  Hence I wrote this method.
    protected int maximum(Vector v) {
        int count = 0;
        for (int c = 0; c < numPrefs; c++) {
            int i1;
            String s1 = (String) v.elementAt(c);
            if (s1.equals("-")) {
                i1 = 0;
            } else {
                i1 = Integer.parseInt(s1);
            }

            if (i1 > count) {
                count = i1;
            }
        }
        return count;
    }

    public String toString() {
        return oVoteStore.toString();
    }

    // Calculate results for a simple majority vote
    public Vector simpleMajorityVote() {
        Vector temp = new Vector();

        talliedSMV.clear();
        // Initialise all elements in final tally Vector to 0
        for (int count = 0; count < numPrefs; count++) {
            talliedSMV.add(count, "0");
        }

        for (int i = 0; i < voteNumber; i++) {
            // Copy a vote into temp
            temp = (Vector) pointStore.elementAt(i);
            for (int j = 0; j < numPrefs; j++) {
                // Must ensure that all votes are of the one lengthgth
                // An exception will be thrown, for example, if the loop
                // encounters...
                //
                // { 3, 5, 2, 1, 4 }
                // { 3, 4, 2, 1, null } --> exception thrown at null
                //
                // Ensure in deBorda program that all Vectors are returned
                // with zeros replacing any ommitted entries
                // eg {1, 2, 3, 4 } in a 6-preference scenario should be
                //    {1, 2, 3, 4, 0, 0 }

                String s1 = (String) temp.elementAt(j);
                int i1 = Integer.parseInt(s1);
                String s2 = (String) talliedSMV.elementAt(j);
                int i2 = Integer.parseInt(s2);

                // Award a point to the preference scoring the most
                if (i1 == maximum(temp) && i1 >= 1) {
                    i1 = 1;
                    talliedSMV.set(j, String.valueOf(i1 + i2));
                }
            }
        }
        return talliedSMV;
    }

    // Two round majority vote
    public Vector twoRoundMV() {
        // See below, in alternativeVote, for the reasons for making clones of
        // the vectors on a number of occasions
        Vector twoRoundPointStore = (Vector) pointStore.clone();
        Vector talliedTwoRoundMVBackup = (Vector) talliedTwoRoundMV.clone();
        Vector temp = new Vector(0, 1);
        Vector temp2 = new Vector(0, 1); // Will contain an individual vote on each inner iteration
        int maxScore = 0, firstElement = 0, secondElement = 1, total = 0;
        boolean maxFound, empty = false, winner = false;
        int numMaxima = 0;
        setTwoRoundDraw(false);

        // Initialise all elements in final tally Vector to 0
        talliedTwoRoundMV.clear();

        for (int count = 0; count < numPrefs; count++) {
            talliedTwoRoundMV.add(count, "0");
            talliedTwoRoundMVBackup.add(count, "0");
        }

        // Go through all votes...
        for (int i = 0; i < voteNumber; i++) {
            // ...assign to temp an individual vote...
            temp = (Vector) pointStore.elementAt(i);
            temp2 = (Vector) temp.clone(); // clone temp

            // Add 1 point to tally for highest-ranking option
            for (int j = 0; j < temp2.size(); j++) {
                String s1 = (String) temp2.elementAt(j);
                int i1 = Integer.parseInt(s1);
                String s2 = (String) talliedTwoRoundMV.elementAt(j);
                int i2 = Integer.parseInt(s2);
                if (maximum(temp2) <= 0) {
                    empty = true;
                } else {
                    empty = false;
                }

                // If element in vote has received highest score, add it to the tally
                if (i1 == maximum(temp2) && i1 > 0) {
                    i1 = 1;
                    talliedTwoRoundMV.set(j, String.valueOf(i1 + i2));
                }
            }
        } // All votes have been tallied at this point

        // Work out total points for ALL final tallied options
        for (int i = 0; i < numPrefs; i++) {
            String s1 = (String) talliedTwoRoundMV.elementAt(i);
            total += Integer.parseInt(s1);
        }

        // Percentage obtained by most popular preference
        double max = Double.parseDouble(String.valueOf(maximum(
                talliedTwoRoundMV)));
        double percentage = max / total * 100;

        // If more than 50% has been received by any one option, it's not necessary to continue
        // to a second round
        if (percentage > 50.0) {
            return talliedTwoRoundMV;
        }

        // If 50% or less has been obtained, and two or more options draw,
        // set the draw flag to true (used in DeBorda program to indicate
        // whether options have drawn
        for (int i = 0; i < numPrefs; i++) {
            if (Double.parseDouble(String.valueOf(talliedTwoRoundMV.elementAt(i))) ==
                max && max > 0) {
                numMaxima++;
                if (numMaxima > 1) {
                    setTwoRoundDraw(true);
                }
            }
        }

        // This sequence avoids an exception being thrown if an empty vote contains a '-'
        if (empty) {
            for (int count = 0; count < numPrefs; count++) {
                if (talliedTwoRoundMV.elementAt(count) == "-") {
                    talliedTwoRoundMV.set(count, "0");
                }
            }
        }

        talliedTwoRoundMVBackup.clear();

        // Otherwise, find the two most popular options
        // Find maximum points scored and eliminate temporarily from tally,
        // so that next higest value may be ascertained--thus, two most popular
        // values are obtained
        for (int i = 0; i < 2; i++) {
            maxFound = false;
            maxScore = maximum(talliedTwoRoundMV);
            int count = 0;

            while (count < temp.size() && !maxFound) {
                String s1 = (String) talliedTwoRoundMV.elementAt(count);
                int i1 = Integer.parseInt(s1);
                if (i1 == maxScore) {
                    talliedTwoRoundMV.set(count, "0");
                    if (i == 0) {
                        firstElement = count;
                    }
                    if (i == 1) {
                        secondElement = count;
                    }
                    maxFound = true;
                }
                count++;
            }
        }

        // Re-initialise final tally (so lower ranking preferences can be removed)
        talliedTwoRoundMV.clear();

        for (int count = 0; count < numPrefs; count++) {
            talliedTwoRoundMV.add(count, "0");
        }

        // Iterate through votes, eliminating all values not in top two preferences
        for (int i = 0; i < voteNumber; i++) {
            // temp contains an individual vote...
            boolean set = false;
            temp = (Vector) pointStore.elementAt(i);
            temp2 = (Vector) temp.clone();

            // Iterate through individual votes...
            for (int j = 0; j < temp2.size(); j++) {
                String s1 = (String) talliedTwoRoundMV.elementAt(firstElement);
                int i1 = Integer.parseInt(s1); // most popular element overall
                String s2 = (String) talliedTwoRoundMV.elementAt(secondElement);
                int i2 = Integer.parseInt(s2); // second most popular element overall
                String s3 = (String) temp2.elementAt(firstElement);
                int i3 = Integer.parseInt(s3); // the preference at location of most preferred option
                String s4 = (String) temp2.elementAt(secondElement);
                int i4 = Integer.parseInt(s4); // preference at location of second most preferred option

                // If the preference at this position is more popular than the
                // preference at the position of the other most popular choice,
                // add a point to the total scored by that preference
                if (i3 > i4 && i3 > 0 && !set) {
                    talliedTwoRoundMV.set(firstElement, String.valueOf(++i1));
                    set = true;
                }
                // The same idea here
                else if (i3 < i4 && i4 > 0 && !set) {
                    talliedTwoRoundMV.set(secondElement, String.valueOf(++i2));
                    set = true;
                }
            }
        }
        return talliedTwoRoundMV;
    }


    public Vector alternativeVote() {
        // It is here necessary to ceate a clone of pointStore, and clones of temp (individual votes)
        // because failure to clone individual votes means that their points total will be altered by
        // this method for good, thus providing inaccurate points for other voting methods to process
        Vector altPointStore = (Vector) pointStore.clone();
        Vector temp = new Vector(0, 1); // Will contain an individual vote on each inner iteration
        Vector temp2 = new Vector(0, 1); // Will contain an individual vote on each inner iteration
        double percentage = 0;
        int total = 0, leastPreferred = 0, numMaxima = 0, numMinima = 0,
                element;
        int x = 10;
        setAVDraw(false);

        // Keep going until more than 50% scored by an option
        while (percentage <= 50.0) {
            x--;
            total = 0; // reset total points (necessary if counted previously)
            numMaxima = 0; // for establishing whether the tally contains a draw - reset
            numMinima = 0; // for establishing whether the two 'losing' values draw - reset

            // Initialise all elements in final tally Vector to 0
            // (Necessary so that old totals are not retained)
            talliedAltV.clear();
            for (int count = 0; count < numPrefs; count++) {
                talliedAltV.add(count, "0");
            }

            // Iterate through each vote
            for (int i = 0; i < voteNumber; i++) {
                // temp contains an individual vote...
                temp = (Vector) altPointStore.elementAt(i);
                temp2 = (Vector) temp.clone();

                // Iterate through each preference in the vote under examination
                for (int j = 0; j < temp2.size(); j++) {
                    String s1 = (String) temp2.elementAt(j);
                    int i1 = Integer.parseInt(s1);
                    String s2 = (String) talliedAltV.elementAt(j);
                    int i2 = Integer.parseInt(s2);

                    // For the maximum value found, add a point to the points tally
                    if (i1 == maximum(temp2) && i1 > 0) {
                        i1 = 1;
                        talliedAltV.set(j, String.valueOf(i1 + i2));
                    }
                }
            } // All votes have been tallied at this point

            // Work out total points for ALL final tallied options
            for (int n = 0; n < numPrefs; n++) {
                String s1 = (String) talliedAltV.elementAt(n);
                total += Integer.parseInt(s1);
            }

            // Percentage obtained by most popular preference
            double max = Double.parseDouble(String.valueOf(maximum(talliedAltV)));
            percentage = max / total * 100;

            // If 50% or less has been obtained, and two or more options draw,
            // set the draw flag to true (used in DeBorda program to indicate
            // whether options have drawn
            for (int i = 0; i < numPrefs; i++) {
                if (Integer.parseInt(String.valueOf(talliedAltV.elementAt(i))) ==
                    max && max > 0) {
                    numMaxima++;
                    if (numMaxima > 1) {
                        setAVDraw(true);
                    }
                }
            }

            // If percentage is not > 50, find the lowest value in the final tally that is
            // not zero (leastPreferred).
            if (total != 0 && percentage <= 50.0) {
                for (int i = 0; i < talliedAltV.size(); i++) {
                    String leastP = (String) talliedAltV.elementAt(i);
                    leastPreferred += Integer.parseInt(leastP);
                }
                // LeastPreferred: start out as the *most* preferred option, and compare the other
                // options to it, until the least preferred that is not zero is found
                element = -1;

                // LeastPreferred starts out as the option to the right of the Vector, whatever that option
                // may be.  Then, the value of this preference is compared to the other values in the Vector,
                // from right to left.  The lowest non-zero total will be leastPreferred
                for (int i = (numPrefs - 1); i >= 0; i--) {
                    String s1 = (String) talliedAltV.elementAt(i);
                    int i1 = Integer.parseInt(s1);
                    // If the preference under examination is less preferred than leastPreferred,
                    // give leastPreferred this new value...And so on, until the lowest value that
                    // is not 0 has been found
                    if (i1 < leastPreferred && i1 > 0) {
                        leastPreferred = i1;
                        element = i;
                    }
                }
                // leastPreferred will be option i throughout all votes.  Therefore, eliminate this
                // option from consideration (by rendering it 0--thus it cannot be a maximum and
                // obtain a point
                for (int i = 0; i < numPrefs; i++) {
                    if (Integer.parseInt(String.valueOf(talliedAltV.elementAt(i))) == leastPreferred && leastPreferred > 0 ) {
                        numMinima++;
                        if (numMinima > 1) {
                            setAVDraw(true);
                        }
                    }
                }

                // Iterate through all votes
                for (int i = (voteNumber - 1); i >= 0; i--) {
                    // temp contains an individual vote...
                    temp = (Vector) altPointStore.elementAt(i);
                    temp2 = (Vector) temp.clone();
                    {
                        // Eliminate lowest value from all votes
                        temp2.set(element, "0");
                        // Deals with options having no first preferences, eliminating them from the count
                        for (int c = 0; c < numPrefs; c++) {
                            int points = Integer.parseInt(String.valueOf(
                                    talliedAltV.elementAt(c)));
                            if (points == 0) {
                                temp2.set(c, "0");
                            }
                        }
                    }
                    // Replace modified vote in pointStore
                    altPointStore.set(i, temp2);
                }
            }
        } // end of while loop
        return talliedAltV;
    }

// Method for calculating the outcome of an approval vote, where failure to vote for a given option
// indicates lack of approval, and the normal rules for invalidity apply
    public Vector approvalVote() {
        Vector temp = new Vector();

        talliedAppV.clear();
        // Initialise all elements in final tally Vector to 0
        for (int count = 0; count < numPrefs; count++) {
            talliedAppV.add(count, "0");
        }

        for (int i = 0; i < voteNumber; i++) {
            // Copy a vote into temp
            temp = (Vector) pointStore.elementAt(i);

            for (int j = 0; j < numPrefs; j++) {
                String s1 = (String) temp.elementAt(j); // preference-in-this-vote
                int i1 = Integer.parseInt(s1);
                String s2 = (String) talliedAppV.elementAt(j); // overall total for this preference across all votes
                int i2 = Integer.parseInt(s2);

                // If any approval has been given to this option, add a point to its total
                if (i1 >= 1) {
                    i1 = 1;
                    talliedAppV.set(j, String.valueOf(i1 + i2));
                }
            }
        }
        return talliedAppV;
    }

// Condorcet calculation method
    public Vector condorcet() {
        int outer = 0, inner, increment = 0;
        Vector storage = new Vector();
        // Initialise all elements in final tally Vector to 0
        talliedCondorcet.clear();
        for (int count = 0; count < numPrefs; count++) {
            talliedCondorcet.add(count, "0");
        }

        // 'Super' outer loop goes through each vote
        while (outer < numPrefs) {
            inner = 1 + increment;

            // Inner loop goes through preferences
            while (inner < numPrefs) {
                int first = 0, second = 0;
                for (int i = 0; i < voteNumber; i++) {
                    Vector temp = (Vector) pointStore.elementAt(i);
                    String s1 = (String) temp.elementAt(outer);
                    int i1 = Integer.parseInt(s1);
                    String s2 = (String) temp.elementAt(inner);
                    int i2 = Integer.parseInt(s2);

                    if (i1 > i2) {
                        first++;
                    }
                    if (i1 < i2) {
                        second++;
                    }
                }
                // Store option pairings
                storage.add(String.valueOf(first));
                storage.add(String.valueOf(second));
                inner++;
            }
            outer++;
            increment++;
        }
        return storage;
    }

// Borda count
    public Vector borda() {
        Vector temp = new Vector();

        talliedBorda.clear();
        // Initialise all elements in final tally Vector to 0
        for (int count = 0; count < numPrefs; count++) {
            talliedBorda.add(count, "0");
        }

        // Cycle through preference i in every vote, totalling the number of points
        // that preference has secured overall
        for (int i = 0; i < voteNumber; i++) {
            temp = (Vector) pointStore.elementAt(i);

            for (int j = 0; j < numPrefs; j++) {
                String s1 = (String) temp.elementAt(j);
                int i1 = Integer.parseInt(s1);
                // s2 gets existing total for this preference
                String s2 = (String) talliedBorda.elementAt(j);
                int i2 = (Integer.parseInt(s2));
                i2 += i1;
                talliedBorda.set(j, Integer.toString(i2));
            }
        }
        return talliedBorda;
    }

// Borda preferendum
    public Vector bordaP() {
        Vector temp = new Vector();

        talliedBordaP.clear();
        // Initialise all elements in final tally Vector to 0
        for (int count = 0; count < numPrefs; count++) {
            talliedBordaP.add(count, "0");
        }

        // Cycle through preference i in every vote, totalling the number of points
        // that preference has secured overall
        for (int i = 0; i < voteNumber; i++) {
            temp = (Vector) bPointStore.elementAt(i);

            for (int j = 0; j < numPrefs; j++) {
                String s1 = (String) temp.elementAt(j);
                int i1 = Integer.parseInt(s1);
                // s2 gets existing total for this preference
                String s2 = (String) talliedBordaP.elementAt(j);
                int i2 = (Integer.parseInt(s2));
                i2 += i1;
                talliedBordaP.set(j, Integer.toString(i2));
            }
        }
        return talliedBordaP;
    }

// Serial vote
    public Vector serialVote() {
        // As above with the alternative vote, it is here necessary to ceate a clone of pointStore, and
        // clones of temp (individual votes) because failure to clone individual votes means that their
        // points total will be altered by this method for good, thus providing inaccurate votes for other
        // voting methods to process
        Vector serialPointStore = (Vector) pointStore.clone();
        Vector temp = new Vector(0, 1);
        Vector temp2 = new Vector(0, 1); // Will contain an individual vote on each inner iteration

        // Integers 'first' and 'last' compare the most extreme options (0 with 5, 1 with 4, for example)
        int first = 0, last = numPrefs - 1;
        boolean end = false;
        setSerialDraw(false);

        while (!end) {
            // Initialise all elements in final tally Vector to 0
            // (Necessary so that old totals are not retained)
            talliedSV.clear();
            for (int count = 0; count < numPrefs; count++) {
                talliedSV.add(count, "0");
            }

            int lowestTotal = 0;
            int highestTotal = 0;

            for (int i = 0; i < voteNumber; i++) {
                // temp contains an individual vote...
                temp = (Vector) serialPointStore.elementAt(i);
                temp2 = (Vector) temp.clone();

                // Add 1 point from this particular vote to the overall tally
                // in the appropriate position (i.e. at the highest-ranking option)
                String lowestExtremeS = (String) temp2.elementAt(first);
                int lowestExtremeI = Integer.parseInt(lowestExtremeS);
                String highestExtremeS = (String) temp2.elementAt(last);
                int highestExtremeI = Integer.parseInt(highestExtremeS);

                // If element in vote has received highest score (in that vote--i.e. it has become the first
                // preference--add it to the tally
                if (lowestExtremeI > highestExtremeI) {
                    lowestTotal++;
                    talliedSV.set(first, String.valueOf(lowestTotal));
                }
                if (highestExtremeI > lowestExtremeI) {
                    highestTotal++;
                    talliedSV.set(last, String.valueOf(highestTotal));
                }
            } // All votes have been tallied at this point

            // Compare the two extremes in the final tally
            String sFirst = (String) talliedSV.elementAt(first);
            int iFirst = Integer.parseInt(sFirst);
            String sLast = (String) talliedSV.elementAt(last);
            int iLast = Integer.parseInt(sLast);

            // If the first option is more popular than the last, or equally popular, eliminate it
            if (iFirst >= iLast) {
                if (iFirst == iLast && iFirst > 0) {
                    setSerialDraw(true);
                }
                for (int i = 0; i < voteNumber; i++) {
                    temp = (Vector) serialPointStore.elementAt(i);
                    temp2 = (Vector) temp.clone();
                    temp2.set(last, "0");
                    serialPointStore.set(i, temp2);
                }
                last--;
            }
            // and vice versa
            else {
                for (int i = 0; i < voteNumber; i++) {
                    temp = (Vector) serialPointStore.elementAt(i);
                    temp2 = (Vector) temp.clone();
                    temp2.set(first, "0");
                    serialPointStore.set(i, temp2);
                }
                first++;
            }
            // Loop until no more legitimate comparisons can be made
            if (last <= first || first >= last) {
                end = true;
            }
        }
        return talliedSV;
    }

    public void setTwoRoundDraw(boolean b) {
        twoRoundDraw = b;
    }

    public void setAVDraw(boolean b) {
        AVDraw = b;
    }

    public void setSerialDraw(boolean b) {
        serialDraw = b;
    }

    public boolean getTwoRoundDraw() {
        if (twoRoundDraw == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getAVDraw() {
        if (AVDraw == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getSerialDraw() {
        if (serialDraw == true) {
            return true;
        } else {
            return false;
        }
    }

    private void jbInit() throws Exception {
    }
}
