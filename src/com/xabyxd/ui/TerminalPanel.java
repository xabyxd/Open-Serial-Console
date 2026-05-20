package com.xabyxd.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.xabyxd.config.Theme;

/** Panel de terminal: muestra texto recibido con colores. Solo lectura. */
public class TerminalPanel extends JScrollPane {

    private final JTextPane    textPane;
    private final StyledDocument doc;

    public TerminalPanel() {
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(Theme.TERMINAL);
        textPane.setForeground(Theme.TEXT);
        textPane.setFont(Theme.MONO);
        textPane.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        doc = textPane.getStyledDocument();

        setViewportView(textPane);
        setBorder(null);
        setBackground(Theme.TERMINAL);
        Theme.styleScrollBar(getVerticalScrollBar());
    }

    /** Añade texto al terminal con el color indicado. Llama siempre desde el EDT. */
    public void append(String text, Color color) {
        try {
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, color);
            StyleConstants.setFontFamily(attr, "Monospaced");
            StyleConstants.setFontSize(attr, 13);
            String out = text.endsWith("\n") ? text : text + "\n";
            doc.insertString(doc.getLength(), out, attr);
        } catch (BadLocationException ignored) {}
        textPane.setCaretPosition(doc.getLength());
    }

    /** Variante thread-safe: despacha al EDT si es necesario. */
    public void appendSafe(String text, Color color) {
        SwingUtilities.invokeLater(() -> append(text, color));
    }
}