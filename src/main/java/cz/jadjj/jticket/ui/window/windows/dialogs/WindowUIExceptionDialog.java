package cz.jadjj.jticket.ui.window.windows.dialogs;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Class representing window with exception details
 * @author jadjj
 */
public class WindowUIExceptionDialog extends WindowUIDialog
{
    /**
     * Exception which will be displayed
     */
    private Exception exception;
    
    /**
     * Creates new dialog for displaying exception
     * @param ex Exception which will be displayed
     */
    public WindowUIExceptionDialog(Exception ex)
    {
        super("Neočekáváná chyba", null, WindowUIDialogType.ERROR, WindowUIButtonType.OK);
    }
    
    @Override
    protected JPanel initializeBody()
    {
        JPanel reti = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        reti.add(this.initializeIcon());
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(new JLabel("Byla zachycena neočekáváná výjimka. Podrobnosti o výjimce naleznete níže."));
        content.add(new JLabel("Pokud se rozhodnete pokračovat v práci s aplikací, mohou se vyskytnout potíže."));
        content.add(new JLabel("Výjimka: " + (this.exception == null ? "<neznámá vyjímka>" : this.exception.toString())));
        JTextArea exInfo = new JTextArea();
        exInfo.setEditable(false);
        if (this.exception != null)
        {
            for (StackTraceElement elem: this.exception.getStackTrace())
            {
                exInfo.append(elem.toString());
            }
        }
        else
        {
            exInfo.append("<žádné informace nejsou k dispozici>");
        }
        content.add(exInfo);
        reti.add(content);
        return reti;
    }
}
