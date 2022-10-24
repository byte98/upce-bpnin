package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Tariff;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing creating new zone tariff (with abbreavation selected)
 * @author jadjj
 */
public class TextUITariffsZoneAbbr extends TextUIState {

    /**
     * Name of tariff
     */
    private String tariffName;
    
    /**
     * Creates new dialog for creating new zone tariff (with abbreavation selected)
     * @param controller Controller of program
     */
    public TextUITariffsZoneAbbr(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/zone:abbr";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-zone-abbr", "tariffs-zone-abbr.html");
        this.name = "tariffs-zone-abbr";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<zkratka tarifu>", Color.YELLOW, "Zkratka tarifu");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
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
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public void handleInput(String input)
    {
        if (input.toLowerCase().equals("cancel"))
        {
            this.controller.changeState("tariffs");
        }
        else
        {
            Tariff t = cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(input);
            if (t != null)
            {
                this.controller.showError("Tarif '" + input + "' jiz existuje!");
            }
            else
            {
                Map<String, String> data = new HashMap<>();
                data.put("tariff_abbr", input);
                data.put("tariff_name", this.tariffName);
                this.controller.changeState("tariffs-zone", data);
            }
        }
    }

    
    
}
