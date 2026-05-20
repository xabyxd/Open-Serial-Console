package com.xabyxd.utils;

import java.util.function.Consumer;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * Gestiona la conexión serie: descubrimiento de puertos,
 * apertura/cierre y recepción asíncrona de datos.
 */
public class SerialManager {

    private SerialPort       activePort;
    private Consumer<String> onData;    // llamado al recibir bytes
    private Consumer<String> onStatus;  // llamado en eventos de conexión/error

    // ── Puertos ──────────────────────────────────────────────────────────────────

    /** Devuelve todos los puertos detectados por el sistema. */
    public SerialPort[] getAvailablePorts() {
        return SerialPort.getCommPorts();
    }

    // ── Conexión ─────────────────────────────────────────────────────────────────

    public boolean connect(String portName, int baudRate) {
        if (isConnected()) disconnect();

        SerialPort port = SerialPort.getCommPort(portName);
        port.setBaudRate(baudRate);
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (!port.openPort()) {
            emit(onStatus, "ERROR: no se pudo abrir " + portName);
            return false;
        }

        activePort = port;
        activePort.addDataListener(new SerialPortDataListener() {
            @Override public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            @Override public void serialEvent(SerialPortEvent e) {
                if (e.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
                byte[] buf = new byte[activePort.bytesAvailable()];
                activePort.readBytes(buf, buf.length);
                emit(onData, new String(buf));
            }
        });

        emit(onStatus, "Conectado a " + portName + " @ " + baudRate + " baud");
        return true;
    }

    public void disconnect() {
        if (activePort != null && activePort.isOpen()) {
            activePort.removeDataListener();
            activePort.closePort();
        }
        activePort = null;
        emit(onStatus, "Desconectado");
    }

    public boolean isConnected() {
        return activePort != null && activePort.isOpen();
    }

    public String getActivePortName() {
        return activePort != null ? activePort.getSystemPortName() : "—";
    }

    // ── Callbacks ────────────────────────────────────────────────────────────────

    public void setOnData(Consumer<String> cb)   { this.onData   = cb; }
    public void setOnStatus(Consumer<String> cb) { this.onStatus = cb; }

    private void emit(Consumer<String> cb, String msg) {
        if (cb != null) cb.accept(msg);
    }
}