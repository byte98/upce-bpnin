package cz.jadjj.jticket.help;

import java.awt.Color;

/**
 * Class which can create simple helps
 * @author jadjj
 */
public class HelpFactory {
    
    /**
     * Creates new help to the command
     * @param command Command to which help belongs to
     * @param color Color of the command
     * @param help Text of help to the command
     * @return New simple help to the command
     */
    public static SimpleHelp CreateSimpleHelp(String command, Color color, String help)
    {
        return new SimpleHelp(command, help, color);
    }
}
