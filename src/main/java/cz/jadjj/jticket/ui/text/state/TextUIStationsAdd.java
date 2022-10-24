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
 * Class representing add station form (with displayed confirmation)
 * @author jadjj
 */
public class TextUIStationsAdd extends TextUIState
{

    /**
     * Name of station
     */
    private String stationName;
    
    /**
     * Abbreavation of station
     */
    private String stationAbbr;
    
    /**
     * Creates new add station form (with displayed confirmation)
     * @param controller Controller of program
     */
    public TextUIStationsAdd(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/add?";
        this.screen = new TextUIHTMLTemplateScreen("stations-add", "stations-add.html");
        this.name = "stations-add";
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("yes", Color.GREEN, "Udaje jsou v poradku, pridat stanici");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("no", Color.RED, "Zrusit");
        
    }

    @Override
    public void handleInput(String input)
    {
        if ("yes".equals(input))
        {
            Station st = new Station(this.stationAbbr, this.stationName);
            cz.jadjj.jticket.data.Stations.GetInstance().AddStation(st);
            System.out.printf("New station (name: %s, abbreavation: %s) has been added\n", this.stationName, this.stationAbbr);
            this.controller.showSucess("Stanice '" + this.stationName + " (" + this.stationAbbr + ")' byla uspesne pridana!");
            this.controller.changeState("stations");
        }
        else
        {
            this.controller.changeState("stations");
        }
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
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        data.put("stations_tr", cz.jadjj.jticket.data.Stations.GetInstance().GenerateTableRows());
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        this.stationAbbr = data.get("station_abbr");
        this.stationName = data.get("station_name");
        return this.screen;
    }
}
