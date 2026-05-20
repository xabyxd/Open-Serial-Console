package com.xabyxd.config;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/** Colores, fuentes y helpers de widgets compartidos. */
public class Theme {

    // ── Paleta ───────────────────────────────────────────────────────────────────
    public static final Color BG       = hex("#1E1E2E");
    public static final Color PANEL    = hex("#181825");
    public static final Color TEXT     = hex("#CDD6F4");
    public static final Color SUBTEXT  = hex("#6C7086");
    public static final Color ACCENT   = hex("#7C6AF7");
    public static final Color RECV     = hex("#89DCEB");
    public static final Color STATUS   = hex("#9399B2");
    public static final Color ERROR    = hex("#F38BA8");
    public static final Color SUCCESS  = hex("#A6E3A1");
    public static final Color TERMINAL = hex("#11111B");
    public static final Color BORDER   = hex("#313244");
    public static final Color COMBO_BG = hex("#2A2A3E");

    // ── Fuentes ──────────────────────────────────────────────────────────────────
    public static final Font MONO  = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font SANS  = new Font("SansSerif",  Font.PLAIN, 12);
    public static final Font SANS_S= new Font("SansSerif",  Font.PLAIN, 11);
    public static final Font BOLD  = new Font("SansSerif",  Font.BOLD,  12);

    private Theme() {}

    // ── Widget factories ──────────────────────────────────────────────────────────

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(SANS);
        l.setForeground(TEXT);
        return l;
    }

    public static JComboBox<String> combo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(COMBO_BG);
        c.setForeground(TEXT);
        c.setFont(SANS);
        c.setFocusable(false);
        return c;
    }

    public static JButton button(String label, Color bg) {
        JButton b = new JButton(label);
        b.setBackground(bg);
        b.setForeground(TEXT);
        b.setFont(BOLD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void styleScrollBar(JScrollBar sb) {
        sb.setBackground(TERMINAL);
        sb.setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = BORDER;
                trackColor = TERMINAL;
            }
            @Override protected JButton createDecreaseButton(int o) { return ghost(); }
            @Override protected JButton createIncreaseButton(int o) { return ghost(); }
            private JButton ghost() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });
    }

    private static Color hex(String h) { return Color.decode(h); }
}