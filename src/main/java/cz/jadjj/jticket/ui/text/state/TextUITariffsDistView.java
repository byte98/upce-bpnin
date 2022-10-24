package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.DistanceTariff;
import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing tariff viewer for distance tariffs
 * @author jadjj
 */
public class TextUITariffsDistView extends TextUIState {

    /**
     * Tariff which is displayed
     */
    private DistanceTariff tariff;
    
    /**
     * Creates new tariff viewer for distance tariffs
     * @param controller Controller of program
     */
    public TextUITariffsDistView(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-dist-view", "tariffs-dist-view.html");
        this.name = "tariffs-dist-view";
        this.strict = true;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("delete", Color.RED, "Smazat tarif");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("back", Color.MAGENTA, "Zpet");
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        this.tariff =(DistanceTariff) cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(data.get("tariff_abbr"));
        if (tariff != null)
        {
            int min = -1, max = 1;
            for (Station from: cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations())
            {
                for (Station to: cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations())
                {
                    int dist = cz.jadjj.jticket.data.Distances.GetInstance().GetDistance(from, to);
                    if (min == -1)
                    {
                        min = dist;
                    }
                    if (max == -1)
                    {
                        max = dist;
                    }
                    if (dist > max)
                    {
                        max = dist;
                    }
                    if (dist < min)
                    {
                        min = dist;
                    }
                }
            }
            data.put("tariff_name", this.tariff.getName());
            data.put("tariff_prices", this.tariff.GeneratePriceListRows(min, max));
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
                this.controller.changeState("tariffs-dist-delete", data); 
                break;
        }
    }    
}
