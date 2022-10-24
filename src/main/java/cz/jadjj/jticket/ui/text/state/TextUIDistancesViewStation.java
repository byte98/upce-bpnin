package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.Map;

/**
 * Class representing state of program which displays table of distances from station
 * @author jadjj
 */
public class TextUIDistancesViewStation extends TextUIState
{

    /**
     * Origin station from which distances will be displayed
     */
    private Station origin;
    
    /**
     * Creates new state of program with table of distances from station
     * @param controller 
     */
    public TextUIDistancesViewStation(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/view/";
        this.screen = new TextUIHTMLTemplateScreen("distances-view-station", "distances-view-station.html");
        this.name = "distances-view-station";
        this.strict = true;
        
        this.helps = new ITextUIHelp[1];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
       Station s = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(data.get("station"));
       if (s != null)
       {
           data.put("station_from", s.getName() + " (" + s.getAbbrevation() + ")");
           data.put("stations_distances_tr", cz.jadjj.jticket.data.Distances.GetInstance().GenerateDistancesRows(s));
           this.origin = s;
           this.commandPrefix = "/data/distances/view/" + s.getAbbrevation().toLowerCase();
           ((TextUIHTMLTemplateScreen) this.screen).setContent(data);           
       }
       return this.screen;
    }
    
    
    @Override
    public void handleInput(String input)
    {
        if (input.toLowerCase().equals("back"))
        {
            this.controller.changeState("distances-view");
        }
    }
    
    
}
