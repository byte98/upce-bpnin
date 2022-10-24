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
 * Class representing delete station dialog
 * @author jadjj
 */
public class TextUIStationsDelete extends TextUIState
{
    /**
     * Abbrevation of station
     */
    private String stationAbbr;
    
    /**
     * Name of the station
     */
    private String stationName;
    
    /**
     * Creates new delete station dialog
     * @param controller Controller of program
     */
    public TextUIStationsDelete(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/delete?";
        this.screen = new TextUIHTMLTemplateScreen("stations-delete", "stations-delete.html");
        this.name = "stations-delete";
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("no", Color.GREEN, "Zrusit");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("yes", Color.RED, "Smazat stanici");
        
    }

    @Override
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            
            case "no": 
                Map<String, String> data = new HashMap<>();
                data.put("station_name", this.stationName);
                data.put("station_abbr", this.stationAbbr);
                this.controller.changeState("stations-edit-name", data);
                break;
            case "yes":
                cz.jadjj.jticket.data.Stations.GetInstance().DeleteStation(cz.jadjj.jticket.data.Stations.GetInstance().GetStation(this.stationAbbr));
                this.controller.showSucess("Stanice '" + this.stationName + "' byla uspesne vymazana.");
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
        this.stationAbbr = data.get("station_abbr");
        this.stationName = data.get("station_name");
        data.put("stations_tr", cz.jadjj.jticket.data.Stations.GetInstance().GenerateTableRows());
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
}
