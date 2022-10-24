package cz.jadjj.jticket;

import cz.jadjj.jticket.data.Configuration;
import cz.jadjj.jticket.ui.IUserInterface;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Class representing whole program jTicket
 * @author jadjj
 */
public class jTicket
{
    /**
     * Path to file with configuration of program
     */
    public static final String CONFIG_FILE = "config.ini";
    
    /**
     * Version of program
     */
    public static final String VERSION = "1.0";
    
    /**
     * Date of release of program
     */
    public static final String RELEASE_DATE = "2022/12/09";
    
    /**
     * Main function of program
     * @param args Arguments of program
     */
    public static void main(String[] args)
    {
        Configuration config = Configuration.getInstance(jTicket.CONFIG_FILE);
        IUserInterface ui = config.getUI();
        if (ui != null)
        {
            ui.prepare();
            ui.start();
        }
        else
        {
            Logger.getLogger(jTicket.class.getName()).log(Level.SEVERE, "Unknown UI MODE");
        }
    }
}
