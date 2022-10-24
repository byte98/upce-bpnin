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
 * Class representing creating new distance tariff (with name selected)
 * @author jadjj
 */
public class TextUITariffsDistName extends TextUIState {

    /**
     * Creates new dialog for creating new distance tariff (with name selected)
     * @param controller Controller of program
     */
    public TextUITariffsDistName(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/distance:name";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-dist-name", "tariffs-dist-name.html");
        this.name = "tariffs-dist-name";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<jmeno tarifu>", Color.YELLOW, "Jmeno tarifu");
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
                data.put("tariff_name", input);
                this.controller.changeState("tariffs-dist-abbr", data);
            }
        }
    }

    
    
}
