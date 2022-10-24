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
 * Class representing tariff viewer for zone tariffs (with deleting dialog)
 * @author jadjj
 */
public class TextUITariffsZoneDelete extends TextUIState {

    /**
     * Tariff which is displayed
     */
    private ZoneTariff tariff;
    
    /**
     * Creates new tariff viewer for zone tariffs (with deleting dialog)
     * @param controller Controller of program
     */
    public TextUITariffsZoneDelete(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-zone-delete", "tariffs-zone-delete.html");
        this.name = "tariffs-zone-delete";
        this.strict = true;
        
        this.helps = new ITextUIHelp[2];
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("no", Color.GREEN, "Zrusit");
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("yes", Color.RED, "Smazat tarif");
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
            this.commandPrefix = "/data/tariffs/" + this.tariff.getAbbr().toLowerCase() + "/delete?";
        }
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public void handleInput(String input)
    {
        switch(input.toLowerCase())
        {
            case "no": 
                Map<String, String> data = new HashMap<>();
                data.put("tariff_abbr", this.tariff.getAbbr());
                this.controller.changeState("tariffs-zone-view"); 
                break;
            case "yes":
                String tarifName = this.tariff.getName();
                cz.jadjj.jticket.data.Tariffs.GetInstance().RemoveTariff(this.tariff);
                this.controller.showSucess("Tarif '" + tarifName + "' byl uspesne odebran.");
                this.controller.changeState("tariffs");
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
