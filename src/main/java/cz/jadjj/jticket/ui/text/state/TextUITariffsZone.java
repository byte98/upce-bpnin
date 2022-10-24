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
 * Class representing creating new zone tariff (with confirmation dialog)
 * @author jadjj
 */
public class TextUITariffsZone extends TextUIState {

    /**
     * Name of tariff
     */
    private String tariffName;
    
    /**
     * Abbreavation of tariff
     */
    private String tariffAbbr;
    
    /**
     * Creates new dialog for creating new zone tariff (with confirmation dialog)
     * @param controller Controller of program
     */
    public TextUITariffsZone(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/zone?";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-zone", "tariffs-zone.html");
        this.name = "tariffs-zone";
        this.strict = true;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("yes", Color.GREEN, "Ano, zadane udaje jsou v poradku");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("no", Color.RED, "Zrusit");
    }

    @Override
    public ITextUIScreen getScreen()
    {
        Map<String, String> data = new HashMap<>();
        data.put("tariffs_tr", cz.jadjj.jticket.data.Tariffs.GetInstance().GenerateTariffsTableRows());
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        data.put("tariffs_tr", cz.jadjj.jticket.data.Tariffs.GetInstance().GenerateTariffsTableRows());
        this.tariffName = data.get("tariff_name");
        this.tariffAbbr = data.get("tariff_abbr");
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public void handleInput(String input)
    {
        switch(input.toLowerCase())
        {
            case "no": this.controller.changeState("tariffs"); break;
            case "yes": 
                Map<String, String> data = new HashMap<>();
                data.put("tariff_name", this.tariffName);
                data.put("tariff_abbr", this.tariffAbbr);
                System.out.format("Tariff (type: %s, name: %s, abbreavation: %s) has been created\n", "ZONE", this.tariffName, this.tariffAbbr);
                this.controller.changeState("tariffs-zone-zones", data);                
                break;
        }
    }
    
}
