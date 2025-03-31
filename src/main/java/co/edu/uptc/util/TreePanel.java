package co.edu.uptc.util;

import co.edu.uptc.model.Document;
import co.edu.uptc.model.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class TreePanel extends JPanel implements MouseListener, MouseMotionListener {

    private Document document;
    private Map<Version, Point> nodePositions;
    private static final int NODE_WIDTH = 100;
    private static final int NODE_HEIGHT = 50;
    private static final int LEVEL_HEIGHT = 120;
    private static final int HORIZONTAL_SPACING = 150;

    private int lastX, lastY;
    private int offsetX = 0, offsetY = 0;

    public TreePanel() {
        setBackground(Color.WHITE);
        nodePositions = new HashMap<>();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setDocument(Document document) {
        this.document = document;
        calculatePositions();
    }

    private void calculatePositions() {
        if (document == null) {
            return;
        }

        nodePositions.clear();

        int xOffset = 50;
        for (Version mainVersion : document.getMainVersions()) {
            calculateSubtreePositions(mainVersion, 0, xOffset);
            xOffset += getSubtreeWidth(mainVersion) + HORIZONTAL_SPACING;
        }

        int maxX = 0;
        int maxY = 0;
        for (Point p : nodePositions.values()) {
            maxX = Math.max(maxX, p.x + NODE_WIDTH + 50);
            maxY = Math.max(maxY, p.y + NODE_HEIGHT + 50);
        }

        setPreferredSize(new Dimension(maxX, maxY));
        revalidate();
        repaint();
    }

    private void calculateSubtreePositions(Version version, int level, int xOffset) {
        if (version == null) {
            return;
        }

        int yPosition = level * LEVEL_HEIGHT + 50;
        nodePositions.put(version, new Point(xOffset, yPosition));

        if (version.getSubversions().isEmpty()) {
            return;
        }

        int childrenWidth = getSubtreeWidth(version) - NODE_WIDTH;
        int childXOffset = xOffset - childrenWidth / 2;

        for (Version child : version.getSubversions()) {
            int childWidth = getSubtreeWidth(child);
            childXOffset += childWidth / 2;
            calculateSubtreePositions(child, level + 1, childXOffset);
            childXOffset += childWidth / 2 + HORIZONTAL_SPACING / 2;
        }
    }

    private int getSubtreeWidth(Version version) {
        if (version == null || version.getSubversions().isEmpty()) {
            return NODE_WIDTH;
        }

        int width = 0;
        for (Version child : version.getSubversions()) {
            width += getSubtreeWidth(child) + HORIZONTAL_SPACING / 2;
        }

        return Math.max(width, NODE_WIDTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (document == null || nodePositions.isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Version version : nodePositions.keySet()) {
            Point p = nodePositions.get(version);
            int x = p.x + offsetX;
            int y = p.y + offsetY;

            for (Version child : version.getSubversions()) {
                Point childPoint = nodePositions.get(child);
                if (childPoint != null) {
                    int childX = childPoint.x + offsetX;
                    int childY = childPoint.y + offsetY;

                    g2d.setColor(Color.GRAY);
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.drawLine(x + NODE_WIDTH / 2, y + NODE_HEIGHT, childX + NODE_WIDTH / 2, childY);
                }
            }
        }

        for (Version version : nodePositions.keySet()) {
            Point p = nodePositions.get(version);
            int x = p.x + offsetX;
            int y = p.y + offsetY;

            if (version.isMainVersion()) {
                g2d.setColor(new Color(5, 102, 199));
            } else {
                g2d.setColor(new Color(153, 204, 255));
            }

            g2d.fillRoundRect(x, y, NODE_WIDTH, NODE_HEIGHT, 10, 10);

            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(x, y, NODE_WIDTH, NODE_HEIGHT, 10, 10);

            g2d.setColor(Color.BLACK);
            FontMetrics fm = g2d.getFontMetrics();
            String text = version.getId();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, x + (NODE_WIDTH - textWidth) / 2, y + NODE_HEIGHT / 2);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        offsetX += dx;
        offsetY += dy;
        lastX = e.getX();
        lastY = e.getY();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

}
