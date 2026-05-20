package com.xabyxd.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.fazecast.jSerialComm.SerialPort;
import com.xabyxd.config.Theme;
import com.xabyxd.utils.SerialManager;

/**
 * Ventana principal. Instancia TopBar, TerminalPanel y SerialManager,
 * y conecta sus callbacks.
 */
public class AppFrame {

    private final TopBar       topBar;
    private final TerminalPanel terminal;
    private final SerialManager serial;

    private static void loadIcon(JFrame frame, String iconPath) {
        URL iconURL = AppFrame.class.getResource(iconPath);
        if (iconURL != null) {
            frame.setIconImage(new ImageIcon(iconURL).getImage());
        } else {
            System.err.println("Icon not found: " + iconPath);
        }
    }

    public AppFrame() {
        serial   = new SerialManager();
        topBar   = new TopBar();
        terminal = new TerminalPanel();

        wireCallbacks();

        JFrame frame = new JFrame("SerialConsole v0.0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 520);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Theme.BG);
        frame.setLayout(new BorderLayout(0, 0));
        frame.add(topBar,    BorderLayout.NORTH);
        frame.add(terminal,  BorderLayout.CENTER);

        loadIcon(frame, "/resources/icon.png");

        frame.setVisible(true);

        // Escaneo inicial de puertos
        refreshPorts();
        terminal.append("Listo. Selecciona un puerto y pulsa Conectar.", Theme.STATUS);
    }

    // ── Callbacks ────────────────────────────────────────────────────────────────

    private void wireCallbacks() {

        // Datos recibidos → terminal
        serial.setOnData(data ->
            terminal.appendSafe(data, Theme.RECV)
        );

        // Eventos de estado → barra superior + terminal
        serial.setOnStatus(msg -> {
            boolean err = msg.startsWith("ERROR");
            Color color = err ? Theme.ERROR : msg.startsWith("Conectado") ? Theme.SUCCESS : Theme.SUBTEXT;
            topBar.setStatus(msg, color);
            terminal.appendSafe(msg.startsWith("ERROR")
                ? "[ERROR] " + msg
                : "─── " + msg + " ───",
                err ? Theme.ERROR : Theme.STATUS);
        });

        // Botón Conectar
        topBar.setOnConnect(() -> {
            String port = topBar.getSelectedPortName();
            if (port == null) return;
            int baud = topBar.getSelectedBaudRate();
            if (serial.connect(port, baud)) {
                topBar.showConnected();
            }
        });

        // Botón Desconectar
        topBar.setOnDisconnect(() -> {
            serial.disconnect();
            topBar.showDisconnected();
        });

        // Botón Refresco
        topBar.setOnRefresh(this::refreshPorts);
    }

    // ── Puerto ────────────────────────────────────────────────────────────────────

    private void refreshPorts() {
        SerialPort[] ports = serial.getAvailablePorts();
        String[] names = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            String sys  = ports[i].getSystemPortName();
            String desc = ports[i].getDescriptivePortName();
            names[i] = sys.equals(desc) ? sys : sys + "  —  " + desc;
        }
        topBar.populatePorts(names);
    }
}