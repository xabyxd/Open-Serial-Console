package com.xabyxd.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.xabyxd.config.Theme;

/**
 * Barra superior con los controles de conexión:
 * selector de puerto, botón de refresco, baud rate,
 * botón conectar/desconectar y etiqueta de estado.
 */
public class TopBar extends JPanel {

    public static final String[] BAUDRATES = {
        "300","600","1200","2400","4800",
        "9600","19200","38400","57600","115200","230400","460800","921600"
    };

    private final JComboBox<String> cmbPort;
    private final JComboBox<String> cmbBaud;
    private final JButton           btnRefresh;
    private final JButton           btnConnect;
    private final JLabel            lblStatus;

    // Callbacks hacia AppFrame
    private Runnable onConnect;
    private Runnable onDisconnect;
    private Runnable onRefresh;

    public TopBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Theme.PANEL);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));

        // Puerto
        add(Theme.label("Puerto:"));
        cmbPort = Theme.combo(new String[]{"Escaneando..."});
        cmbPort.setPreferredSize(new Dimension(200, 28));
        add(cmbPort);

        btnRefresh = Theme.button("↻", Theme.ACCENT);
        btnRefresh.setPreferredSize(new Dimension(34, 28));
        btnRefresh.addActionListener(e -> { if (onRefresh != null) onRefresh.run(); });
        add(btnRefresh);

        add(Box.createHorizontalStrut(8));

        // Baud rate
        add(Theme.label("Baud:"));
        cmbBaud = Theme.combo(BAUDRATES);
        cmbBaud.setSelectedItem("9600");
        cmbBaud.setPreferredSize(new Dimension(110, 28));
        add(cmbBaud);

        add(Box.createHorizontalStrut(8));

        // Conectar
        btnConnect = Theme.button("Conectar", Theme.ACCENT);
        btnConnect.addActionListener(e -> {
            if (isInConnectedState()) {
                if (onDisconnect != null) onDisconnect.run();
            } else {
                if (onConnect != null) onConnect.run();
            }
        });
        add(btnConnect);

        add(Box.createHorizontalStrut(16));

        // Estado
        lblStatus = new JLabel("No conectado");
        lblStatus.setFont(Theme.SANS_S);
        lblStatus.setForeground(Theme.SUBTEXT);
        add(lblStatus);
    }

    // ── Estado visual ─────────────────────────────────────────────────────────────

    public void showConnected() {
        btnConnect.setText("Desconectar");
        setControlsEnabled(false);
    }

    public void showDisconnected() {
        btnConnect.setText("Conectar");
        setControlsEnabled(true);
    }

    public void setStatus(String msg, Color color) {
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText(msg);
            lblStatus.setForeground(color);
        });
    }

    public void populatePorts(String[] displayNames) {
        cmbPort.removeAllItems();
        if (displayNames.length == 0) {
            cmbPort.addItem("Sin puertos");
        } else {
            for (String n : displayNames) cmbPort.addItem(n);
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────────

    /** Nombre del sistema extraído del item seleccionado (antes del "  —  "). */
    public String getSelectedPortName() {
        String s = (String) cmbPort.getSelectedItem();
        if (s == null || s.startsWith("Sin")) return null;
        return s.contains("  —  ") ? s.split("  —  ")[0].trim() : s.trim();
    }

    public int getSelectedBaudRate() {
        return Integer.parseInt((String) cmbBaud.getSelectedItem());
    }

    // ── Callbacks ────────────────────────────────────────────────────────────────

    public void setOnConnect(Runnable r)    { this.onConnect    = r; }
    public void setOnDisconnect(Runnable r) { this.onDisconnect = r; }
    public void setOnRefresh(Runnable r)    { this.onRefresh    = r; }

    // ── Helpers ───────────────────────────────────────────────────────────────────

    private boolean isInConnectedState() {
        return btnConnect.getText().equals("Desconectar");
    }

    private void setControlsEnabled(boolean en) {
        cmbPort.setEnabled(en);
        cmbBaud.setEnabled(en);
        btnRefresh.setEnabled(en);
    }
}