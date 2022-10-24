package cz.jadjj.jticket.ui.window;

import cz.jadjj.jticket.data.Configuration;
import cz.jadjj.jticket.jTicket;
import cz.jadjj.jticket.ui.IUserInterface;
import cz.jadjj.jticket.ui.window.windows.WindowUIMainWindow;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIExceptionDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Class representing user interface which uses windows to display content
 * @author jadjj
 */
public class WindowUI implements IUserInterface
{
    /**
     * Main window of program
     */
    private WindowUIMainWindow mainWindow;
        
    /**
     * Path to user interface theme resources
     */
    public static String UI_PATH;
    
    @Override
    public void prepare()
    { 
        Configuration config = Configuration.getInstance(jTicket.CONFIG_FILE);
        WindowUITheme theme = WindowUITheme.getTheme(config.uiTheme);
        if (theme == null)
            theme = WindowUITheme.THEMES.get(0);
        WindowUI.UI_PATH = theme.getUiPath();
        try
        {
            UIManager.setLookAndFeel(theme.getLookAndFeel());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(WindowUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start()
    {
        // Run UI in separate thread
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run()
            {
                mainWindow = new WindowUIMainWindow();
                mainWindow.pack();
                mainWindow.setVisible(true);
            }
        });
    }
    
    /**
     * Method which handles exception
     * @param ex Exception which should be handled
     */
    public static void handleException(Exception ex)
    {
        WindowUIExceptionDialog dialog = new WindowUIExceptionDialog(ex);
        dialog.showDialog();
    }
}
