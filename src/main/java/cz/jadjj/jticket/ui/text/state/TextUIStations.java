package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing state of program which displays stations menu
 * @author jadjj
 */
public class TextUIStations extends TextUIState
{

    /**
     * Creates new state of program with stations menu
     * @param controller 
     */
    public TextUIStations(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations";
        this.screen = new TextUIHTMLTemplateScreen("stations", "stations.html");
        this.name = "stations";
        this.strict = false;
        
        this.helps = new ITextUIHelp[3];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<nazev nebo zkratka stanice>", Color.YELLOW, "Vybrat stanici");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("add", Color.YELLOW, "Pridat novou stanici");
        this.helps[2] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
    }
    
    @Override
    public ITextUIScreen getScreen()
    {
        Map<String, String> data = new HashMap<>();
        data.put("stations_tr", cz.jadjj.jticket.data.Stations.GetInstance().GenerateTableRows());
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }

    @Override
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            case "back": this.controller.changeState("data"); break;
            case "add": this.controller.changeState("stations-add-name"); break;
            default:
                Station st = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(input);
                if (st == null)
                {
                    this.controller.showError("Stanice '" + input + "' nenalezena!");
                }
                else
                {
                    Map<String, String> data = new HashMap<>();
                    data.put("station_name", st.getName());
                    data.put("station_abbr", st.getAbbrevation());
                    this.controller.changeState("stations-edit-name", data);
                }
                break;
        }
    }
    
}
