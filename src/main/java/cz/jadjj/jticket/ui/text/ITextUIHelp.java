package cz.jadjj.jticket.ui.text;

import java.awt.Color;

/**
 * Class representing help which will be displayed on screen
 * @author jadjj
 */
public interface ITextUIHelp
{
    /**
     * Gets command to which help belongs to
     * @return Command to which help belongs to
     */
    public String getCommand();
    
    /**
     * Gets text of help to the command
     * @return Text of help to the command
     */
    public String getHelp();
    
    /**
     * Gets colour of command
     * @return colour of command
     */
    public Color getColor();
}
