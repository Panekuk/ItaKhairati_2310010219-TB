package form;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.Border;
import java.awt.Toolkit;

public class RegexFilter extends DocumentFilter {

    private final String regex;
    private final int maxLength;
    private final JComponent component;
    private final Border normalBorder;
    private final Border errorBorder;

    public RegexFilter(String regex, int maxLength,
                       JComponent component,
                       Border normalBorder,
                       Border errorBorder) {
        this.regex = regex;
        this.maxLength = maxLength;
        this.component = component;
        this.normalBorder = normalBorder;
        this.errorBorder = errorBorder;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {

        if (text == null) return;

        String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;

        if (newText.matches(regex) && newText.length() <= maxLength) {
            component.setBorder(normalBorder);
            super.insertString(fb, offset, text, attr);
        } else {
            component.setBorder(errorBorder);
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs)
            throws BadLocationException {

        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset)
                + (text == null ? "" : text)
                + currentText.substring(offset + length);

        if (newText.matches(regex) && newText.length() <= maxLength) {
            component.setBorder(normalBorder);
            super.replace(fb, offset, length, text, attrs);
        } else {
            component.setBorder(errorBorder);
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
