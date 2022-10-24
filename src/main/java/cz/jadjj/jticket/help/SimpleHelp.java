package cz.jadjj.jticket.help;

import java.awt.Color;

/**
 * Class representing simple help
 * @author jadjj
 */
public class SimpleHelp implements Help {
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
    public SimpleHelp(String command, String help, Color color)
    {
        this.command = command;
        this.help = help;
        this.color = color;
    }

    @Override
    public String GetCommand()
    {
        return this.command;
    }

    @Override
    public String GetHelp()
    {
        return this.help;
    }

    @Override
    public Color GetColor()
    {
        return this.color;
    }
    
    
    
}
