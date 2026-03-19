import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MaximinAlgorithm {

    private List<Point> data;
    private List<Cluster> clusters;
    private Random random;
    private DrawPanel panel;
    private JButton button;
    private final int THREAD_SLEEP_TIME = 200;

    public MaximinAlgorithm(int numberOfPoints,
                            DrawPanel panel,
                            JButton button) {

        this.panel = panel;
        this.button = button;
        random = new Random();
        data = new ArrayList<>();
        clusters = new ArrayList<>();
        generateData(numberOfPoints);
    }

    private void generateData(int count) {
        for (int i = 0; i < count; i++) {
            data.add(new Point(
                    random.nextDouble() * 100,
                    random.nextDouble() * 100
            ));
        }
    }

    // =========================
    //        MAXIMIN
    // =========================
    public void runMaximin() throws InterruptedException {

        Point firstCore = data.get(random.nextInt(data.size()));
        clusters.add(new Cluster(firstCore));
        updateView(null);
        Thread.sleep(THREAD_SLEEP_TIME);

        Point secondCore = null;
        double maxDist = 0;

        for (Point p : data) {
            double d = p.distance(firstCore);
            if (d > maxDist) {
                maxDist = d;
                secondCore = p;
            }
        }

        clusters.add(new Cluster(secondCore));
        updateView(null);
        Thread.sleep(THREAD_SLEEP_TIME);

        boolean continueAlgorithm = true;

        while (continueAlgorithm) {

            distributePoints();
            updateView(null);
            Thread.sleep(THREAD_SLEEP_TIME);

            Point candidate = null;
            double deltaMax = 0;

            for (Cluster c : clusters) {
                for (Point p : c.points) {
                    double d = p.distance(c.core);
                    if (d > deltaMax) {
                        deltaMax = d;
                        candidate = p;
                    }
                }
            }

            updateView(candidate);
            Thread.sleep(THREAD_SLEEP_TIME);

            double avg = averageDistanceBetweenCores();

            if (avg == 0) break;

            if (deltaMax > avg / 2.0) {
                clusters.add(new Cluster(candidate));
                updateView(null);
                Thread.sleep(THREAD_SLEEP_TIME);
            } else {
                continueAlgorithm = false;
            }
        }

        System.out.println("Максимин завершён. Классов: " + clusters.size());

        SwingUtilities.invokeLater(() -> button.setEnabled(true));
    }

    // =========================
    //          K-MEANS
    // =========================
    public void runKMeans() throws InterruptedException {

        System.out.println("Запуск K-Means...");

        boolean changed = true;

        while (changed) {

            distributePoints();
            updateView(null);
            Thread.sleep(THREAD_SLEEP_TIME);

            changed = false;

            for (Cluster c : clusters) {

                double sumX = 0;
                double sumY = 0;

                for (Point p : c.points) {
                    sumX += p.x;
                    sumY += p.y;
                }

                if (c.points.size() == 0) continue;

                Point newCore = new Point(
                        sumX / c.points.size(),
                        sumY / c.points.size()
                );

                if (c.core.distance(newCore) > 0.01) {
                    c.core = newCore;
                    changed = true;
                }
            }

            updateView(null);
            Thread.sleep(THREAD_SLEEP_TIME);
        }

        System.out.println("K-Means завершён.");
    }

    // =========================

    private void distributePoints() {

        for (Cluster c : clusters)
            c.clearPoints();

        for (Point p : data) {

            Cluster nearest = clusters.get(0);
            double minDist = p.distance(nearest.core);

            for (Cluster c : clusters) {
                double d = p.distance(c.core);
                if (d < minDist) {
                    minDist = d;
                    nearest = c;
                }
            }

            nearest.addPoint(p);
        }
    }

    private double averageDistanceBetweenCores() {

        double sum = 0;
        int count = 0;

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                sum += clusters.get(i).core
                        .distance(clusters.get(j).core);
                count++;
            }
        }

        if (count == 0) return 0;
        return sum / count;
    }

    private void updateView(Point candidate) {
        panel.setClusters(clusters);
        panel.setCandidate(candidate);
        panel.repaint();
    }
}