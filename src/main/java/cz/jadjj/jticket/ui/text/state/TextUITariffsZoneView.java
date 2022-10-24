package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.ZoneTariff;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing tariff viewer for zone tariffs
 * @author jadjj
 */
public class TextUITariffsZoneView extends TextUIState {

    /**
     * Tariff which is displayed
     */
    private ZoneTariff tariff;
    
    /**
     * Creates new tariff viewer for zone tariffs
     * @param controller Controller of program
     */
    public TextUITariffsZoneView(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-zone-view", "tariffs-zone-view.html");
        this.name = "tariffs-zone-view";
        this.strict = true;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("delete", Color.RED, "Smazat tarif");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        this.tariff =(ZoneTariff) cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(data.get("tariff_abbr"));
        if (tariff != null)
        {
            data.put("tariff_name", this.tariff.getName());
            data.put("tariff_zones", this.tariff.generateZonesTr());
            data.put("tariff_prices", this.getTariffPrices());
            this.commandPrefix = "/data/tariffs/" + this.tariff.getAbbr().toLowerCase();
        }
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public void handleInput(String input)
    {
        switch(input.toLowerCase())
        {
            case "back": this.controller.changeState("tariffs"); break;
            case "delete": 
                Map<String, String> data = new HashMap<>();
                data.put("tariff_abbr", this.tariff.getAbbr());
                this.controller.changeState("tariffs-zone-delete", data); 
                break;
        }
    }
    
    /**
     * Gets table rows with tariff row prices
     * @return 
     */
    private String getTariffPrices()
    {
        String reti = new String();
        reti = this.tariff.GetAllPrices().keySet().stream().map(zone -> "<tr><td>" + zone + "</td><td style='color: white;'>" + this.tariff.GetAllPrices().get(zone) + " Kc</td></tr").reduce(reti, String::concat);
        return reti;
    }
    
}
