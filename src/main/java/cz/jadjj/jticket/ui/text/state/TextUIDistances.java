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
 * Class representing state of program which displays distances menu
 * @author jadjj
 */
public class TextUIDistances extends TextUIState
{

    /**
     * Creates new state of program with distances menu
     * @param controller Controller of program
     */
    public TextUIDistances(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances";
        this.screen = new TextUIHTMLTemplateScreen("distances", "distances.html");
        this.name = "distances";
        this.strict = true;
        
        this.helps = new ITextUIHelp[4];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("create", Color.YELLOW, "Rezim vytvareni tabulky vzdalenosti");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("set", Color.YELLOW, "Rezim upravy tabulky vzdalenosti");
        this.helps[2] = TextUIHelpFactory.createSimpleHelp("view", Color.YELLOW, "Rezim prohlizeni tabulky vzdalenosti");
        this.helps[3] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
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
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            case "back": this.controller.changeState("data"); break;
            case "create": this.controller.changeState("distances-create"); break;
            case "view": this.controller.changeState("distances-view"); break;
            case "set": this.controller.changeState("distances-set-from"); break;
            
        }
    }
    
}
