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
 * Class representing creating new distance tariff (with setting prices to distances)
 * @author jadjj
 */
public class TextUITariffsDistPrices extends TextUIState {

    /**
     * Tariff which will be edited
     */
    private DistanceTariff tariff;
    
    /**
     * Actually selected distance
     */
    private int actDistance = 0;
    
    /**
     * Maximal selected distance
     */
    private int maxDistance = 0;
    
    /**
     * Minimal selected distance
     */
    private int minDistance = 0;
    
    /**
     * Creates new dialog for creating new distance tariff (with setting prices to distances)
     * @param controller Controller of program
     */
    public TextUITariffsDistPrices(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-dist-prices", "tariffs-dist-prices.html");
        this.name = "tariffs-dist-prices";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<cele cislo>", Color.YELLOW, "Cena za projetou vzdalenost");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
    }
    
    @Override
    public ITextUIScreen getScreen()
    {
        Map<String, String> data = new HashMap<>();
        data.put("distance_act", Integer.toString(this.actDistance));
        data.put("tariff_name", this.tariff.getName());
        data.put("distance_prices", this.tariff.GeneratePriceListRows(this.minDistance, this.maxDistance));
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        if (this.tariff == null)
        {
            this.tariff = (DistanceTariff) cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(data.get("tariff_abbr"));
            this.commandPrefix = "/data/tariffs/zone/" + data.get("tariff_abbr").toLowerCase();
            int min = -1, max = -1;
            for (Station from: cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations())
            {
                for (Station to: cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations())
                {
                    int distance = cz.jadjj.jticket.data.Distances.GetInstance().GetDistance(from, to);
                    if (min == -1)
                    {
                        min = distance;
                    }
                    if (max == -1)
                    {
                        max = distance;
                    }
                    if (distance < min)
                    {
                        min = distance;
                    }
                    if (distance > max)
                    {
                        max = distance;
                    }
                }
            }
            this.minDistance = min;
            this.maxDistance = max;
            this.actDistance = this.minDistance;
        }
        
        data.put("distance_act", Integer.toString(this.actDistance));
        data.put("tariff_name", this.tariff.getName());
        data.put("distance_prices", this.tariff.GeneratePriceListRows(this.minDistance, this.maxDistance));
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    /**
     * Checks, whether input contains only integer
     * @param input Input which will be checked
     * @return <code>TRUE</code> if input contains integer only, <code>FALSE</code> otherwise
     * @author Jonas K https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
     */
    private boolean checkInt(String input)
    {
        if (input == null)
        {
            return false;
        }
        int length = input.length();
        if (length == 0)
        {
            return false;
        }
        int i = 0;
        if (input.charAt(0) == '-')
        {
            if (length == 1)
            {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++)
        {
            char c = input.charAt(i);
            if (c < '0' || c > '9')
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void handleInput(String input)
    {
        if (input.toLowerCase().equals("cancel"))
        {
            this.controller.changeState("tariffs");
        }
        else if (this.checkInt(input))
        {
            int price = Integer.parseInt(input);
            if (price >= 0)
            {
                this.tariff.SetPrice(this.actDistance, Integer.parseInt(input));
                this.controller.showSucess("Cena pro vzdalenost " + this.actDistance + " km nastavena.");
                this.actDistance++;
                this.controller.reDraw();
                if (this.actDistance > this.maxDistance)
                {
                    this.controller.showSucess("Cenik tarifu '" + this.tariff.getName() + "' byl uspesne vytvoren.");
                    this.controller.changeState("tariffs");
                }
                
            }
            else
            {
                this.controller.showError("Cislo zony musi byt nezaporne cislo!");
            }
        }
        else
        {
            this.controller.showError("Neznamy prikaz '" + input + "'!");
        }
    }
}
