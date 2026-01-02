package backend;

import backend.transactions.Transaction;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel {

    private final double[] monthlyData = new double[12];
    private final YearMonth[] last12Months = new YearMonth[12];

    public ChartPanel() {
        calculateLast12Months();
    }

    /* Υπολογισμός τελευταίων 12 μηνών */
    private void calculateLast12Months() {
        YearMonth current = YearMonth.now();
        for (int i = 11; i >= 0; i--) {
            last12Months[i] = current;
            current = current.minusMonths(1);
        }
    }

    /* Φόρτωση δεδομένων */
    public void setData(List<Transaction> transactions) {
        Arrays.fill(monthlyData, 0.0);
        calculateLast12Months();

        for (Transaction t : transactions) {
            LocalDateTime ldt = t.getTimestamp();
            YearMonth txMonth = YearMonth.from(ldt);

            for (int i = 0; i < 12; i++) {
                if (last12Months[i].equals(txMonth)) {
                    // Όλες οι συναλλαγές αυξάνουν
                    monthlyData[i] += Math.abs(t.getAmount());
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Rendering quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int padding = 30;
        int labelPadding = 40;
        int width = getWidth();
        int height = getHeight();
        int graphWidth = width - (2 * padding) - labelPadding;
        int graphHeight = height - (2 * padding) - 50;

        double maxValue = Arrays.stream(monthlyData).max().orElse(100.0);
        if (maxValue == 0) maxValue = 100;

        /* Grid lines */
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i <= 4; i++) {
            int y = height - padding - 50 - (i * graphHeight / 4);
            g2.setColor(new Color(240, 240, 240));
            g2.drawLine(padding + labelPadding, y, width - padding, y);

            g2.setColor(new Color(150, 150, 150));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.drawString(String.format("%.0f", (maxValue / 4) * i), 5, y + 4);
        }

        /* Υπολογισμός σημείων */
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int x = (int) (i * ((double) graphWidth / 11) + padding + labelPadding);
            int y = (int) ((maxValue - monthlyData[i]) * graphHeight / maxValue + padding);
            points.add(new Point(x, y));
        }

        /* Gradient fill */
        Polygon area = new Polygon();
        area.addPoint(points.get(0).x, height - padding - 50);
        for (Point p : points) area.addPoint(p.x, p.y);
        area.addPoint(points.get(11).x, height - padding - 50);

        g2.setPaint(new GradientPaint(
                0, padding, new Color(0, 120, 255, 40),
                0, height - 50, new Color(255, 255, 255, 0))
        );
        g2.fill(area);

        /* Main line */
        g2.setColor(new Color(0, 120, 255));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < points.size() - 1; i++) {
            g2.drawLine(points.get(i).x, points.get(i).y,
                        points.get(i + 1).x, points.get(i + 1).y);
        }

        /* Points & Month labels */
        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
        for (int i = 0; i < 12; i++) {

            // Dot
            g2.setColor(new Color(0, 120, 255));
            g2.fillOval(points.get(i).x - 3, points.get(i).y - 3, 6, 6);
            g2.setColor(Color.WHITE);
            g2.fillOval(points.get(i).x - 1, points.get(i).y - 1, 2, 2);

            // Month label
            String label = last12Months[i].getMonth()
                    .toString().substring(0, 3);
            label = label.charAt(0) + label.substring(1).toLowerCase();

            g2.setColor(new Color(80, 80, 80));
            Graphics2D g2rotated = (Graphics2D) g2.create();
            g2rotated.translate(points.get(i).x, height - padding - 35);
            g2rotated.rotate(Math.toRadians(45));
            g2rotated.drawString(label, 0, 0);
            g2rotated.dispose();
        }
    }
}
