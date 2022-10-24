package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Tariff;
import cz.jadjj.jticket.data.TariffType;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing tariffs menu
 * @author jadjj
 */
public class TextUITariffs extends TextUIState {

    /**
     * Creates new tariffs menu
     * @param controller Controller of program
     */
    public TextUITariffs(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs";
        this.screen = new TextUIHTMLTemplateScreen("tariffs", "tariffs.html");
        this.name = "tariffs";
        this.strict = false;
        
        this.helps = new ITextUIHelp[4];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<jmeno nebo zkratka tarifu>", Color.YELLOW, "Rezim prohlizeni tarifu");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("zone", Color.YELLOW, "Vytvorit novy zonovy tarif");
        this.helps[2] = TextUIHelpFactory.createSimpleHelp("distance", Color.YELLOW, "Vytvorit novy vzdalenostni tarif");
        this.helps[3] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
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
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            case "back":
                this.controller.changeState("data");
                break;
            case "zone":
                this.controller.changeState("tariffs-zone-name");
                break;
            case "distance":
                this.controller.changeState("tariffs-dist-name");
                break;
            default:
                Tariff t = cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(input);
                if (input.equals(""))
                {
                    //pass
                }
                else if (t == null)
                {
                    this.controller.showError("Neznamy prikaz '" + input + "'!");
                }
                else
                {
                    Map<String, String> data = new HashMap<>();
                    data.put("tariff_abbr", t.getAbbr());
                    if (t.getType() == TariffType.ZONE)
                    {
                        this.controller.changeState("tariffs-zone-view", data);
                    }   
                    else if (t.getType() == TariffType.DISTANCE)
                    {
                        this.controller.changeState("tariffs-dist-view", data);
                    }
                }
                break;
        }
    }

    
    
}
