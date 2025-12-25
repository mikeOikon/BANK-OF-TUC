package backend;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDateTime;

import javax.swing.JPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import backend.transactions.Transaction;

public class ChartPanel extends JPanel {
    private double[] monthlyData = new double[12];
    private final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public void setData(List<Transaction> transactions) {
        Arrays.fill(monthlyData, 0.0);
        
        for (Transaction t : transactions) {
            // Αν το timestamp σου είναι LocalDateTime
            LocalDateTime ldt = t.getTimestamp(); 
            
            // Παίρνουμε τον μήνα (1-12) και αφαιρούμε 1 για να έχουμε index 0-11
            int monthIndex = ldt.getMonthValue() - 1;
            
            // Καταγράφουμε έσοδα (Amount > 0)
            if (t.getAmount() > 0) {
                monthlyData[monthIndex] += t.getAmount();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Μέγιστη ποιότητα σχεδίασης
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Καθαρό Λευκό Φόντο
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int padding = 30;
        int labelPadding = 40;
        int width = getWidth();
        int height = getHeight();
        int graphWidth = width - (2 * padding) - labelPadding;
        int graphHeight = height - (2 * padding) - 50; // Χώρος για διαγώνια labels

        double maxIncome = Arrays.stream(monthlyData).max().orElse(100.0);
        if (maxIncome == 0) maxIncome = 100;

        // 1. Διακριτικές Γραμμές Πλέγματος (Soft Gray)
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i <= 4; i++) {
            int y = height - padding - 50 - (i * graphHeight / 4);
            g2.setColor(new Color(240, 240, 240)); 
            g2.drawLine(padding + labelPadding, y, width - padding, y);
            
            g2.setColor(new Color(150, 150, 150));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.drawString(String.format("%.0f", (maxIncome / 4) * i), 5, y + 4);
        }

        // 2. Υπολογισμός Σημείων
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int x = (int) (i * ((double) graphWidth / 11) + padding + labelPadding);
            int y = (int) ((maxIncome - monthlyData[i]) * ((double) graphHeight / maxIncome) + padding);
            points.add(new Point(x, y));
        }

        // 3. Απαλό Gradient Fill (Light Blue Glow)
        Polygon area = new Polygon();
        area.addPoint(points.get(0).x, height - padding - 50);
        for (Point p : points) area.addPoint(p.x, p.y);
        area.addPoint(points.get(points.size() - 1).x, height - padding - 50);

        // Διαβάθμιση από απαλό μπλε σε τελείως διάφανο λευκό
        g2.setPaint(new GradientPaint(0, padding, new Color(0, 120, 255, 40), 0, height - 50, new Color(255, 255, 255, 0)));
        g2.fill(area);

        // 4. Κύρια "Electric Blue" Γραμμή
        g2.setColor(new Color(0, 120, 255)); 
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < points.size() - 1; i++) {
            g2.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }

        // 5. Διαγώνια Labels & Σημεία
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        for (int i = 0; i < 12; i++) {
            // Μικροί κύκλοι στα σημεία (Points)
            g2.setColor(new Color(0, 120, 255));
            g2.fillOval(points.get(i).x - 3, points.get(i).y - 3, 6, 6);
            g2.setColor(Color.WHITE);
            g2.fillOval(points.get(i).x - 1, points.get(i).y - 1, 2, 2); // Λευκό "κέντρο" στο dot

            // Διαγώνιο κείμενο για τους μήνες
            g2.setColor(new Color(80, 80, 80));
            Graphics2D g2rotated = (Graphics2D) g2.create();
            g2rotated.translate(points.get(i).x, height - padding - 35);
            g2rotated.rotate(Math.toRadians(45)); 
            g2rotated.drawString(months[i], 0, 0);
            g2rotated.dispose();
        }
    }
}