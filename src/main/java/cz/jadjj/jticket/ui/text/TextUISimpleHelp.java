package cz.jadjj.jticket.ui.text;

import java.awt.Color;

/**
 * Class representing simple help for text user interface
 * @author jadjj
 */
public class TextUISimpleHelp implements ITextUIHelp
{
    /**
     * Command to which help belongs to
     */
    private String command;
    
    /**
     * Text of help to the command
     */
    private String help;
    
    /**
     * Color of the command
     */
    private Color color;

    /**
     * Creates new simple help
     * @param command Command to which help belongs to
     * @param help Text of help to the command
     * @param color Color of the command
     */
    public TextUISimpleHelp(String command, String help, Color color)
    {
        this.command = command;
        this.help = help;
        this.color = color;
    }

    @Override
    public String getCommand()
    {
        return this.command;
    }

    @Override
    public String getHelp()
    {
        return this.help;
    }

    @Override
    public Color getColor()
    {
        return this.color;
    }
}
