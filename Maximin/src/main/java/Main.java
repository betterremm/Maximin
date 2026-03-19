import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Максимин + K-Means");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 900);
            frame.setLayout(new BorderLayout());

            DrawPanel panel = new DrawPanel();
            JButton kMeansButton = new JButton("Запустить K-Means");
            kMeansButton.setEnabled(false);

            frame.add(panel, BorderLayout.CENTER);
            frame.add(kMeansButton, BorderLayout.SOUTH);
            frame.setVisible(true);

            MaximinAlgorithm algorithm =
                    new MaximinAlgorithm(50000, panel, kMeansButton);
                    //КОЛИЧЕСТВО ТОЧЕК
            new Thread(() -> {
                try {
                    algorithm.runMaximin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            kMeansButton.addActionListener(e -> {
                new Thread(() -> {
                    try {
                        algorithm.runKMeans();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            });
        });
    }
}