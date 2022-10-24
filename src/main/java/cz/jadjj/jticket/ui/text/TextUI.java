package cz.jadjj.jticket.ui.text;

import cz.jadjj.jticket.ui.IUserInterface;
import javax.swing.SwingUtilities;

/**
 * Class representing textual user interface
 * @author jadjj
 */
public class TextUI implements IUserInterface
{
    
    /**
     * Main window of program
     */
    private TextUIMainWindow mainWindow;
    
    /**
     * Window with help
     */
    private TextUIHelpWindow helpWindow;
    
    /**
     * Controller of text user interface
     */
    private TextUIController controller;
    
    /**
     * Path to directory with html templates
     */
    public static final String HTML_DIR = "resources/textui/";
    
    @Override
    public void prepare()
    {
        this.controller = TextUIController.getController();
    }

    @Override
    public void start()
    {
        // Run UI in separate thread
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run()
            {
                mainWindow = new TextUIMainWindow();
                mainWindow.pack();
                mainWindow.setController(controller);
                mainWindow.setVisible(true);

                helpWindow = new TextUIHelpWindow();
                helpWindow.pack();
                controller.setWindows(mainWindow, helpWindow);
                controller.changeState("welcome");
                
            }
        });
    }
    
}
