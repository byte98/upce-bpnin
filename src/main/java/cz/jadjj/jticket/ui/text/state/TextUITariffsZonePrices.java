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
 * Class representing creating new zone tariff (with setting prices to zones)
 * @author jadjj
 */
public class TextUITariffsZonePrices extends TextUIState {

    /**
     * Tariff which will be edited
     */
    private ZoneTariff tariff;
    
    /**
     * Actually selected zone
     */
    private int actZone = 0;
    
    /**
     * Maximal selected zone
     */
    private int maxZone = 0;
    
    /**
     * Creates new dialog for creating new zone tariff (with setting prices to zones)
     * @param controller Controller of program
     */
    public TextUITariffsZonePrices(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-zone-prices", "tariffs-zone-prices.html");
        this.name = "tariffs-zone-prices";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<cele cislo>", Color.YELLOW, "Cena za projete zony");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
    }
    
    @Override
    public ITextUIScreen getScreen()
    {
        Map<String, String> data = new HashMap<>();
        data.put("zones_count", Integer.toString(this.actZone));
        data.put("tariff_name", this.tariff.getName());
        data.put("zones_prices", this.getZonePrices());
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        if (this.tariff == null)
        {
            this.tariff = (ZoneTariff) cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(data.get("tariff_abbr"));
            this.commandPrefix = "/data/tariffs/zone/" + data.get("tariff_abbr").toLowerCase();
            int min = -1, max = -1;
            for (int zone: this.tariff.GetAllZones().values())
            {
                if (min == -1)
                {
                    min = zone;
                }
                if (max == -1)
                {
                    max = zone;
                }
                if (zone < min)
                {
                    min = zone;
                }
                if (zone > max)
                {
                    max = zone;
                }
            }
        this.maxZone = max - min;
        }
        
        data.put("zones_count", Integer.toString(this.actZone));
        data.put("tariff_name", this.tariff.getName());
        data.put("zones_prices", this.getZonePrices());
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
                this.tariff.SetPrice(this.actZone, price);
                this.controller.showSucess("Cena pro " + this.actZone + " zony byla nastavena.");
                this.actZone++;
                if (this.actZone > this.maxZone)
                {
                    this.controller.showSucess("Ceny pro vsechny zony byly uspesne nastaveny!");
                    this.controller.changeState("tariffs");
                }
                else
                {
                    this.controller.reDraw();
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
    
    /**
     * Gets HTML table rows with zone prices
     * @return String with HTML table rows with zone prices
     */
    private String getZonePrices()
    {
        String reti = new String();
        
        for (int i = 0; i <= this.maxZone; i++)
        {
            reti += "<tr><td>" + i + "</td><td style='color: white;'>";
            reti += (this.tariff.GetAllPrices().get(i) == null ? " " : this.tariff.GetAllPrices().get(i) + " Kc");
            reti += "</td></tr>";
        }
        return reti;
    }
}
