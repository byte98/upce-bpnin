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
 * Class representing edit station form (with selected abbreaviation option)
 * @author jadjj
 */
public class TextUIStationsEditAbbr extends TextUIState
{
    
    /**
     * Abbreaviation of station
     */
    private String stationAbbr = null;
    
    /**
     * Name of station
     */
    private String stationName = null;
    
    /**
     * New name of station
     */
    private String newStationName = null;

    /**
     * Creates new edit station form (with selected abbreaviation option)
     * @param controller Controller of program
     */
    public TextUIStationsEditAbbr(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/edit:abbr";
        this.screen = new TextUIHTMLTemplateScreen("stations-edit-abbr", "stations-edit-abbr.html");
        this.name = "stations-edit-abbr";
        this.strict = false;
        
        this.helps = new ITextUIHelp[3];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<zkratka stanice>", Color.YELLOW, "Nova zkratka stanice");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("delete", Color.MAGENTA, "Smazat stanici");
        this.helps[2] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
        
    }

    @Override
    public void handleInput(String input)
    {
        if ("cancel".equals(input.toLowerCase()))
        {
            this.controller.changeState("stations");   
        }
        else if ("delete".equals(input.toLowerCase()))
        {
            Map<String, String> data = new HashMap<>();
            data.put("station_name", this.stationName);
            data.put("station_abbr", this.stationAbbr);
            this.controller.changeState("stations-delete", data);
        }
        else
        {
            if (cz.jadjj.jticket.data.Stations.GetInstance().CheckFreeAbbr(input.toLowerCase()) == false && !input.toLowerCase().equals(this.stationAbbr.toLowerCase()))
            {
                this.controller.showError("Zkratka '" + input + "' je jiz obsazena!");
            }
            else
            {
                Map<String, String> data = new HashMap<>();
                data.put("station_name", this.stationName);
                data.put("station_abbr", this.stationAbbr);
                data.put("station_new_name", this.newStationName);
                data.put("station_new_abbr", input);
                this.controller.changeState("stations-edit", data);
            }            
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
        this.newStationName = data.get("station_new_name");
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
}
