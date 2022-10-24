package cz.jadjj.jticket.help;

import java.awt.Color;

/**
 * Interface describing behaviour of help to the commands
 * @author jadjj
 */
public interface Help {
    
    /**
     * Gets command to which help belongs to
     * @return Command to which help belongs to
     */
    public String GetCommand();
    
    /**
     * Gets text of help to the command
     * @return Text of help to the command
     */
    public String GetHelp();
    
    /**
     * Gets colour of command
     * @return colour of command
     */
    public Color GetColor();
}
