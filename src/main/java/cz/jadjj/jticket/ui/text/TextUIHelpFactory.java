package cz.jadjj.jticket.ui.text;

import java.awt.Color;

/**
 * Class which can be used to create simple helps
 * @author jadjj
 */
public class TextUIHelpFactory
{
    /**
     * Creates new help to the command
     * @param command Command to which help belongs to
     * @param color Color of the command
     * @param help Text of help to the command
     * @return New simple help to the command
     */
    public static TextUISimpleHelp createSimpleHelp(String command, Color color, String help)
    {
        return new TextUISimpleHelp(command, help, color);
    }
}
