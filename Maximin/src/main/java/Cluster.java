import java.util.ArrayList;
import java.util.List;

public class Cluster {

    public Point core;
    public List<Point> points;

    public Cluster(Point core) {
        this.core = core;
        this.points = new ArrayList<>();
    }

    public void clearPoints() {
        points.clear();
    }

    public void addPoint(Point p) {
        points.add(p);
    }
}