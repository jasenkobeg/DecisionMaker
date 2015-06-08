package ie.aphelion.decisionmaker.engine;

// Adapted by Alan Scott, 2006

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class HelpViewer extends JFrame implements HyperlinkListener,
        ActionListener
{
    private JEditorPane htmlPane;
    private String initialURL;
    private String viewerTitle;

    public HelpViewer(String initialURL, String viewerTitle)
    {
        super(viewerTitle);
        this.initialURL = initialURL;

        try {
            htmlPane = new JEditorPane(initialURL);
            htmlPane.setEditable(false);
            htmlPane.addHyperlinkListener(this);
            JScrollPane scrollPane = new JScrollPane(htmlPane);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
        }
        catch(IOException ioe) {
        warnUser("Can't build HTML pane for "
                 + initialURL + ": " + ioe);
        }

        Dimension screenSize = getToolkit().getScreenSize();
        int width = screenSize.width * 8 / 10;
        int height = screenSize.height * 8 / 10;
        setBounds(width / 8, height / 8, width, height);
        setVisible(true);
        }

        public void actionPerformed(ActionEvent event) {
        String url;
        url = initialURL;
        try {
        htmlPane.setPage(new URL(url));
        } catch(IOException ioe) {
        warnUser("Can't follow link to "
                 + url + ": " + ioe);
        }
        }

        public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        try {
        htmlPane.setPage(event.getURL());
        }
        catch(IOException ioe) {
        warnUser("Cannot follow link to " + event.getURL().toExternalForm()
                 + ": " + ioe);
        }
        }
        }

        private void warnUser(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }
}
