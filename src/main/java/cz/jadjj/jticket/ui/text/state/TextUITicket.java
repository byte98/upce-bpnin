package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.data.Tariff;
import cz.jadjj.jticket.data.Ticket;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing ticket selling state of program
 * @author jadjj
 */
public class TextUITicket extends TextUIState{

    /**
     * Progress in filling form to sell ticket
     */
    private byte progress = 0;
        
    /**
     * Array with all needed screens to fill ticket data
     */
    private final TextUIHTMLTemplateScreen[] screens;
    
    /**
     * Array with helps to each state of filling ticket data
     */
    private ITextUIHelp[][] helps;
    
    /**
     * Tariff which calculates tickets price
     */
    private Tariff tariff;
    
    /**
     * Origin station of ticket
     */
    private Station origin;
    
    /**
     * Destination of ticket
     */
    private Station destination;
    
    /**
     * Ticket which information will be collected
     */
    private Ticket ticket;
    
    /**
     * Creates new state of ticket selling
     * @param controller Controller of program
     */
    public TextUITicket(TextUIController controller)
    {
        super(controller);
        this.strict = false;
        this.name = "ticket";
        this.commandPrefix = "/ticket:tariff";
        this.helps = new ITextUIHelp[4][];
        
        this.screens = new TextUIHTMLTemplateScreen[4];
        this.screens[0] = new TextUIHTMLTemplateScreen("ticket-tariff", "ticket-tariff.html");
        this.screens[1] = new TextUIHTMLTemplateScreen("ticket-from", "ticket-from.html");
        this.screens[2] = new TextUIHTMLTemplateScreen("ticket-to", "ticket-to.html");
        this.screens[3] = new TextUIHTMLTemplateScreen("ticket", "ticket.html");
        
        this.helps[0] = new ITextUIHelp[2];
        this.helps[0][0] = TextUIHelpFactory.createSimpleHelp("<jmeno nebo zkratka tarifu>", Color.YELLOW, "Jmeno nebo zkratka tarifu, podle ktereho se vypocita cena");
        this.helps[0][1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
        
        this.helps[1] = new ITextUIHelp[2];
        this.helps[1][0] = TextUIHelpFactory.createSimpleHelp("<jmeno nebo zkratka stanice>", Color.YELLOW, "Jmeno nebo zkratka vychozi stanice");
        this.helps[1][1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
        
        this.helps[2] = new ITextUIHelp[2];
        this.helps[2][0] = TextUIHelpFactory.createSimpleHelp("<jmeno nebo zkratka stanice>", Color.YELLOW, "Jmeno nebo zkratka cilove stanice");
        this.helps[2][1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
        
        this.helps[3] = new ITextUIHelp[2];
        this.helps[3][0] = TextUIHelpFactory.createSimpleHelp("yes", Color.GREEN, "Ano, zadane udaje jsou v poradku");
        this.helps[3][1] = TextUIHelpFactory.createSimpleHelp("no", Color.RED, "Zrusit");
    }
    
    /**
     * Generates content which will be displayed
     * @return Content which will be displayed
     */
    private Map<String, String> generateContent()
    {
        Map<String, String> reti = new HashMap<>();
        if (this.ticket != null)
        {
            reti.put("ticket_tariff", this.ticket.getTariffText());
            reti.put("ticket_id", this.ticket.getId() == null ? " " : this.ticket.getId());
            reti.put("ticket_from", this.ticket.getFromText());
            reti.put("ticket_to", this.ticket.getToText());
            reti.put("ticket_distance", this.ticket.getDistanceText());
            reti.put("ticket_validity", this.ticket.getValidityText());
            reti.put("ticket_price", this.ticket.getPriceText());
        }
        else
        {
            reti.put("ticket_tariff", " ");
            reti.put("ticket_id", " ");
            reti.put("ticket_from", " ");
            reti.put("ticket_to", " ");
            reti.put("ticket_distance", " ");
            reti.put("ticket_validity", " ");
            reti.put("ticket_price", " ");
        }
        return reti;
    }
    
    @Override
    public void load()
    {
        this.commandPrefix = "/ticket:tariff";
        this.progress = 0;
    }
    
    @Override
    public ITextUIHelp[] getHelps()
    {
        return this.helps[this.progress];
    }
    
    
    @Override
    public ITextUIScreen getScreen()
    {
        if (this.progress == 0) this.controller.showTariffsHelp();
        else this.controller.showStationsHelp();
        TextUIHTMLTemplateScreen reti = this.screens[this.progress];
        reti.setContent(this.generateContent());
        return reti;
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        if (this.progress == 0) this.controller.showTariffsHelp();
        else this.controller.showStationsHelp();
        TextUIHTMLTemplateScreen reti = this.screens[this.progress];
        this.generateContent().keySet().forEach(tD -> {
            data.put(tD, this.generateContent().get(tD));
        });
        reti.setContent(data);
        return reti;
    }
    

    @Override
    public void handleInput(String input)
    {
        if (input.length() > 0)
        {
            if (progress < 3 && input.toLowerCase().equals("cancel"))
            {
                this.controller.hideHelp();
                this.controller.changeState("welcome");
            }
            else
            {
                switch (this.progress)
                {
                    case 0:
                        Tariff t = cz.jadjj.jticket.data.Tariffs.GetInstance().GetTariff(input);
                        if (t == null)
                        {
                            this.controller.showError("Neznamy tarif '" + input + "'!");
                        }
                        else
                        {
                            this.tariff = t;
                            this.ticket = new Ticket();
                            this.ticket.initializeTicket();
                            this.ticket.setTariff(t);
                            this.commandPrefix = "/ticket:from";
                            this.progress++;
                            this.controller.reDraw();
                        }
                        break;
                    case 1:
                        Station s = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(input);
                        if (s == null)
                        {
                            this.controller.showError("Neznama stanice '" + input + "'!");
                        }
                        else
                        {
                            this.origin = s;
                            this.ticket.setOrigin(s);
                            this.commandPrefix = "/ticket:to";
                            this.progress++;
                            this.controller.reDraw();
                        }
                        break;
                    case 2:
                        Station to = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(input);
                        if (to == null)
                        {
                            this.controller.showError("Neznama stanice '" + input + "'!");
                        }
                        else
                        {
                            this.destination = to;
                            this.ticket.setDestination(to);
                            this.commandPrefix = "/ticket?";
                            this.progress++;
                            this.controller.reDraw();
                        }
                        break;
                    case 3:
                        if (input.toLowerCase().equals("no"))
                        {
                            this.controller.hideHelp();
                            this.controller.changeState("welcome");
                        }
                        else if (input.toLowerCase().equals("yes"))
                        {
                            this.ticket.printToPdf(this.ticket.getId() + ".pdf");
                            this.controller.showSucess("Jizdenka s cislem " + this.ticket.getId() + " byla uspesne vytvorena.");
                            this.controller.hideHelp();
                            this.controller.changeState("welcome");
                            this.controller.changeState("ticket");
                        }
                        else
                        {
                            this.controller.showError("Neznamy prikaz '" + input + "'!");
                        }
                        break;
                }
            }
            
        }
    }
}
