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
 * Class representing add station form (with selected abbbrevation option)
 * @author jadjj
 */
public class TextUIStationsAddAbbr extends TextUIState
{    
    /**
     * Name of station
     */
    private String stationName;
    
    /**
     * Creates new add station form (with selected abbbrevation option)
     * @param controller Controller of program
     */
    public TextUIStationsAddAbbr(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/add:abbr";
        this.screen = new TextUIHTMLTemplateScreen("stations-add-abbr", "stations-add-abbr.html");
        this.name = "stations-add-abbr";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<zkratka stanice>", Color.YELLOW, "Zkratka stanice");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
        
    }

    @Override
    public void handleInput(String input)
    {
        if ("cancel".equals(input.toLowerCase()))
        {
            this.controller.changeState("stations");   
        }
        else
        {
            if (cz.jadjj.jticket.data.Stations.GetInstance().CheckFreeAbbr(input))
            {
                Map<String, String> data = new HashMap<>();
                data.put("station_name", this.stationName);
                data.put("station_abbr", input);
                this.controller.changeState("stations-add", data);
            }
            else
            {
                this.controller.showError("Zkratka stanice '"+  input + "' je jiz obsazena!");
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
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        this.stationName = data.get("station_name");
        return this.screen;
    }
}
