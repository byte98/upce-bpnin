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
 * Class representing state of program which displays dialog for setting distance between stations (with origin station option selected)
 * @author jadjj
 */
public class TextUIDistancesSetFrom extends TextUIState
{

    /**
     * Creates new state of program which displays dialog for setting distance between stations (with origin station option selected)
     * @param controller Controller of program
     */
    public TextUIDistancesSetFrom(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/set:from";
        this.screen = new TextUIHTMLTemplateScreen("distances-set-from", "distances-set-from.html");
        this.name = "distances-set-from";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<nazev nebo zkratka stanice>", Color.YELLOW, "Vychozi stanice pro nastaveni vzdalenosti");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
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
        if (input.toLowerCase().equals("cancel"))
        {
            this.controller.changeState("distances");
        }
        else
        {
            Station s = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(input);
            if (s == null)
            {
                this.controller.showError("Neznama stanice '" + input + "'!");
            }
            else
            {
                Map<String, String> data = new HashMap<>();
                data.put("station_from", s.getAbbrevation());
                this.controller.changeState("distances-set-to", data);
            }
        }
    }
    
}
