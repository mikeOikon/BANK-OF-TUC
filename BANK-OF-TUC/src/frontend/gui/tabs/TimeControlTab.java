package frontend.gui.tabs;

import backend.BankSystem;
import backend.FileLogger;
import backend.users.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class TimeControlTab extends JPanel {

    private final JLabel nowLabel = new JLabel();

    public TimeControlTab(User user) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BankSystem system = BankSystem.getInstance();

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Simulated now: "));
        top.add(nowLabel);

        JButton plus1Day = new JButton("+1 day");
        JButton plus7Days = new JButton("+7 days");
        JButton plus1Month = new JButton("+1 month");
        JButton plus3Months = new JButton("+3 months");
        JButton setExact = new JButton("Set exact datetime...");
        JButton reset = new JButton("Reset to real time");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(plus1Day);
        buttons.add(plus7Days);
        buttons.add(plus1Month);
        buttons.add(plus3Months);
        buttons.add(setExact);
        buttons.add(reset);

        add(top, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);

        Runnable refresh = () -> nowLabel.setText(system.getSimulatedNow().toString());
        refresh.run();

        // Safety: only admin should use this tab
        if (!user.canAdvanceTime()) {
            JOptionPane.showMessageDialog(this, "Access denied.", "Error", JOptionPane.ERROR_MESSAGE);
            setEnabled(false);
            return;
        }

        plus1Day.addActionListener(e -> {
            system.advanceDays(1);
            system.saveAllData();
            refresh.run();
        });

        plus7Days.addActionListener(e -> {
            system.advanceDays(7);
            system.saveAllData();
            refresh.run();
        });

        plus1Month.addActionListener(e -> {
            system.advanceMonths(1);
            system.saveAllData();
            refresh.run();
        });

        plus3Months.addActionListener(e -> {
            system.advanceMonths(3);
            system.saveAllData();
            refresh.run();
        });

        setExact.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(
                    this,
                    "Enter datetime ISO (e.g. 2026-01-28T10:30:00)",
                    system.getSimulatedNow().toString()
            );
            if (s == null) return;
            try {
                LocalDateTime target = LocalDateTime.parse(s.trim());
                system.setSimulatedTo(target);
                system.saveAllData();
                refresh.run();
            } catch (Exception ex) {
                FileLogger.getInstance().log(types.LogLevel.ERROR, types.LogCategory.SYSTEM,
                        "[GUI] Invalid datetime input: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Invalid datetime format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reset.addActionListener(e -> {
            system.resetToRealTime();
            system.saveAllData();
            refresh.run();
        });
    }
}
