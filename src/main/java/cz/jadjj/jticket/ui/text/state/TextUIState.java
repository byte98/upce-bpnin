package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import java.util.Map;

/**
 * Class representing state of program using text user interface
 * @author jadjj
 */
public abstract class TextUIState
{
    /**
     * Controller of the program
     */
    protected TextUIController controller;
    
    /**
     * Name of state
     */
    protected String name;
    
    /**
     * Prefix of state in command line
     */
    protected String commandPrefix;
    
    /**
     * Screen of state of program
     */
    protected ITextUIScreen screen;
    
    /**
     * Array of available commands and help to them
     */
    protected ITextUIHelp[] helps;
    
    /**
     * Flag, whether input is strict to helps only or not
     */
    protected boolean strict = true;
    
    /**
     * Creates new state of program
     * @param controller Controller of program
     */
    public TextUIState(TextUIController controller)
    {
        this.controller = controller;
    }
    
    
    /**
     * Gets name of state of program
     * @return Name of state of program
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Gets prefix of state in command line
     * @return Prefix of state in command line
     */
    public String getCommandPrefix()
    {
        return this.commandPrefix;
    }
    
    /**
     * Gets screen representing state of program
     * @return Screen representing state of program
     */
    public ITextUIScreen getScreen()
    {
        return this.screen;
    }
    
    /**
     * Gets screen representing state of program
     * @param data Data which will be displayed on screen
     * @return Screen representing state of program
     */
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        ITextUIScreen reti = this.screen;
        if (data != null && this.screen instanceof TextUIHTMLTemplateScreen)
        {
            TextUIHTMLTemplateScreen actualScreen = (TextUIHTMLTemplateScreen)this.screen;
            actualScreen.setContent(data);
            reti = actualScreen;
        }
        return reti;
    }
    
    /**
     * Gets array of available commands and helps to them
     * @return Array of available commands and helps to them
     */
    public ITextUIHelp[] getHelps()
    {
        return this.helps;
    }
    
    /**
     * Gets flag, whether input has to be limited to helps only or not
     * @return <code>TRUE</code> if helps has to be limited to helps only, <code>FALSE</code> otherwise
     */
    public boolean getStrict()
    {
        return this.strict;
    }
    
    /**
     * Handles input from command line
     * @param input Input from command line
     */
    public abstract void handleInput(String input);
    
    /**
     * Loads screen
     */
    public void load(){};
    
    /**
     * Gets control to state
     */
    public void control(){};

}
