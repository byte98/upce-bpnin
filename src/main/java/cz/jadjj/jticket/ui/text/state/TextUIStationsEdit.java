package cz.jadjj.jticket.ui.text.state;


import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing edit station form
 * @author jadjj
 */
public class TextUIStationsEdit extends TextUIState
{
    
    /**
     * Abbreavation of station
     */
    private String stationAbbr = null;
    
    /**
     * Name of station
     */
    private String stationName = null;
    
    /**
     * New abbreavation of station
     */
    private String newStationAbbr = null;
    
    /**
     * New name of station
     */
    private String newStationName = null;

    /**
     * Creates new edit station form
     * @param controller Controller of program
     */
    public TextUIStationsEdit(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/edit?";
        this.screen = new TextUIHTMLTemplateScreen("stations-edit", "stations-edit.html");
        this.name = "stations-edit";
        this.strict = true;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("yes", Color.GREEN, "Udaje jsou v poradku, zmenit stanici");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("no", Color.RED, "Zrusit");
        
    }

    @Override
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            case "no":
                this.controller.changeState("stations");
                break;
            case "yes":
                cz.jadjj.jticket.data.Stations.GetInstance().EditStation(cz.jadjj.jticket.data.Stations.GetInstance().GetStation(this.stationAbbr), this.newStationName, this.newStationAbbr);
                this.controller.showSucess("Stanice '" + this.stationName + " (" + this.stationAbbr + ")' byla uspesne zmenena.");
                this.controller.changeState("stations");
                break;
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
        this.stationAbbr = data.get("station_abbr");
        this.stationName = data.get("station_name");
        this.newStationAbbr = data.get("station_new_abbr");
        this.newStationName = data.get("station_new_name");
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
}
