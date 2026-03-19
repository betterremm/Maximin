import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class DrawPanel extends JPanel {

    private List<Cluster> clusters;
    private Point candidate;
    private Random random = new Random();

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public void setCandidate(Point candidate) {
        this.candidate = candidate;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (clusters == null) return;

        int width = getWidth();
        int height = getHeight();

        for (Cluster cluster : clusters) {

            Color color = new Color(
                    random.nextInt(200),
                    random.nextInt(200),
                    random.nextInt(200)
            );

            g.setColor(color);

            for (Point p : cluster.points) {
                int x = (int)(p.x * width / 100);
                int y = (int)(p.y * height / 100);
                g.fillOval(x, y, 4, 4);
            }

            // ядро
            g.setColor(Color.BLACK);
            int coreX = (int)(cluster.core.x * width / 100);
            int coreY = (int)(cluster.core.y * height / 100);
            g.fillOval(coreX - 6, coreY - 6, 12, 12);
        }

        // кандидат
        if (candidate != null) {
            g.setColor(Color.RED);
            int x = (int)(candidate.x * width / 100);
            int y = (int)(candidate.y * height / 100);
            g.fillOval(x - 8, y - 8, 16, 16);
        }
    }
}