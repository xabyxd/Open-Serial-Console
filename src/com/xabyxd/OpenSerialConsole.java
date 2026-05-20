package com.xabyxd;

import javax.swing.SwingUtilities;

import com.xabyxd.ui.AppFrame;

/**
 * OpenSerialConsole v0.0.1
 * ──────────────────
 * Entry point. Lanza AppFrame en el Event Dispatch Thread.
 */
public class OpenSerialConsole {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppFrame::new);
    }
}