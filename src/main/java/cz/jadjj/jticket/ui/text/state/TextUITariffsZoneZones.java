package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Station;
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
 * Class representing creating new zone tariff (with setting zones to stations)
 * @author jadjj
 */
public class TextUITariffsZoneZones extends TextUIState {

    /**
     * Tariff which will be edited
     */
    private ZoneTariff tariff;
    
    /**
     * Array with all available stations
     */
    private Station[] stations;
    
    /**
     * Index of actually selected station
     */
    private int stIdx;
    
    /**
     * Creates new dialog for creating new zone tariff (with setting zones to stations)
     * @param controller Controller of program
     */
    public TextUITariffsZoneZones(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/tariffs/";
        this.screen = new TextUIHTMLTemplateScreen("tariffs-zone-zones", "tariffs-zone-zones.html");
        this.name = "tariffs-zone-zones";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<cele cislo>", Color.YELLOW, "Cislo zony pro stanici");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
    }

    @Override
    public void load()
    {
        this.stations = cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations();
        this.stIdx = 0;
    }
    
    @Override
    public ITextUIScreen getScreen()
    {
        Map<String, String> data = new HashMap<>();
        data.put("tariff_zones", this.tariff.generateZonesTr());
        data.put("tariff_name", this.tariff.getName());
        data.put("station_name", this.stations[this.stIdx].getName());
        data.put("station_abbr", this.stations[this.stIdx].getAbbrevation());
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        this.tariff = new ZoneTariff(data.get("tariff_name"), data.get("tariff_abbr"));
        this.commandPrefix = "/data/tariffs/zone/" + data.get("tariff_abbr").toLowerCase();
        data.put("tariff_zones", this.tariff.generateZonesTr());
        data.put("station_name", this.stations[this.stIdx].getName());
        data.put("station_abbr", this.stations[this.stIdx].getAbbrevation());
        data.put("tariff_name", this.tariff.getName());
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
            int zone = Integer.parseInt(input);
            if (zone > 0)
            {
                this.tariff.setZone(this.stations[this.stIdx], zone);
                this.controller.showSucess("Zona pro stanici '" + this.stations[this.stIdx].getName() + "' byla nastavene.");
                this.stIdx++;
                if (this.stIdx >= this.stations.length)
                {
                    cz.jadjj.jticket.data.Tariffs.GetInstance().AddTariff(this.tariff);
                    this.controller.showSucess("Zony pro veschny stanice byly uspesne nastaveny!");
                    Map<String, String> data = new HashMap<>();
                    data.put("tariff_abbr", this.tariff.getAbbr());
                    this.controller.changeState("tariffs-zone-prices", data);
                }
                else
                {
                    this.controller.reDraw();
                }
            }
            else
            {
                this.controller.showError("Cislo zony musi byt kladne cislo!");
            }
        }
        else
        {
            this.controller.showError("Neznamy prikaz '" + input + "'!");
        }
    }
    
}
