package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Configuration;
import cz.jadjj.jticket.data.UIMode;
import cz.jadjj.jticket.jTicket;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import cz.jadjj.jticket.ui.text.TextUIScreenFactory;
import java.awt.Color;

/**
 * Class representing welcome state of program
 * @author jadjj
 */
public class TextUIWelcome extends TextUIState
{

    /**
     * Creates new welcome state of program
     * @param controller Controller of whole program
     */
    public TextUIWelcome(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "";
        this.screen = TextUIScreenFactory.createHTMLScreen("welcome", "welcome.html");
        this.name = "welcome";
        
        this.helps = new ITextUIHelp[3];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("ticket", Color.YELLOW, "Rezim prodeje");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("data", Color.YELLOW, "Rezim upravy dat");
        this.helps[2] = TextUIHelpFactory.createSimpleHelp("graphics", Color.YELLOW, "Zmeni rezim vzhledu na graficky");
        this.helps[3] = TextUIHelpFactory.createSimpleHelp("exit", Color.MAGENTA, "Ukoncit program");
    }
    
    

    @Override
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            case "exit": this.controller.changeState("exit"); break;
            case "data": this.controller.changeState("data"); break;
            case "ticket":
                if (cz.jadjj.jticket.data.Tariffs.GetInstance().GetAllTariffs().length < 1)
                {
                    this.controller.showError("Nenalezen zadny tarif! Rezim prodeje nelze spustit!");
                }
                else if (cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations().length < 1)
                {
                    this.controller.showError("Nenalezeny zadne stanice! Rezim prodeje nelze spustit!");
                }
                else
                {
                    this.controller.changeState("ticket");
                }             
                break;
            case "graphics": 
                Configuration config = Configuration.getInstance(jTicket.CONFIG_FILE);
                config.uiMode = UIMode.GRAPHICS;
                config.saveToFile();
                this.controller.showSucess("Nastaveni grafiky upsesne zmeneno. Pri pristim spusteni programu bude pouzito graficke prostredi.");
        }
    }
    
}
