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
 * Class representing edit station form (with selected name option)
 * @author jadjj
 */
public class TextUIStationsEditName extends TextUIState
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
     * Creates new edit station form (with selected name option)
     * @param controller Controller of program
     */
    public TextUIStationsEditName(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/edit:name";
        this.screen = new TextUIHTMLTemplateScreen("stations-edit-name", "stations-edit-name.html");
        this.name = "stations-edit-name";
        this.strict = false;
        
        this.helps = new ITextUIHelp[3];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<nazev stanice>", Color.YELLOW, "Novy nazev stanice");
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
            if (cz.jadjj.jticket.data.Stations.GetInstance().CheckFreeName(input) == false && !input.toLowerCase().equals(this.stationName.toLowerCase()))
            {
                this.controller.showError("Jmeno stanice '" + input + "' jiz existuje!");
            }
            else
            {
                Map<String, String> data = new HashMap<>();
                data.put("station_name", this.stationName);
                data.put("station_abbr", this.stationAbbr);
                data.put("station_new_name", input);
                this.controller.changeState("stations-edit-abbr", data);
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
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
}
