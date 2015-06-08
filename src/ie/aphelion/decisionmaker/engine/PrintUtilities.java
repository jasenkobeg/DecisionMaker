package ie.aphelion.decisionmaker.engine;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

// 7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
// May be freely used or adapted

// Modified and awtdapted for proportionate scaling
// for the component being printed, Alan Scott, 07/01

public class PrintUtilities implements Printable
{
    private Component componentToBePrinted;
    private int copies = 1;

    public static void printComponent(Component c)
    {
        new PrintUtilities(c).print(c);
    }

    public PrintUtilities(Component componentToBePrinted)
    {
    	this.componentToBePrinted = componentToBePrinted;
    }

    // For printing
    public void print(Component c)
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        //if (printJob.printDialog()) {
        printJob.setPrintable(this);
        try
        {
            printJob.print();
        }
        catch(PrinterException pe)
        {
            JOptionPane.showMessageDialog(c,
                    "Error printing: " + pe,
                                          "Printing Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
  }


  // For printing
  public int print(Graphics g, PageFormat pageFormat, int pageIndex)
  {
      if (pageIndex > 0)
      {
          return(NO_SUCH_PAGE);
      }
      else
      {
          Graphics2D g2d = (Graphics2D)g;

          // Ensure that the printed page is proportionate to the component's size
          double scaleX = (pageFormat.getImageableWidth()) / (componentToBePrinted.getWidth());
          double scaleY = (pageFormat.getImageableHeight()) / (componentToBePrinted.getHeight());

          // Translate before scaling
          g2d.translate(pageFormat.getImageableX(),
          pageFormat.getImageableY());
          g2d.scale(scaleX,scaleY);

          // Enhance print quality (and speed) by disabling double buffering
          disableDoubleBuffering(componentToBePrinted);
          g2d.drawString(("Ballot# " + copies), 0, 0);
          componentToBePrinted.paint(g2d);
          enableDoubleBuffering(componentToBePrinted);
          return(PAGE_EXISTS);
        }
      }

      // Improves print quality--advised before printing
      public static void disableDoubleBuffering(Component c)
      {
          RepaintManager currentManager = RepaintManager.currentManager(c);
          currentManager.setDoubleBufferingEnabled(false);
      }

      // Improves screen display quality--advised after printing
      public static void enableDoubleBuffering(Component c)
      {
          RepaintManager currentManager = RepaintManager.currentManager(c);
          currentManager.setDoubleBufferingEnabled(true);
      }
}
