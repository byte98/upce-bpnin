package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import cz.jadjj.jticket.ui.text.TextUIScreenFactory;
import java.awt.Color;

/**
 * Class representing exit dialog of the program
 * @author jadjj
 */
public class TextUIExit extends TextUIState {

    /**
     * Creates new state of program representing exit dialog
     * @param controller Controller of whole program
     */
    public TextUIExit(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/exit?";
        this.screen = TextUIScreenFactory.createHTMLScreen("exit", "exit.html");
        this.name = "exit";
        
        this.helps = new ITextUIHelp[2];        
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("no", Color.GREEN, "Zrusit");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("yes", Color.RED, "Ukoncit program");
    }

    @Override
    public void handleInput(String input)
    {
        switch (input.toLowerCase())
        {
            case "yes": this.controller.stop(); break;
            case "no": this.controller.changeToPreviousState();                
        }
    }
    
}
