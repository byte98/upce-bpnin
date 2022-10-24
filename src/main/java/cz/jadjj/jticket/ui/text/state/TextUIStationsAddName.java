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
 * Class representing add station form (with selected name option)
 * @author jadjj
 */
public class TextUIStationsAddName extends TextUIState
{

    /**
     * Creates new add station form (with selected name option)
     * @param controller Controller of program
     */
    public TextUIStationsAddName(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/stations/add:name";
        this.screen = new TextUIHTMLTemplateScreen("stations-add-name", "stations-add-name.html");
        this.name = "stations-add-name";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<nazev stanice>", Color.YELLOW, "Nazev stanice");
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
            if (cz.jadjj.jticket.data.Stations.GetInstance().CheckFreeName(input))
            {
                Map<String, String> data = new HashMap<>();
                data.put("station_name", input);
                this.controller.changeState("stations-add-abbr", data);
            }
            else
            {
                this.controller.showError("Stanice '" + input + "' jiz existuje!");
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
}
