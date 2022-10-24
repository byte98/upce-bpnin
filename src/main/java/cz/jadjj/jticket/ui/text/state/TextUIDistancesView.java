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
 * Class representing state of program which can display set distances between stations
 * @author jadjj
 */
public class TextUIDistancesView extends TextUIState
{

    /**
     * Creates new state of program with viewer of set distances between stations
     * @param controller 
     */
    public TextUIDistancesView(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/view";
        this.screen = new TextUIHTMLTemplateScreen("distances", "distances.html");
        this.name = "distances-view";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<nazev nebo zkratka stanice>", Color.YELLOW, "Stanice pro zobrazeni vzdalenosti");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
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
        if (input.toLowerCase().equals("back"))
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
                data.put("station", s.getAbbrevation());
                this.controller.changeState("distances-view-station", data);
            }
        }
    }
    
}
