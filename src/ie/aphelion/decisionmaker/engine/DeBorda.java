package ie.aphelion.decisionmaker.engine;

// Gaphical Uster Interface for the de Borda Institute
// utilising Ballot ADT to record, tally and calculate
// the results of votes, according to the counting rules
// laid down by a number of methodologies.
//
// (c) 2001, 2006 Dr Alan M Scott
// for the de Borda Institute

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class DeBorda extends JFrame
                     implements HyperlinkListener, ActionListener {

    // Initialise constants and variables
    private final int MAX_VALUE = 14;

    private BallotEngine b = new BallotEngine();

    private Object[] options = {"Proceed", "Cancel"};

    DecimalFormat d = new DecimalFormat("0.00");

    private int numPrefs = MAX_VALUE;
    private int flag;
    private int paneIndex, keycode;
    private boolean invalidated, proceed;
    private String optionLabel = new String("ABCDEFGHIJKLMN");

    // Initialise GUI Components
    private Container c;
    private JPanel ballotP, auditP, resultP, condP, setupP, printP, infoP;
    private JTabbedPane pane;
    private NumericTextField voteT[] = new NumericTextField[MAX_VALUE];
    private LimitedTextField optionT[] = new LimitedTextField[MAX_VALUE];
    private AuditTextField from = new AuditTextField(2);
    private AuditTextField to = new AuditTextField(2);
    private AuditTextField voteAuditT = new AuditTextField(5);
    private JTextField ballotT[] = new JTextField[MAX_VALUE];
    private JLabel voteOptionS[] = new JLabel[MAX_VALUE];
    private JLabel voteOptionL[] = new JLabel[MAX_VALUE];
    private JLabel voteOptionA[] = new JLabel[MAX_VALUE];
    private JLabel voteOptionR[] = new JLabel[MAX_VALUE];
    private JLabel voteOptionP[] = new JLabel[MAX_VALUE];
    private JLabel voteA1[] = new JLabel[MAX_VALUE];
    private JLabel voteA2[] = new JLabel[MAX_VALUE];
    private JLabel voteR1[] = new JLabel[MAX_VALUE];
    private JLabel voteR2[] = new JLabel[MAX_VALUE];
    private JLabel voteR3[] = new JLabel[MAX_VALUE];
    private JLabel voteR4[] = new JLabel[MAX_VALUE];
    private JLabel voteR5[] = new JLabel[MAX_VALUE];
    private JLabel voteR6[] = new JLabel[MAX_VALUE];
    private JLabel voteR7[] = new JLabel[MAX_VALUE];
    private JLabel voteR8[] = new JLabel[MAX_VALUE];
    private JLabel voteC[][] = new JLabel[MAX_VALUE + 2][MAX_VALUE + 2];
    private JLabel auditL, sliderL, voteCountL, voteCountAL,
            auditOriginalL, auditValidatedL, logoL, infoL, debordaL,
            AVL, twoRMVL, SMVL, AppVL, bordaL, bordaPL, SVL, CL, percL, percL1,
            percL2, percL3, percL4,
            percL5, percL6, percL7, printFromL, printToL, ballotNumberL;
    private JButton voteSubmitB = new JButton("Submit Vote");
    private JButton voteAuditB = new JButton("Audit Vote");
    private JButton setOptionsB = new JButton("Set Options");
    private JButton glossaryB = new JButton("Glossary...");
    private JButton technicalNoteB = new JButton("Technical Note...");
    private JButton debordaB = new JButton("The Institute...");
    private JButton aboutB = new JButton("Decision Maker");
    private JFileChooser save = new JFileChooser();
    private JFileChooser open = new JFileChooser();
    private JSlider voteS = new JSlider(JSlider.HORIZONTAL, 2, numPrefs, 14);

    private JTextPane introp = new JTextPane();
    private JTextPane tp = new JTextPane();
    private JScrollPane js = new JScrollPane();
    private JFrame jf = new JFrame();

    private HelpViewer viewer;

    // Colours
    private Color backg, foreg, cwin, closs, cdraw;

    // Icons
    private ImageIcon info = new ImageIcon("info.gif");
    private ImageIcon back = new ImageIcon("back.gif");
    private ImageIcon rubc = new ImageIcon("RubikMixed.gif");
    private ImageIcon prin = new ImageIcon("prin1.gif");
    private ImageIcon note = new ImageIcon("NotePad.gif");
    private ImageIcon tick = new ImageIcon("tick1.gif");
    private ImageIcon resu = new ImageIcon("Text.gif");
    private ImageIcon logo = new ImageIcon("debordalogo.gif");

    // Menu items
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu prefMenu = new JMenu("Preferences");
    private JMenu miscMenu = new JMenu("Misc");
    private JMenuItem newM = new JMenuItem("New");
    private JMenuItem openM = new JMenuItem("Open...");
    private JMenuItem saveM = new JMenuItem("Save As...");
    private JMenuItem printBM = new JMenuItem("Print Ballot");
    private JMenuItem printAM = new JMenuItem("Print Audit");
    private JMenuItem exitM = new JMenuItem("Exit");
    private JMenuItem backgM = new JMenuItem("Set Background Colour...");
    private JMenuItem foregM = new JMenuItem("Set Foreground Colour...");
    private JMenuItem cwinM = new JMenuItem("Set Win Colour (Condorcet)...");
    private JMenuItem clossM = new JMenuItem("Set Loss Colour (Condorcet)...");
    private JMenuItem cdrawM = new JMenuItem("Set Draw Colour (Condorcet)...");
    private JMenuItem glossaryM = new JMenuItem("Glossary");
    private JMenuItem debordaM = new JMenuItem("About the de Borda Institute");
    private JMenuItem technicalNoteM = new JMenuItem("Technical Note");
    private JMenuItem aboutM = new JMenuItem("About Decision Maker");
    private JSeparator sep1 = new JSeparator();
    private JSeparator sep2 = new JSeparator();
    private JSeparator sep3 = new JSeparator();

    // Create an instance of the GUI
    public DeBorda() {
        ballotEntry();
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Setting up and placing the GUI components
    public void ballotEntry() {
        b.setNumPrefs(MAX_VALUE);

        // Set up look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        c = getContentPane();

        // Gridbag layout manager used for each panel, for placement of GUI components
        GridBagLayout setupG = new GridBagLayout(); // layout for setup panel
        GridBagLayout ballotG = new GridBagLayout(); // layout for vote entry panel
        GridBagLayout auditG = new GridBagLayout(); // layout for vote audit panel
        GridBagLayout resultG = new GridBagLayout(); // layout for vote result panel
        GridBagLayout condG = new GridBagLayout(); // layout for condPdorcet panel
        GridBagLayout printG = new GridBagLayout(); // layout for ballot paper panel
        GridBagLayout infoG = new GridBagLayout(); // layout for info panel
        GridBagConstraints con = new GridBagConstraints();

	
        // Info panel
        infoP = new JPanel();
        infoP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        infoP.setLayout(infoG);
        backg = infoP.getBackground();
        foreg = infoP.getForeground();
        c.setBackground(backg);
        c.setForeground(foreg);

        // Setup panel
        setupP = new JPanel();
        setupP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        setupP.setLayout(setupG);

        // Ballot panel
        ballotP = new JPanel();
        ballotP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        ballotP.setLayout(ballotG);

        // Results panel
        resultP = new JPanel();
        resultP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        resultP.setLayout(resultG);

        // Condorcet panel
        cwin = Color.BLUE;
        closs = Color.RED;
        cdraw = Color.GRAY;
        condP = new JPanel();
        condP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        condP.setLayout(condG);

        // Audit panel
        auditP = new JPanel();
        auditP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        auditP.setLayout(auditG);
        auditP.setBackground(Color.white);
        auditP.setForeground(Color.black);

        // Ballot paper panel
        printP = new JPanel();
        printP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        printP.setLayout(printG);
        printP.setBackground(Color.white);
        printP.setForeground(Color.black);

        // Selection tabs
        pane = new JTabbedPane();
	pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        pane.addTab("Setup", back, setupP, "Setup");
        pane.setMnemonicAt(0, KeyEvent.VK_S);
        pane.addTab("Ballot Entry", note, ballotP, "Ballot Entry");
        pane.setMnemonicAt(1, KeyEvent.VK_B);
	pane.addTab("Results", resu, resultP, "Results");
        pane.setMnemonicAt(2, KeyEvent.VK_R);
	pane.addTab("Condorcet", rubc, condP, "Condorcet");
        pane.setMnemonicAt(3, KeyEvent.VK_C);
        pane.addTab("Vote Audit", tick, auditP, "Vote Audit");
	pane.setMnemonicAt(4, KeyEvent.VK_A);
	pane.addTab("Ballot Paper", prin, printP, "Print");
        pane.setMnemonicAt(5, KeyEvent.VK_P);
	pane.addTab("Info", info, infoP, "Info");
        pane.setMnemonicAt(6, KeyEvent.VK_I);
	pane.setSelectedIndex(6);
        c.add(pane);
	
        // File Menu
        fileMenu.setMnemonic('F');
        fileMenu.setBackground(backg);
        fileMenu.setForeground(foreg);
        newM.setMnemonic('N');
        fileMenu.add(newM);
        newM.setBackground(backg);
        newM.setForeground(foreg);
        fileMenu.add(sep1);
        openM.setMnemonic('O');
        fileMenu.add(openM);
        openM.setBackground(backg);
        openM.setForeground(foreg);
        saveM.setMnemonic('S');
        fileMenu.add(saveM);
        saveM.setBackground(backg);
        saveM.setForeground(foreg);
        fileMenu.add(sep2);
        printBM.setMnemonic('B');
        fileMenu.add(printBM);
        printBM.setBackground(backg);
        printBM.setForeground(foreg);
        printAM.setMnemonic('A');
        fileMenu.add(printAM);
        printAM.setBackground(backg);
        printAM.setForeground(foreg);
        fileMenu.add(sep3);
        exitM.setMnemonic('E');
        fileMenu.add(exitM);
        exitM.setBackground(backg);
        exitM.setForeground(foreg);

        // Preferences Menu
        prefMenu.setMnemonic('P');
        prefMenu.setBackground(backg);
        prefMenu.setForeground(foreg);
        backgM.setMnemonic('g');
        prefMenu.add(backgM);
        backgM.setBackground(backg);
        backgM.setForeground(foreg);
        foregM.setMnemonic('o');
        prefMenu.add(foregM);
        foregM.setBackground(backg);
        foregM.setForeground(foreg);
        cwinM.setMnemonic('w');
        prefMenu.add(cwinM);
        cwinM.setBackground(backg);
        cwinM.setForeground(foreg);
        clossM.setMnemonic('l');
        prefMenu.add(clossM);
        clossM.setBackground(backg);
        clossM.setForeground(foreg);
        cdrawM.setMnemonic('d');
        prefMenu.add(cdrawM);
        cdrawM.setBackground(backg);
        cdrawM.setForeground(foreg);

        // Misc Menu
        miscMenu.setMnemonic('M');
        miscMenu.setBackground(backg);
        miscMenu.setForeground(foreg);
        glossaryM.setMnemonic('G');
        miscMenu.add(glossaryM);
        glossaryM.setBackground(backg);
        glossaryM.setForeground(foreg);
        debordaM.setMnemonic('d');
        miscMenu.add(debordaM);
        debordaM.setBackground(backg);
        debordaM.setForeground(foreg);
        technicalNoteM.setMnemonic('l');
        miscMenu.add(technicalNoteM);
        technicalNoteM.setBackground(backg);
        technicalNoteM.setForeground(foreg);
        aboutM.setMnemonic('A');
        miscMenu.add(aboutM);
        aboutM.setBackground(backg);
        aboutM.setForeground(foreg);

        // Menu bar
        menuBar.add(fileMenu);
        menuBar.add(prefMenu);
        menuBar.add(miscMenu);
        menuBar.setBackground(backg);
        menuBar.setForeground(foreg);
        setJMenuBar(menuBar);

        // ActionListeners for menu selections
        // Prefs item
        backgM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backg = JColorChooser.showDialog(pane,
                                 "Select background colour",
                                 getBackground());
                selectBackground();
            }
        });

        foregM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                foreg = JColorChooser.showDialog(pane,
                                 "Select foreground colour",
                                 getForeground());
                selectForeground();
            }
        });

        cwinM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cwin = JColorChooser.showDialog(pane,
                                "Select win colour for Condorcet results",
                                getForeground());
                selectCondorcetWinColour();
            }
        });

        clossM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closs = JColorChooser.showDialog(pane,
                                 "Select loss colour for Condorcet results",
                                 getForeground());
                selectCondorcetLossColour();
            }
        });

        cdrawM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cdraw = JColorChooser.showDialog(pane,
                                 "Select draw colour for Condorcet results",
                                 getForeground());
                selectCondorcetDrawColour();
            }
        });

        // Glossary item
        glossaryM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String curDir = System.getProperty("user.dir");
                try {
                    viewer = new HelpViewer("file:///" + curDir + "/Glossary.html", "Decision Maker: Glossary");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // de Borda item
        debordaM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String curDir = System.getProperty("user.dir");
                try {
                    viewer = new HelpViewer("file:///" + curDir + "/deBorda.html", "About the de Borda Institute");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Technical Note item
        technicalNoteM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String curDir = System.getProperty("user.dir");
                try {
                    viewer = new HelpViewer("file:///" + curDir + "/TechnicalNote.html", "Technical Note");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // About item
        aboutM.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(c, "Decision Maker V1.1\n\nDesigned and coded by Dr Alan Scott (alan.scott@dnet.co.uk)\non psephological advice from Peter Emerson,\ndirector of the de Borda Institute (www.deborda.org)\n\n<html><body>&#169;&nbsp;2001-2006, All rights reserved</body></html>", "About Decision Maker", JOptionPane.PLAIN_MESSAGE, tick);
            }
        });

       // Exit item
       exitM.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               System.exit(0);
           }
       });

       // New item (i.e. new ballot)
       newM.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               clearAll();
               voteS.setValue(14);
               changeOptions();
           }
       });

       // Save item
       saveM.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               saveFile();
           }
       });

       // Open item
       openM.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               openFile();
           }
       });

       // Print audit item
       printAM.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               PrintUtilities.disableDoubleBuffering(auditP);
               PrintUtilities.printComponent(auditP);
               PrintUtilities.enableDoubleBuffering(auditP);
           }
       });

       // Print ballot item
       printBM.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   int i1 = 1, i2 = 1;
                   // If a valid range has been specified for printing (both fields), fetch the values
                   // Otherwise, the default setting prints from page 1 to page 1
                   if (!from.isEmpty() && !to.isEmpty()) {
                       i1 = Integer.parseInt(String.valueOf(from.getText()));
                       i2 = Integer.parseInt(String.valueOf(to.getText()));
                       if (i2 < i1) {
                           i2 = i1; // to avoid nonsensical input
                       }
                   }
                   PrintUtilities.disableDoubleBuffering(printP);
                   ballotNumberL.setForeground(Color.black);
                   for (int i = i1; i <= i2; i++)
                   {
                       // Print, for each iteration setting the ballot number on the paper
                       //ballotNumberL.setText("");
                       ballotNumberL.setText("Ballot# " + String.valueOf(i) + "          ");
                       PrintUtilities.printComponent(printP);
                       //ballotNumberL.setText("");
                   }
                   PrintUtilities.enableDoubleBuffering(printP);
               } catch (NumberFormatException nExc) {
                   JOptionPane.showMessageDialog(c,
                                                 "Print range invalid\nPlease re-check your input",
                                                 "Invalid Input",
                                                 JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Glossary item
        glossaryB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String curDir = System.getProperty("user.dir");
                try {
                    viewer = new HelpViewer("file:///" + curDir + "/Glossary.html", "Decision Maker: Glossary");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // de Borda button
        debordaB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String curDir = System.getProperty("user.dir");
                try {
                    viewer = new HelpViewer("file:///" + curDir + "/deBorda.html", "About the de Borda Institute");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Technical Note button
        technicalNoteB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String curDir = System.getProperty("user.dir");
                try {
                    viewer = new HelpViewer("file:///" + curDir + "/TechnicalNote.html", "Technical Note");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // About button
        aboutB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(c, "Decision Maker V1.1\n\nDesigned and coded by Dr Alan Scott (alan.scott@dnet.co.uk)\non psephological advice from Peter Emerson,\ndirector of the de Borda Institute (www.deborda.org)\n\n<html><body>&#169;&nbsp;2001-2006, All rights reserved</body></html>", "About Decision Maker", JOptionPane.PLAIN_MESSAGE, tick);
            }
        });

        // Information text
        infoL = new JLabel();
        infoL.setForeground(foreg);
        infoL.setFont(new Font("Serif", Font.ITALIC, 20));
        infoL.setText("D E C I S I O N   M A K E R");
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        con.gridheight = 1;
        con.anchor = GridBagConstraints.NORTH;
        infoG.setConstraints(infoL, con);
        infoP.add(infoL);

        // De Borda logo
        logoL = new JLabel(logo);
        logoL.setHorizontalAlignment(JLabel.CENTER);
        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 2;
        con.gridheight = 1;
        con.anchor = GridBagConstraints.SOUTH;
        infoG.setConstraints(logoL, con);
        infoP.add(logoL);

        // De Borda text
        debordaL = new JLabel();
        debordaL.setForeground(foreg);
        debordaL.setFont(new Font("Serif", Font.ITALIC, 20));
        debordaL.setText("The de Borda Institute");
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 2;
        con.gridheight = 1;
        con.anchor = GridBagConstraints.NORTH;
        infoG.setConstraints(debordaL, con);
        infoP.add(debordaL);

        // Information text
        introp.setBackground(backg);
        introp.setForeground(foreg);
        introp.setEditable(false);
        introp.setText("Decision Maker is a program designed to facilitate any group of people - a committee,\n" +
                       "a parliament or a nation - wishing to make a collective decision.  The voters express\n" +
                       "their preferences on a range of options (any number from 2 - 14), and the program\n" +
                       "analyses this profile, in an instant, according to all the best known voting procedures\n\n" +
                       "The program allows the user to print ballot papers and validate any ballot cast via a vote\n" +
                       "audit.   It may also be used for some (non-PR) elections run according to the same voting\n" +
                       "procedures: majority/plurality vote (or first-past-the-post), two-round voting, alternative\n" +
                       "vote (or STV), approval voting, Borda count, modified Borda count, and Condorcet.");
                       //"Decision Maker was designed by Dr Alan Scott, on psephological advice from Peter Emerson,\n"); // +
                       //"director of the De Borda Institute");

        con.gridx = 0;
        con.gridy = 3;
        con.gridwidth = 1;
        con.gridheight = 3;
        con.anchor = GridBagConstraints.WEST;
        infoG.setConstraints(introp, con);
        infoP.add(introp);

        // Glossary button
        glossaryB.setBackground(backg);
        glossaryB.setHorizontalAlignment(JLabel.CENTER);
        glossaryB.setVerticalAlignment(JLabel.TOP);
        con.gridx = 1;
        con.gridy = 2;
        con.gridwidth = 1;
        con.gridheight = 1;
        //con.weighty=1;
        con.anchor = GridBagConstraints.SOUTH;
        con.fill = GridBagConstraints.HORIZONTAL;
        infoG.setConstraints(glossaryB, con);
        infoP.add(glossaryB);

        // de Borda Institute button
        debordaB.setBackground(backg);
        debordaB.setHorizontalAlignment(JLabel.CENTER);
        debordaB.setVerticalAlignment(JLabel.TOP);
        con.gridx = 1;
        con.gridy = 3;
        con.gridwidth = 1;
        con.gridheight = 1;
        //con.weighty=1;
        con.anchor = GridBagConstraints.CENTER;
        con.fill = GridBagConstraints.HORIZONTAL;
        infoG.setConstraints(debordaB, con);
        infoP.add(debordaB);

        // Technical Note button
        technicalNoteB.setBackground(backg);
        technicalNoteB.setHorizontalAlignment(JLabel.CENTER);
        technicalNoteB.setVerticalAlignment(JLabel.TOP);
        con.gridx = 1;
        con.gridy = 4;
        con.gridwidth = 1;
        con.gridheight = 1;
        //con.weighty=1;
        con.anchor = GridBagConstraints.CENTER;
        con.fill = GridBagConstraints.HORIZONTAL;
        infoG.setConstraints(technicalNoteB, con);
        infoP.add(technicalNoteB);

        // About button
        aboutB.setBackground(backg);
        aboutB.setHorizontalAlignment(JLabel.CENTER);
        aboutB.setVerticalAlignment(JLabel.TOP);
        con.gridx = 1;
        con.gridy = 5;
        con.gridwidth = 1;
        con.gridheight = 1;
        //con.weighty=1;
        con.anchor = GridBagConstraints.NORTH;
        con.fill = GridBagConstraints.HORIZONTAL;
        infoG.setConstraints(aboutB, con);
        infoP.add(aboutB);

        // Vote count label
        voteCountL = new JLabel();
        voteCountL.setText("Vote# " + (b.getNumVotes() + 1) + " (" +
                           (b.getNumVotes()) + " stored)");
        voteCountL.setHorizontalAlignment(JLabel.RIGHT);
        con.gridx = 0;
        con.gridy = 17;
        con.gridwidth = 5;
        con.gridheight = 1;
        con.weighty = 0;
        con.fill = GridBagConstraints.NONE;
        con.anchor = GridBagConstraints.CENTER;
        ballotG.setConstraints(voteCountL, con);
        ballotP.add(voteCountL);

        // voteAuditB -- voteAudit submit button
        voteAuditB.setBackground(backg);
        con.gridx = 0;
        con.gridy = 16;
        con.gridwidth = 5;
        con.anchor = GridBagConstraints.CENTER;
        setupG.setConstraints(voteAuditB, con);
        setupP.add(voteAuditB);

        // printFromL -- label for 'from' textfield
        printFromL = new JLabel("From ballot# ");
        con.gridx = 5;
        con.gridy = 16;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        setupG.setConstraints(printFromL, con);
        setupP.add(printFromL);

        // printFrom -- label for 'to' textfield
        printToL = new JLabel("To ballot# ");
        con.gridx = 5;
        con.gridy = 17;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        setupG.setConstraints(printToL, con);
        setupP.add(printToL);

        // from -- textfield to ascertain ballot from which to start printing
        from.setForeground(Color.black);
        from.setBackground(Color.white);
        con.gridx = 8;
        con.gridy = 16;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.CENTER;
        setupG.setConstraints(from, con);
        setupP.add(from);

        // to -- textfield to ascertain ballot at which to end printing
        to.setForeground(Color.black);
        to.setBackground(Color.white);
        con.gridx = 8;
        con.gridy = 17;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.CENTER;
        setupG.setConstraints(to, con);
        setupP.add(to);

        // voteAuditT -- vote audit textfield
        voteAuditT.setMaximumSize(new Dimension(50, 20));
        con.gridx = 20;
        con.gridy = 17;
        con.gridwidth = 5;
        con.anchor = GridBagConstraints.CENTER;
        setupG.setConstraints(voteAuditT, con);
        setupP.add(voteAuditT);

        // Slider
        sliderL = new JLabel("Select number of preferences");
        con.gridx = 10;
        con.gridy = 16;
        con.gridwidth = 10;
        con.anchor = GridBagConstraints.CENTER;
        setupG.setConstraints(sliderL, con);
        setupP.add(sliderL);

        // Audit
        auditL = new JLabel("Audit");
        con.gridx = 20;
        con.gridy = 16;
        con.gridwidth = 5;
        con.anchor = GridBagConstraints.CENTER;
        setupG.setConstraints(auditL, con);
        setupP.add(auditL);

        // Labels for headings in results page
        SMVL = new JLabel("Simple Majority");
        SMVL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 3;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(SMVL, con);
        resultP.add(SMVL);
        twoRMVL = new JLabel("   Two-Round");
        twoRMVL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 5;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(twoRMVL, con);
        resultP.add(twoRMVL);
        AVL = new JLabel("    Alternative");
        AVL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 7;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(AVL, con);
        resultP.add(AVL);
        AppVL = new JLabel("       Approval");
        AppVL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 9;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(AppVL, con);
        resultP.add(AppVL);
        bordaL = new JLabel("    Borda Count");
        bordaL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 11;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(bordaL, con);
        resultP.add(bordaL);
        bordaPL = new JLabel("           MBC");
        bordaPL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 13;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(bordaPL, con);
        resultP.add(bordaPL);
        SVL = new JLabel("         Serial");
        SVL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 15;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(SVL, con);
        resultP.add(SVL);
        CL = new JLabel("      Condorcet");
        CL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 17;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        resultG.setConstraints(CL, con);
        resultP.add(CL);
        //percL = new JLabel("Winning %");
        //percL.setFont(new Font("SansSerif", Font.BOLD, 12));
        //con.gridx = 0;
        //con.gridy = 16;
        //con.gridwidth = 3;
        //con.anchor = GridBagConstraints.WEST;
        //resultG.setConstraints(percL, con);
        //resultP.add(percL);
        //percL1 = new JLabel("");
        //percL1.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL1.setBackground(backg);
        //percL1.setForeground(foreg);
        //con.gridx = 3;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL1, con);
        //resultP.add(percL1);
        //percL2 = new JLabel("");
        //percL2.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL2.setBackground(backg);
        //percL2.setForeground(foreg);
        //con.gridx = 5;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL2, con);
        //resultP.add(percL2);
        //percL3 = new JLabel("");
        //percL3.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL3.setBackground(backg);
        //percL3.setForeground(foreg);
        //con.gridx = 7;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL3, con);
        //resultP.add(percL3);
        //percL4 = new JLabel("");
        //percL4.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL4.setBackground(backg);
        //percL4.setForeground(foreg);
        //con.gridx = 9;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL4, con);
        //resultP.add(percL4);
        //percL5 = new JLabel("");
        //percL5.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL5.setBackground(backg);
        //percL5.setForeground(foreg);
        //con.gridx = 11;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL5, con);
        //resultP.add(percL5);
        //percL6 = new JLabel("");
        //percL6.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL6.setBackground(backg);
        //percL6.setForeground(foreg);
        //con.gridx = 13;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL6, con);
        //resultP.add(percL6);
        //percL7 = new JLabel("");
        //percL7.setFont(new Font("SansSerif", Font.BOLD, 12));
        //percL7.setBackground(backg);
        //percL7.setForeground(foreg);
        //con.gridx = 15;
        //con.gridy = 16;
        //con.gridwidth = 2;
        //con.anchor = GridBagConstraints.EAST;
        //resultG.setConstraints(percL7, con);
        //resultP.add(percL7);

        // For audit panel
        voteCountAL = new JLabel("Vote# ");
        voteCountAL.setBackground(Color.white);
        voteCountAL.setForeground(Color.black);
        voteCountAL.setFont(new Font("Serif", Font.ITALIC, 12));
        voteCountAL.setHorizontalAlignment(JLabel.LEFT);
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 3;
        con.anchor = GridBagConstraints.WEST;
        auditG.setConstraints(voteCountAL, con);
        auditP.add(voteCountAL);
        auditOriginalL = new JLabel("   Original      ");
        auditOriginalL.setBackground(Color.white);
        auditOriginalL.setForeground(Color.black);
        auditOriginalL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 3;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        auditG.setConstraints(auditOriginalL, con);
        auditP.add(auditOriginalL);
        auditValidatedL = new JLabel("   Validated      ");
        auditValidatedL.setBackground(Color.white);
        auditValidatedL.setForeground(Color.black);
        auditValidatedL.setFont(new Font("Serif", Font.ITALIC, 12));
        con.gridx = 5;
        con.gridy = 0;
        con.gridwidth = 2;
        con.anchor = GridBagConstraints.EAST;
        auditG.setConstraints(auditValidatedL, con);
        auditP.add(auditValidatedL);

        for (int count = 0; count < MAX_VALUE; count++) {
            // voteOptionS -- vote entry option textfields
            voteOptionS[count] = new JLabel();
            voteOptionS[count].setText("Option " +
                                            (optionLabel.charAt(count)));
            voteOptionS[count].setFont(new Font("Monospaced", Font.BOLD, 12));
            voteOptionS[count].setBackground(backg);
            voteOptionS[count].setForeground(foreg);
            con.gridx = 0;
            con.gridy = count;
            con.gridwidth = 3;
            con.anchor = GridBagConstraints.WEST;
            setupG.setConstraints(voteOptionS[count], con);
            setupP.add(voteOptionS[count]);

            // voteOptionL -- vote entry option labels
            voteOptionL[count] = new JLabel("Option " +
                                            (optionLabel.charAt(count)));
            voteOptionL[count].setForeground(Color.black);
            voteOptionL[count].setFont(new Font("Monospaced", Font.BOLD, 12));
            voteOptionL[count].setBackground(backg);
            voteOptionL[count].setForeground(foreg);
            con.gridx = 0;
            con.gridy = count;
            con.gridwidth = 3;
            con.anchor = GridBagConstraints.WEST;
            ballotG.setConstraints(voteOptionL[count], con);
            ballotP.add(voteOptionL[count]);

            // voteT -- vote entry textfields
            voteT[count] = new NumericTextField("", 3);
            voteT[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteT[count].setToolTipText("Enter preference " +
                                        (optionLabel.charAt(count)));
            voteT[count].setBackground(Color.white);
            voteT[count].setForeground(Color.black);
            if (count >= voteS.getValue() + 1) {
                voteT[count].setVisible(false);
            }
            con.gridx = 3;
            con.gridy = count;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            ballotG.setConstraints(voteT[count], con);
            ballotP.add(voteT[count]);

            // optionT -- option name entry textfields
            optionT[count] = new LimitedTextField("", 30);
            optionT[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            optionT[count].setToolTipText("Enter description for option " +
                                          (optionLabel.charAt(count)));
            optionT[count].setBackground(Color.white);
            optionT[count].setForeground(Color.black);
            if (count >= voteS.getValue() + 1) {
                optionT[count].setVisible(false);
            }
            con.gridx = 3;
            con.gridy = count;
            con.gridwidth = 30;
            con.anchor = GridBagConstraints.EAST;
            setupG.setConstraints(optionT[count], con);
            setupP.add(optionT[count]);

            // voteOptionA -- vote audit option labels
            voteOptionA[count] = new JLabel();
            voteOptionA[count].setText("Option " + (optionLabel.charAt(count)) +
                                       "     ");
            voteOptionA[count].setFont(new Font("Monospaced", Font.BOLD, 12));
            voteOptionA[count].setBackground(Color.white);
            voteOptionA[count].setForeground(Color.black);
            con.gridx = 0;
            con.gridy = count + 1;
            con.gridwidth = 3;
            con.anchor = GridBagConstraints.WEST;
            auditG.setConstraints(voteOptionA[count], con);
            auditP.add(voteOptionA[count]);

            // voteA1 -- vote audit recorded preferences labels
            voteA1[count] = new JLabel();
            voteA1[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteA1[count].setBackground(Color.white);
            voteA1[count].setForeground(Color.black);
            con.gridx = 3;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            auditG.setConstraints(voteA1[count], con);
            auditP.add(voteA1[count]);

            // voteA2 -- vote audit validated preferences labels
            voteA2[count] = new JLabel();
            voteA2[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteA2[count].setBackground(Color.white);
            voteA2[count].setForeground(Color.black);
            con.gridx = 5;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            auditG.setConstraints(voteA2[count], con);
            auditP.add(voteA2[count]);

            // voteOptionR -- vote result option labels
            voteOptionR[count] = new JLabel("Option " +
                                            (optionLabel.charAt(count)));
            voteOptionR[count].setForeground(Color.black);
            voteOptionR[count].setFont(new Font("Monospaced", Font.BOLD, 12));
            voteOptionR[count].setBackground(backg);
            voteOptionR[count].setForeground(foreg);
            con.gridx = 0;
            con.gridy = count + 1;
            con.gridwidth = 3;
            con.anchor = GridBagConstraints.WEST;
            resultG.setConstraints(voteOptionR[count], con);
            resultP.add(voteOptionR[count]);

            // voteR1 -- labels for SMV
            voteR1[count] = new JLabel();
            voteR1[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR1[count].setBackground(backg);
            voteR1[count].setForeground(foreg);
            con.gridx = 3;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR1[count], con);
            resultP.add(voteR1[count]);

            // voteR2 -- labels for two-round majority vote
            voteR2[count] = new JLabel();
            voteR2[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR2[count].setBackground(backg);
            voteR2[count].setForeground(foreg);
            con.gridx = 5;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR2[count], con);
            resultP.add(voteR2[count]);

            // voteR3 -- labels for AV
            voteR3[count] = new JLabel();
            voteR3[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR3[count].setBackground(backg);
            voteR3[count].setForeground(foreg);
            con.gridx = 7;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR3[count], con);
            resultP.add(voteR3[count]);

            // voteR4 -- labels for AppV
            voteR4[count] = new JLabel();
            voteR4[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR4[count].setBackground(backg);
            voteR4[count].setForeground(foreg);
            con.gridx = 9;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR4[count], con);
            resultP.add(voteR4[count]);

            // voteR5 -- labels for Borda vote
            voteR5[count] = new JLabel();
            voteR5[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR5[count].setBackground(backg);
            voteR5[count].setForeground(foreg);
            con.gridx = 11;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR5[count], con);
            resultP.add(voteR5[count]);

            // voteR6 -- labels for Borda preferendum vote
            voteR6[count] = new JLabel();
            voteR6[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR6[count].setBackground(backg);
            voteR6[count].setForeground(foreg);
            con.gridx = 13;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR6[count], con);
            resultP.add(voteR6[count]);

            // voteR7 -- labels for serial vote
            voteR7[count] = new JLabel();
            voteR7[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR7[count].setBackground(backg);
            voteR7[count].setForeground(foreg);
            con.gridx = 15;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR7[count], con);
            resultP.add(voteR7[count]);

            // voteR8 -- labels for Condorcet vote
            voteR8[count] = new JLabel();
            voteR8[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteR8[count].setBackground(backg);
            voteR8[count].setForeground(foreg);
            con.gridx = 17;
            con.gridy = count + 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.EAST;
            resultG.setConstraints(voteR8[count], con);
            resultP.add(voteR8[count]);

            // Label providing number of ballot for printout
            ballotNumberL = new JLabel("                                        ");
            ballotNumberL.setBackground(Color.white);
            ballotNumberL.setForeground(Color.black);
            ballotNumberL.setFont(new Font("SansSerif", Font.ITALIC, 12));
            //ballotNumberL.setVisible(false);
            ballotNumberL.setHorizontalAlignment(JLabel.RIGHT);
            con.gridx = 0;
            con.gridy = 0;
            con.gridwidth = 5;
            con.gridheight = 2;
            //con.weighty = 1;
            con.anchor = GridBagConstraints.EAST;
            printG.setConstraints(ballotNumberL, con);
            printP.add(ballotNumberL);

            // voteOptionP -- ballot printout labels
            voteOptionP[count] = new JLabel("Option " +
                                            optionLabel.charAt(count) + ":" +
                                            optionT[count].getText() +
                                            "                    ");
            voteOptionP[count].setFont(new Font("Monospaced", Font.BOLD, 12));
            voteOptionP[count].setBackground(Color.white);
            voteOptionP[count].setForeground(Color.black);
            con.gridx = 0;
            con.gridy = count + 2;
            con.gridwidth = 3;
            con.gridheight = 1;
            con.anchor = GridBagConstraints.WEST;
            printG.setConstraints(voteOptionP[count], con);
            printP.add(voteOptionP[count]);

            // ballot paper boxes
            ballotT[count] = new JTextField("", 3);
            ballotT[count].setFont(new Font("SansSerif", Font.BOLD, 12));
            ballotT[count].setEnabled(false);
            ballotT[count].setBackground(Color.white);
            ballotT[count].setForeground(Color.black);
            con.gridx = 3;
            con.gridy = count + 2;
            con.gridwidth = 2;
            con.gridheight = 1;
            con.anchor = GridBagConstraints.EAST;
            printG.setConstraints(ballotT[count], con);
            printP.add(ballotT[count]);
        }

        // voteC -- for Condorcet table
        for (int count = 0; count < MAX_VALUE + 2; count++) {
            for (int innerCount = 0; innerCount < MAX_VALUE + 2; innerCount++) {
                voteC[count][innerCount] = new JLabel();
                voteC[count][innerCount].setFont(new Font("SansSerif",
                        Font.BOLD, 12));
                voteC[count][innerCount].setBackground(backg);
                voteC[count][innerCount].setForeground(foreg);
                con.gridx = innerCount;
                con.gridy = count;
                con.gridwidth = 1;
                con.anchor = GridBagConstraints.EAST;
                condG.setConstraints(voteC[count][innerCount], con);
                condP.add(voteC[count][innerCount]);
            }
        }
        boolean set = false;
        for (int count = 0; count < MAX_VALUE + 1; count++) {
            // voteC -- for Condorcet table
            for (int innerCount = 0; innerCount < MAX_VALUE + 1; innerCount++) {
                voteC[count][innerCount] = new JLabel();
                voteC[count][innerCount].setFont(new Font("SansSerif",
                        Font.BOLD, 12));
                voteC[count][innerCount].setBackground(backg);
                voteC[count][innerCount].setForeground(foreg);
                con.gridx = innerCount;
                con.gridy = count;
                con.gridwidth = 1;
                con.anchor = GridBagConstraints.EAST;
                condG.setConstraints(voteC[count][innerCount], con);
                condP.add(voteC[count][innerCount]);
            }
            if (count == 0 && set == false) {
                voteC[0][0].setText("");
                set = true;
            } else {
                voteC[count][0].setText("  " + optionLabel.charAt(count - 1) +
                                        "  ");
                voteC[0][count].setText("  " + optionLabel.charAt(count - 1) +
                                        "  ");
            }
        }
        for (int y = 0; y < MAX_VALUE + 1; y++) {
            voteC[MAX_VALUE + 1][y] = new JLabel();
            voteC[MAX_VALUE + 1][y].setFont(new Font("SansSerif", Font.BOLD, 12));
            voteC[MAX_VALUE + 1][y].setBackground(backg);
            voteC[MAX_VALUE + 1][y].setForeground(foreg);
            con.gridx = MAX_VALUE + 1;
            con.gridy = y;
            con.gridwidth = 1;
            con.anchor = GridBagConstraints.EAST;
            condG.setConstraints(voteC[MAX_VALUE + 1][y], con);
            condP.add(voteC[MAX_VALUE + 1][y]);
            if (y == 0) {
                voteC[MAX_VALUE + 1][y].setText("  Pts");
            } else {
                voteC[MAX_VALUE + 1][y].setText("");
            }
        }

        // Set options button, for providing each option with a description
        setOptionsB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setOptionText();
            }
        });

        // ActionListener for vote audit button
        voteAuditB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // If a vote has been cast and the value entered is legal (> 0, < numVotes)
                    if (b.getNumVotes() > 0 && !voteAuditT.isEmpty() &&
                        Integer.valueOf(voteAuditT.getText()).intValue() <=
                        b.getNumVotes()
                        && Integer.valueOf(voteAuditT.getText()).intValue() > 0) {
                        audit();
                    } else {
                        JOptionPane.showMessageDialog(c,
                                "Vote invalid or non-existent\nPlease check your 'Audit' input",
                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException nExc) {
                    JOptionPane.showMessageDialog(c,
                            "Vote invalid or non-existent\nPlease check your 'Audit' input",
                                                  "Invalid Input",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Vote submission button
        voteSubmitB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });

        // Set options button
        setOptionsB.setBackground(backg);
        con.gridx = 0;
        con.gridy = 17;
        con.gridwidth = 5;
        setupG.setConstraints(setOptionsB, con);
        setupP.add(setOptionsB);

        // Vote submissions button
        voteSubmitB.setBackground(backg);
        con.gridx = 0;
        con.gridy = 16;
        con.gridwidth = 5;
        con.anchor = GridBagConstraints.CENTER;
        ballotG.setConstraints(voteSubmitB, con);
        ballotP.add(voteSubmitB);

        // Slider control for selecting number of preferences
        voteS.setBackground(backg);
        voteS.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        voteS.setMinimumSize(new Dimension(180, 80));
        voteS.setMajorTickSpacing(2);
        voteS.setMinorTickSpacing(1);
        voteS.setPaintTicks(true);
        voteS.setPaintLabels(true);
        voteS.setPaintTrack(true);
        voteS.setSnapToTicks(true);
        voteS.setToolTipText("Use slider to reflect available options");
        voteS.setMinimumSize(new Dimension(200, 80));
        voteS.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                numPrefs = voteS.getValue();
                b.setNumPrefs(voteS.getValue());
            }

            public void mouseReleased(MouseEvent e) {
                JOptionPane optionPane = new JOptionPane(
                        "Are you sure you wish to alter the number of options to " +
                        voteS.getValue() +
                        "?\nWARNING: Existing votes may be affected",
                        JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                        back, options, options[1]);
                JDialog dialog = optionPane.createDialog(c,
                        "Alter available options");
                dialog.setVisible(true);
                String select = (String) optionPane.getValue();
                if (select.equals(options[0])) {
                    changeOptions();
                } else {
                    voteS.setValue(numPrefs);
                }
            }
        });
        con.gridx = 10;
        con.gridy = 17;
        con.gridwidth = 10;
        setupG.setConstraints(voteS, con);
        setupP.add(voteS);

        // Set window size, visibility, etc

        setSize(640, 480);
        setResizable(true);
        setTitle("Decision Maker");
        setVisible(true);
    }

    // When votes are submitted, carry out the following steps
    public void submit() {
        Vector temp = new Vector(voteS.getValue(), 1);

        for (int count = 0; count < voteS.getValue(); count++) {
            // Add value in textfield to vector (empty is 0), clear textfield
            if (voteT[count].isEmpty()) {
                temp.add(count, "0");
            } else {
                temp.add(count, voteT[count].getText());
            }
            voteT[count].setText("");
        }
        // If the vote audit textfield is empty, then add this vote to the end of the existing store
        if (voteAuditT.isEmpty()) {
            b.recordVote(temp);
            voteCountL.setText("Vote# " + (b.getNumVotes() + 1) + " (" +
                               (b.getNumVotes()) + " stored)");
            getResults();
        }
        // If audit textfield contains the number of a vote that exists, resubmit that vote
        // In other words, this provides the ability to edit the vote presently under audit
        else if (!voteAuditT.isEmpty() &&
                 Integer.parseInt(voteAuditT.getText()) >= 1
                 &&
                 Integer.parseInt(voteAuditT.getText()) < (b.getNumVotes() + 1) &&
                 b.getNumVotes() >= 1) {
            int element = Integer.parseInt(voteAuditT.getText());
            b.recordVote(element - 1, temp);
            voteCountL.setText("Vote# " + voteAuditT.getText() + " (" +
                               (b.getNumVotes()) + " stored)");
            getResults();
        } else {
            JOptionPane.showMessageDialog(c, "If you are trying to submit a new vote,\nplease ensure that the 'Audit' text\nentry area is empty, then click 'Audit Vote'\n\nIf you are trying to edit an existing vote,\nalter the number in 'Audit' to reflect the\nnumber of the vote you wish to edit",
                                          "Submission Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    // Steps to follow when audit is requested
    public void audit() {
        // Note that the original vote may, if the number of preferences has been adjusted,
        // potentially be larger than the number of preferences in the audit.  An audit
        // will display the original vote, but will take into consideration for validation
        // purposes only those preferences currently 'allowed', even if the origianl vote
        // contained more preferences into account
        Vector temp1 = b.returnVote(Integer.valueOf(voteAuditT.getText()).
                                    intValue() - 1);
        for (int count = 0; count < temp1.size(); count++) {
            if (temp1.elementAt(count) == "0") {
                temp1.set(count, "-");
            }
            voteT[count].setText(temp1.elementAt(count).toString());
            voteA1[count].setText(temp1.elementAt(count).toString());
            voteCountL.setText("     Vote# " + voteAuditT.getText() + " (" +
                               (b.getNumVotes()) + " stored)");
            voteCountAL.setText("Vote# " + voteAuditT.getText());
        }
        Vector temp2 = b.auditVote(Integer.valueOf(voteAuditT.getText()).
                                   intValue() - 1);
        for (int count = 0; count < temp2.size(); count++) {
            if (temp2.elementAt(count) == "0") {
                temp2.set(count, "-");
            }
            voteOptionA[count].setEnabled(true);
            voteA2[count].setText(temp2.elementAt(count).toString());
        }
        // Leave the rest of the options (greater than the number of preferences to be considered) blank
        for (int count = temp1.size(); count < MAX_VALUE; count++) {
            voteA1[count].setText("");
        }
        for (int count = temp2.size(); count < MAX_VALUE; count++) {
            voteOptionA[count].setEnabled(false);
            voteA2[count].setText("");
        }
    }

    // For processing results of a ballot
    public void getResults() {
        int totalSMV = 0, totalTwoRMV = 0, totalAV = 0, totalAppV = 0,
                totalBV = 0, totalBPV = 0, totalSV = 0;

        // Vectors for storage of results according to each tallying system
        Vector tempSMV = new Vector();
        Vector tempTwoRMV = new Vector();
        Vector tempAV = new Vector();
        Vector tempAppV = new Vector();
        Vector tempBV = new Vector();
        Vector tempBPV = new Vector();
        Vector tempSV = new Vector();
        Vector tempC = new Vector();

        double totalC = 0.0;

        tempSMV = (Vector) b.simpleMajorityVote();
        tempTwoRMV = (Vector) b.twoRoundMV();
        tempAV = (Vector) b.alternativeVote();
        tempAppV = (Vector) b.approvalVote();
        tempBV = (Vector) b.borda();
        tempBPV = (Vector) b.bordaP();
        tempSV = (Vector) b.serialVote();
        tempC = (Vector) b.condorcet();

        if ( b.getTwoRoundDraw() == true )
        { twoRMVL.setText("  Two-Round*");
        }
        else twoRMVL.setText("   Two-Round");

        if ( b.getAVDraw() == true )
        { AVL.setText("   Alternative*");
        }
        else AVL.setText("    Alternative");

        if ( b.getSerialDraw() == true )
        { SVL.setText("        Serial*");
        }
        else SVL.setText("         Serial");


        // Calculate percentage obtained by winning option
        //for (int i = 0; i < voteS.getValue(); i++) {
        //    totalSMV += Integer.parseInt(String.valueOf(tempSMV.elementAt(i)));
        //    totalTwoRMV +=
        //            Integer.parseInt(String.valueOf(tempTwoRMV.elementAt(i)));
        //    totalAV += Integer.parseInt(String.valueOf(tempAV.elementAt(i)));
        //    totalAppV += Integer.parseInt(String.valueOf(tempAppV.elementAt(i)));
        //    totalBV += Integer.parseInt(String.valueOf(tempBV.elementAt(i)));
        //    totalBPV += Integer.parseInt(String.valueOf(tempBPV.elementAt(i)));
        //    totalSV += Integer.parseInt(String.valueOf(tempSV.elementAt(i)));
        //}
        //double maxSMV = Double.parseDouble(String.valueOf(b.maximum(tempSMV)));
        //double maxTwoRMV = Double.parseDouble(String.valueOf(b.maximum(
        //        tempTwoRMV)));
        //double maxAV = Double.parseDouble(String.valueOf(b.maximum(tempAV)));
        //double maxAppV = Double.parseDouble(String.valueOf(b.maximum(tempAppV)));
        //double maxBV = Double.parseDouble(String.valueOf(b.maximum(tempBV)));
        //double maxBPV = Double.parseDouble(String.valueOf(b.maximum(tempBPV)));
        //double maxSV = Double.parseDouble(String.valueOf(b.maximum(tempSV)));
        //double percSMV = maxSMV / totalSMV * 100;
        //double percTwoRMV = maxTwoRMV / totalTwoRMV * 100;
        //double percAV = maxAV / totalAV * 100;
        //double percAppV = maxAppV / totalAppV * 100;
        //double percBV = maxBV / totalBV * 100;
        //double percBPV = maxBPV / totalBPV * 100;
        //double percSV = maxSV / totalSV * 100;

        // If percentage is 0, render it as such (avoids display of non-numeric character)
        //if (totalSMV == 0) {
        //    percL1.setText("0");
        //}
        //if (totalTwoRMV == 0) {
        //    percL2.setText("0");
        //}
        //if (totalAV == 0) {
        //    percL3.setText("0");
        //}
        //if (totalAppV == 0) {
        //    percL4.setText("0");
        //}
        //if (totalBV == 0) {
        //    percL5.setText("0");
        //}
        //if (totalBPV == 0) {
        //    percL6.setText("0");
        //}
        //if (totalSV == 0) {
        //    percL7.setText("0");
        //}

        // Otherwise, display the percentage scored
        //if (totalSMV > 0) {
        //    percL1.setText(d.format(percSMV));
        //}
        //if (totalTwoRMV > 0) {
        //    percL2.setText(d.format(percTwoRMV));
        //}
        //if (totalAV > 0) {
        //    percL3.setText(d.format(percAV));
        //}
        //if (totalAppV > 0) {
        //    percL4.setText(d.format(percAppV));
        //}
        //if (totalBV > 0) {
        //    percL5.setText(d.format(percBV));
        //}
        //if (totalBPV > 0) {
        //    percL6.setText(d.format(percBPV));
        //}
        //if (totalSV > 0) {
        //    percL7.setText(d.format(percSV));
        //}

        //for (int count = 0; count < voteS.getValue(); count++) {
        for (int count = 0; count < b.getNumPrefs(); count++) {
            // Displays '-' if 0 value, otherwise actual value
            if (tempSMV.elementAt(count).equals("0")) {
                tempSMV.set(count, "-");
            }
            if (tempTwoRMV.elementAt(count).equals("0")) {
                tempTwoRMV.set(count, "-");
            }
            if (tempAV.elementAt(count).equals("0")) {
                tempAV.set(count, "-");
            }
            if (tempAppV.elementAt(count).equals("0")) {
                tempAppV.set(count, "-");
            }
            if (tempBV.elementAt(count).equals("0")) {
                tempBV.set(count, "-");
            }
            if (tempBPV.elementAt(count).equals("0")) {
                tempBPV.set(count, "-");
            }
            if (tempSV.elementAt(count).equals("0")) {
                tempSV.set(count, "-");
            }
            voteR1[count].setText("tempSMV"+tempSMV.elementAt(count).toString());
            voteR2[count].setText("tempTwoRMV"+tempTwoRMV.elementAt(count).toString());
            voteR3[count].setText("tempAV"+tempAV.elementAt(count).toString());
            voteR4[count].setText("tempAppV"+tempAppV.elementAt(count).toString());
            voteR5[count].setText("tempBV"+tempBV.elementAt(count).toString());
            voteR6[count].setText("BPV"+tempBPV.elementAt(count).toString());
            voteR7[count].setText("tempSV"+tempSV.elementAt(count).toString());
            voteOptionR[count].setEnabled(true);
        }
        // Leave the rest of the options (above range) blank
        for (int count = b.getNumPrefs(); count < MAX_VALUE; count++) {
            voteR1[count].setText("");
            voteR2[count].setText("");
            voteR3[count].setText("");
            voteR4[count].setText("");
            voteR5[count].setText("");
            voteR6[count].setText("");
            voteR7[count].setText("");
            voteR8[count].setText("");
            voteOptionR[count].setEnabled(false);
        }

        // Clear any existent Condorcet matrix (all spaces)
        for (int outer = 0; outer < MAX_VALUE + 1; outer++) {
            for (int inner = 0; inner < MAX_VALUE + 1; inner++) {
                voteC[outer + 1][inner + 1].setText("");
            }
        }
        // Fill in new Condorcet matrix
        int element = 0;
        for (int y = 0; y < b.getNumPrefs(); y++) { // y is row
            for (int x = y; x < b.getNumPrefs(); x++) { // x is column
                // If x and y coords in table are equal, mark with 'dividing line'
                if (x == y) {
                    voteC[x + 1][y + 1].setText(" - ");
                }
                // Otherwise, display results in the appropriate colours
                else {
                    String s1 = (String) tempC.elementAt(element);
                    int i1 = Integer.parseInt(s1);
                    String s2 = (String) tempC.elementAt(element + 1);
                    int i2 = Integer.parseInt(s2);
                    if (i1 > i2) {
                        voteC[y + 1][x + 1].setForeground(cwin);
                        voteC[x + 1][y + 1].setForeground(closs);
                    }
                    if (i1 == i2 && i1 > 0) {
                        voteC[y + 1][x + 1].setForeground(cdraw);
                        voteC[x + 1][y + 1].setForeground(cdraw);
                    }
                    if (i1 == i2 && i1 == 0) {
                        voteC[y + 1][x + 1].setForeground(foreg);
                        voteC[x + 1][y + 1].setForeground(foreg);
                    }
                    if (i1 < i2) {
                        voteC[y + 1][x + 1].setForeground(closs);
                        voteC[x + 1][y + 1].setForeground(cwin);
                    }
                    voteC[y + 1][x +
                            1].setText(tempC.elementAt(element).toString());
                    element++;
                    voteC[x + 1][y +
                            1].setText(tempC.elementAt(element).toString());
                    element++;
                }
            }
        }

        // Now, iterate through table, adding up winners to see which is the most popular option
        for (int y = 0; y < b.getNumPrefs(); y++) {
            for (int x = 0; x < b.getNumPrefs(); x++) {
                String s1 = voteC[y + 1][x + 1].getText();
                String s2 = voteC[x + 1][y + 1].getText();
                if (s1 != "" && s1 != " - " && s2 != "" & s2 != " ") {
                    int i1 = Integer.parseInt(s1);
                    int i2 = Integer.parseInt(s2);
                    if (i1 > i2) {
                        totalC += 1;
                    }
                    if (i1 == i2 && i1 > 0) {
                        totalC += 0.5;
                    }
                }
            }
            // Display final points tally for Condorcet
            if (totalC > 0) {
                voteR8[y].setText(String.valueOf("total C " + totalC));
            } else {
                voteR8[y].setText("-");
            }
            voteC[MAX_VALUE + 1][y + 1].setText(String.valueOf("total C2 " + totalC));
            totalC = 0.0;
        }
    }

    // Sequence of operations to be followed when slider is adjusted
    public void changeOptions() {
        try {
            // Never allow less than two options (a vote on one option isn't much of a vote!)
            if (voteS.getValue() > 1) {
                b.setNumPrefs(voteS.getValue());
                b.truncatePrefs(voteS.getValue());
                // For vote audit: if a vote has been cast and the value entered is legal (> 0, < numVotes)
                if (b.getNumVotes() > 0 && !voteAuditT.isEmpty() &&
                    Integer.valueOf(voteAuditT.getText()).intValue() <=
                    b.getNumVotes()
                    && Integer.valueOf(voteAuditT.getText()).intValue() > 0) {
                    for (int count = 0; count < MAX_VALUE; count++) {
                        voteA1[count].setText("-");
                        voteA2[count].setText("-");
                        voteCountAL.setText("Vote# ");
                    }
                }
                // If votes have been cast, process the results automatically
                if (b.getNumVotes() > 0) {
                    getResults();
                }
            }
            // Enabling / disabling displays and input fields when number of options is adjusted
            for (int count = 0; count < MAX_VALUE; count++) {
                if (count < voteS.getValue()) {
                    voteT[count].setEnabled(true);
                    voteT[count].setBackground(Color.white);
                    voteOptionL[count].setEnabled(true);
                    voteOptionA[count].setEnabled(true);
                    voteOptionS[count].setEnabled(true);
                    voteOptionR[count].setEnabled(true);
                    voteOptionP[count].setVisible(true);
                    optionT[count].setEnabled(true);
                    optionT[count].setEditable(true);
                    optionT[count].setBackground(Color.white);
                    ballotT[count].setVisible(true);
                } else if (voteS.getValue() > 1) {
                    voteT[count].setEnabled(false);
                    voteT[count].setBackground(Color.black);
                    voteT[count].setText("");
                    voteA1[count].setText(""); // CHECK THIS1!!!!!!!!!!!!!!!!!!!!!!
                    voteA2[count].setText(""); // AND THIS!!!!!!!!!!!!!!!!!!!!!!!
                    voteOptionL[count].setEnabled(false);
                    voteOptionA[count].setEnabled(false);
                    voteOptionS[count].setEnabled(false);
                    voteOptionR[count].setEnabled(false);
                    voteOptionP[count].setVisible(false);
                    optionT[count].setEnabled(false);
                    optionT[count].setBackground(Color.black);
                    ballotT[count].setVisible(false);
                }
            }
        } catch (NumberFormatException nExc) {
            for (int count = 0; count < MAX_VALUE; count++) {
                voteA1[count].setText("-");
                voteA2[count].setText("-");
                voteCountAL.setText("Vote# ");
            }
            JOptionPane.showMessageDialog(c, "Number of options successfully changed\nPlease note that your 'Audit' input is invalid",
                                          "Invalid Input",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clears display and memory of existing ballot
    public void clearAll() {
        for (int count = 0; count < MAX_VALUE; count++) {
            voteOptionP[count].setText("Option " + (count + 1) +
                                       "                    ");
            voteT[count].setText("");
            voteA1[count].setText("");
            voteA2[count].setText("");
            voteR1[count].setText("");
            voteR2[count].setText("");
            voteR3[count].setText("");
            voteR4[count].setText("");
            voteR5[count].setText("");
            voteR6[count].setText("");
            voteR7[count].setText("");
            voteR8[count].setText("");
        }
        String s = "";
        for (int count = 0; count < MAX_VALUE; count++) {
            optionT[count].setText("");
            voteOptionP[count].setText("Option " +
                               (optionLabel.charAt(count)) +
                               "                    ");
            voteOptionA[count].setText("Option " +
                               (optionLabel.charAt(count)) +
                                               "     ");
            voteOptionL[count].setText("Option " +
                                (optionLabel.charAt(count)));
}



        for (int count = 1; count < MAX_VALUE + 2; count++) {
            for (int innerCount = 1; innerCount < MAX_VALUE + 2; innerCount++) {
                voteC[count][innerCount].setText("");
            }
        }
        //percL1.setText("");
        //percL2.setText("");
        //percL3.setText("");
        //percL4.setText("");
        //percL5.setText("");
        //percL6.setText("");
        //percL7.setText("");
        b.clear();
        voteCountL.setText("Vote# " + (b.getNumVotes() + 1) + " (" +
                           (b.getNumVotes()) + " stored)");
    }

    // Permits storage of ballots to peripheral media
    public void saveFile() {

        // Vector to hold names of actual options
        Vector voteOptionLabelTemp = new Vector();

        // Vector to hold the number of preferences in the vote
        Vector tempPrefs = new Vector();
        // Get the existing number of preferences...
        String tempNumString = String.valueOf(b.getNumPrefs());
        // ...and store them in the appropriate vector
        tempPrefs.add(tempNumString);
        // Use the remainder of the first vector to store colour information
        tempPrefs.add(backg);
        tempPrefs.add(foreg);
        tempPrefs.add(cwin);
        tempPrefs.add(closs);
        tempPrefs.add(cdraw);

        // Store names of options in temporary Vector to save
        // Loop through the options and store them in each element of the vector
        for (int count = 0; count < MAX_VALUE; count++) {
            voteOptionLabelTemp.add(optionT[count].getText());
        }

        // Pop up the save dialog box
        int result = save.showSaveDialog(c);

        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File fileName = save.getSelectedFile();

        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(c, "File error", "File error",
                                          JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                FileOutputStream outStream = new FileOutputStream(fileName);
                ObjectOutputStream obj = new ObjectOutputStream(outStream);

                // Write number of preferences (first in - vector number 0)
                obj.writeObject(tempPrefs);
                // Write option names (second in - vector number 1)
                obj.writeObject(voteOptionLabelTemp);
                // Write votes - any subsequent vectors stored in the file
                // NOTE: The order in which these go in is important for the
                // reading-back-in process
                for (int i = 0; i < b.getNumVotes(); i++) {
                    obj.writeObject(b.returnVote(i));
                }
                obj.flush();
                obj.close();
                setScreenTitle(fileName.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(c, "File error",
                                              "Error saving file: " + e,
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Permits saved ballots to be loaded into memory from peripheral device
    public void openFile() {

        // Vectors to temporarily store the number of options in the vote that
        // is loaded in, and the names of each of those options
        Vector temporaryOptionStore = new Vector();
        Vector voteOptionLabelTemp = new Vector();

        int positionInFile = 0;
        int numberOfSavedPrefs;

        int result = open.showOpenDialog(c);

        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File fileName = open.getSelectedFile();

        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(c, "File error", "File error",
                                          JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                FileInputStream inStream = new FileInputStream(fileName);
                ObjectInputStream objIn = new ObjectInputStream(inStream);
                Vector temporaryStore = new Vector(0, 1);
                boolean endOfFile = false;
                b.clear();

                while (!endOfFile) {
                    try {
                        Vector readIn = (Vector) objIn.readObject();

                        // Read in the first element in the stored vector, which
                        // should be the number of preferences in the stored
                        // vote
                        if ( positionInFile == 0 ) {
                            for ( int i = 0; i < 6; i++ ) {
                                temporaryOptionStore.add(readIn.elementAt(i));
                            }
                        }

                        // Read in the names of the options for the next vector
                        // i.e. the second vector in the stored file
                        if ( positionInFile == 1 ) {
                            for (int count = 0; count < Integer.parseInt((String)temporaryOptionStore.firstElement()); count++) {
                                String s = (String) readIn.elementAt(count);
                                // Add what's been read in to a temporary vector
                                // to avoid the effects of clearAll(), which
                                // comes up soon, before the data is put back
                                // into the GUI
                                voteOptionLabelTemp.add(s);
                            }
                        }
                        if ( positionInFile > 1 ) {
                            temporaryStore.add(readIn);
                        }
                        positionInFile++;
                    }
                    catch (EOFException x) {
                        endOfFile = true;
                    }
                    catch (Exception x) {
                    }
                }
                objIn.close();

                clearAll();

                setScreenTitle(fileName.getName());
                // Convert element in first vector containing number of saved
                // preferences to string, convert remaining entries to colours,
                // set the slider to this value, set colours, and readjust the
                // number of preferences on the appropriate tab occordingly
                numberOfSavedPrefs = Integer.parseInt((String)temporaryOptionStore.firstElement());
                backg = (Color) temporaryOptionStore.elementAt(1);
                foreg = (Color) temporaryOptionStore.elementAt(2);
                cwin = (Color) temporaryOptionStore.elementAt(3);
                closs = (Color) temporaryOptionStore.elementAt(4);
                cdraw = (Color) temporaryOptionStore.elementAt(5);
                voteS.setValue(numberOfSavedPrefs);
                selectForeground();
                selectBackground();
                selectCondorcetWinColour();
                selectCondorcetLossColour();
                selectCondorcetDrawColour();
                changeOptions();

                // Count through number of saved prefs, convert to string and
                // place them in the appropriate GUI area
                for (int count = 0; count < numberOfSavedPrefs; count++) {
                    String s = (String) voteOptionLabelTemp.elementAt(count);
                    optionT[count].setText(s);

                    setOptionText();
                }

                // Read in the stored votes
                for (int i = 0; i < temporaryStore.size(); i++) {
                    b.recordVote((Vector) temporaryStore.elementAt(i));
                }
                b.truncatePrefs(voteS.getValue());
                voteCountL.setText("Vote# " + (b.getNumVotes() + 1) + " (" +
                                   (b.getNumVotes()) + " stored)");
                getResults();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(c, "Error opening file",
                                              "File error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setScreenTitle(String title)
    {
        int maxFileNameLength = 50;
        String screenTitle = "";
        for (int count = 0; count < title.length(); count++) {
            if (count < maxFileNameLength) {
                screenTitle += title.charAt(count);
            }
        }
        setTitle("Decision Maker - " + screenTitle);
    }

    // Subclass of JTextField designed to accept numeric input only, no larger than two digits
    class NumericTextField extends JTextField {
        private int mCols;

        NumericTextField() {
            checkInput();
        }

        NumericTextField(int cols) {
            super(cols);
            mCols = cols;
            checkInput();
        }

        NumericTextField(String value, int cols) {
            super(cols);
            mCols = cols;
            if (value.length() <= cols) {
                this.setText(value);
            }
            checkInput();
        }

        // If textfield is empty
        private boolean isEmpty() {
            if (this.getText().length() == 0 || this.getText() == "-") {
                return true;
            }
            return false;
        }

        // Checks validity of input
        private void checkInput() {
            addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent evt) {
                    NumericTextField source = (NumericTextField) evt.getSource();
                    char ch = evt.getKeyChar();
                    if (!Character.isDigit(ch) &&
                        !Character.isIdentifierIgnorable(ch) &&
                        !Character.isISOControl(ch) ||
                        source.getText().length() > 2) {
                        evt.consume();
                        Toolkit.getDefaultToolkit().beep();
                        source.setText("");
                    }
                }
            });
        }
    }


    // Unlimited numeric textfield used in vote audit
    class AuditTextField extends JTextField {
        private int mCols;

        AuditTextField() {
            checkInput();
        }

        AuditTextField(int cols) {
            super(cols);
            mCols = cols;
            checkInput();
        }

        AuditTextField(String value, int cols) {
            super(cols);
            mCols = cols;
            if (value.length() <= cols) {
                this.setText(value);
            }
            checkInput();
        }

        // As with NumericTextField
        private boolean isEmpty() {
            if (this.getText().length() == 0 || this.getText() == "-") {
                return true;
            }
            return false;
        }

        // Similar restrictions to those seen in NumericTextField
        private void checkInput() {
            addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent evt) {
                    AuditTextField source = (AuditTextField) evt.getSource();
                    char ch = evt.getKeyChar();
                    if (!Character.isDigit(ch) &&
                        !Character.isIdentifierIgnorable(ch) &&
                        !Character.isISOControl(ch)) {
                        evt.consume();
                        Toolkit.getDefaultToolkit().beep();
                        source.setText("");
                    }
                }
            });
        }
    }


    // Alphabetic textfield limited to 50 characters
    class LimitedTextField extends JTextField {
        private int mCols;

        LimitedTextField() {
            checkInput();
        }

        LimitedTextField(int cols) {
            super(cols);
            mCols = cols;
            checkInput();
        }

        LimitedTextField(String value, int cols) {
            super(cols);
            mCols = cols;
            if (value.length() <= cols) {
                this.setText(value);
            }
            checkInput();
        }

        // As above
        private boolean isEmpty() {
            if (this.getText().length() == 0 || this.getText() == "-") {
                return true;
            }
            return false;
        }

        // Similar to the textfields defined above
        private void checkInput() {
            addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent evt) {
                    LimitedTextField source = (LimitedTextField) evt.getSource();
                    if (source.getText().length() > 50) {
                        evt.consume();
                        Toolkit.getDefaultToolkit().beep();
                        source.setText("");
                    }
                }
            });
        }
    }

    private void selectBackground() {
        if (backg != null) {
            UIManager.put ("JPanel.background", backg);
            UIManager.put ("Button.background", backg);
            UIManager.put ("Panel.background", backg);
            UIManager.put ("Label.background", backg);
            UIManager.put ("OptionPane.background", backg);
            setupP.setBackground(backg);
            ballotP.setBackground(backg);
            resultP.setBackground(backg);
            condP.setBackground(backg);
            infoP.setBackground(backg);
            introp.setBackground(backg);
            voteSubmitB.setBackground(backg);
            setOptionsB.setBackground(backg);
            voteAuditB.setBackground(backg);
            glossaryB.setBackground(backg);
            technicalNoteB.setBackground(backg);
            debordaB.setBackground(backg);
            aboutB.setBackground(backg);
            save.setBackground(backg);
            open.setBackground(backg);
            voteS.setBackground(backg);
            pane.setBackgroundAt(1, backg);
            menuBar.setBackground(backg);
            fileMenu.setBackground(backg);
            prefMenu.setBackground(backg);
            miscMenu.setBackground(backg);
            newM.setBackground(backg);
            openM.setBackground(backg);
            saveM.setBackground(backg);
            printBM.setBackground(backg);
            printAM.setBackground(backg);;
            exitM.setBackground(backg);
            backgM.setBackground(backg);
            foregM.setBackground(backg);
            cwinM.setBackground(backg);
            clossM.setBackground(backg);
            cdrawM.setBackground(backg);
            glossaryM.setBackground(backg);
            debordaM.setBackground(backg);
            technicalNoteM.setBackground(backg);
            aboutM.setBackground(backg);
            sep1.setBackground(backg);
            sep2.setBackground(backg);
            sep3.setBackground(backg);
            c.setBackground(backg);
            show();
        }
    }

    private void selectForeground() {
        if (foreg != null) {
            UIManager.put("Button.foreground", foreg);
            UIManager.put("Panel.foreground", foreg);
            UIManager.put("Label.foreground", foreg);
            UIManager.put("OptionPane.foreground", foreg);

            introp.setForeground(foreg);
            voteS.setForeground(foreg);
            sliderL.setForeground(foreg);
            auditL.setForeground(foreg);
            voteCountL.setForeground(foreg);
            AVL.setForeground(foreg);
            twoRMVL.setForeground(foreg);
            SMVL.setForeground(foreg);
            AppVL.setForeground(foreg);
            bordaL.setForeground(foreg);
            bordaPL.setForeground(foreg);
            SVL.setForeground(foreg);
            CL.setForeground(foreg);
            //percL.setForeground(foreg);
            //percL1.setForeground(foreg);
            //percL2.setForeground(foreg);
            //percL3.setForeground(foreg);
            //percL4.setForeground(foreg);
            //percL5.setForeground(foreg);
            //percL6.setForeground(foreg);
            //percL7.setForeground(foreg);
            printFromL.setForeground(foreg);
            printToL.setForeground(foreg);
            ballotNumberL.setForeground(foreg);
            infoL.setForeground(foreg);
            debordaL.setForeground(foreg);
            voteSubmitB.setForeground(foreg);
            voteAuditB.setForeground(foreg);
            setOptionsB.setForeground(foreg);
            glossaryB.setForeground(foreg);
            debordaB.setForeground(foreg);
            technicalNoteB.setForeground(foreg);
            aboutB.setForeground(foreg);
            fileMenu.setForeground(foreg);
            prefMenu.setForeground(foreg);
            miscMenu.setForeground(foreg);
            newM.setForeground(foreg);
            openM.setForeground(foreg);
            saveM.setForeground(foreg);
            printBM.setForeground(foreg);
            printAM.setForeground(foreg);
            backgM.setForeground(foreg);
            foregM.setForeground(foreg);
            cwinM.setForeground(foreg);
            clossM.setForeground(foreg);
            cdrawM.setForeground(foreg);
            glossaryM.setForeground(foreg);
            debordaM.setForeground(foreg);
            technicalNoteM.setForeground(foreg);
            aboutM.setForeground(foreg);
            exitM.setForeground(foreg);

            for (int count = 0; count < pane.getTabCount(); count++) {
                pane.setForegroundAt(count, foreg);
            }

            for (int count = 0; count < MAX_VALUE; count++) {
                voteOptionS[count].setForeground(foreg);
                voteOptionL[count].setForeground(foreg);
                voteOptionR[count].setForeground(foreg);
                voteR1[count].setForeground(foreg);
                voteR2[count].setForeground(foreg);
                voteR3[count].setForeground(foreg);
                voteR4[count].setForeground(foreg);
                voteR5[count].setForeground(foreg);
                voteR6[count].setForeground(foreg);
                voteR7[count].setForeground(foreg);
                voteR8[count].setForeground(foreg);
            }
            for (int count = 0; count < MAX_VALUE + 2; count++) {
                for (int innerCount = 0; innerCount < MAX_VALUE + 2;
                                      innerCount++) {
                    voteC[count][innerCount].setForeground(foreg);
                }
            }
        }
    }

    public void selectCondorcetWinColour() {
        getResults();
    }

    public void selectCondorcetLossColour() {
        getResults();
    }

    public void selectCondorcetDrawColour() {
        getResults();
    }

    public void setOptionText() {
        for (int count = 0; count < MAX_VALUE; count++) {
            voteOptionP[count].setText("");
            voteOptionA[count].setText("");
            voteOptionL[count].setText("");
        }
        for (int count = 0; count < b.getNumPrefs(); count++) {
            String s = optionT[count].getText();
            if (s.length() < 20) {
                int spaces = 20 - s.length();
                for (int c = 0; c <= spaces; c++) {
                    s += " ";
                }
            }
            s += "   ";
            voteOptionP[count].setText("Option " +
                                       (optionLabel.charAt(count)) +
                                       ":" + "     " + s);
            if (s.length() > 40) {
                String sTemp = new String();
                for (int i = 0; i < s.length(); i++)
                {
                    if ( i < 40 ) {
                        sTemp += s.charAt(i);
                    }
                    else { sTemp += ""; }
                }
                s = sTemp + "...";
            }
            s += "       ";
            voteOptionA[count].setText("Option " +
                                       (optionLabel.charAt(count)) +
                                       "     " + s);
            s = optionT[count].getText();
            voteOptionL[count].setText("Option " +
                                       (optionLabel.charAt(count)) +
                                       "     " + s + "     ");
        }
    }

    // Main method creates an instance
    public static void main(String args[]) {
        DeBorda de = new DeBorda();
        de.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
    }

    private void jbInit() throws Exception {
    }
}
