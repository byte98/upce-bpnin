package cz.jadjj.jticket.ui.text;

/**
 * Class representing screen which is displayed when using text user interface
 * @author jadjj
 */
public interface ITextUIScreen
{
    /**
     * Gets content which will be displayed on the screen
     * @return Content which will be displayed on the screen
     */
    public String getContent();
    
    /**
     * Gets name of the screen
     * @return Name of the screen
     */
    public String getName();
}
