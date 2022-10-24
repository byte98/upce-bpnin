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
 * Class representing state of program which displays dialog for setting distance between station (with destination selected)
 * @author jadjj
 */
public class TextUIDistancesSetTo extends TextUIState
{

    /**
     * Origin station for setting distance
     */
    private Station origin;
    
    /**
     * Creates new state of program which displays dialog for setting distance between station (with destination selected)
     * @param controller Controller of program
     */
    public TextUIDistancesSetTo(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/set:to";
        this.screen = new TextUIHTMLTemplateScreen("distances-set-to", "distances-set-to.html");
        this.name = "distances-set-to";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<nazev nebo zkratka stanice>", Color.YELLOW, "Cilova stanice pro nastaveni vzdalenosti");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        this.origin = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(data.get("station_from"));
        data.put("stations_distances_tr", cz.jadjj.jticket.data.Distances.GetInstance().GenerateDistancesRows(
                this.origin
        ));
        data.put("station_from", this.origin.getName() + " (" + this.origin.getAbbrevation() + ")");
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
                data.put("station_from", this.origin.getAbbrevation());
                data.put("station_to", s.getAbbrevation());
                this.controller.changeState("distances-set-distance", data);
            }
        }
    }
    
}
