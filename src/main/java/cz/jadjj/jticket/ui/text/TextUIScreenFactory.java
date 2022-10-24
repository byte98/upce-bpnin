package cz.jadjj.jticket.ui.text;

/**
 * Class which can create every screen needed
 * @author jadjj
 */
public class TextUIScreenFactory
{
    /**
     * Creates new HTML screen
     * @param name Name of screen
     * @param fileName File containing content of the screen
     * @return Screen with selected name and content
     */
    public static TextUIHTMLScreen createHTMLScreen(String name, String fileName)
    {
        return new TextUIHTMLScreen(name, fileName);
    }
}
