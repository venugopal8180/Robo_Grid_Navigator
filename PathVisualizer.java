import javax.swing.*;
import java.awt.*;

public class PathVisualizer extends JFrame {
    private int rows, cols;
    private JLabel[][] cells;
    private JLabel statusLabel;
    private JPanel gridPanel;

    public PathVisualizer(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new JLabel[rows][cols];

        setTitle("PathMatrix");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(rows, cols, 2, 2));
        gridPanel.setBackground(Color.DARK_GRAY);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new JLabel("", SwingConstants.CENTER);
                cells[i][j].setOpaque(true);
                cells[i][j].setBackground(Color.WHITE);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 18));
                cells[i][j].setPreferredSize(new Dimension(50, 50));
                cells[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                gridPanel.add(cells[i][j]);
            }
        }

        statusLabel = new JLabel("Starting path exploration...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusLabel.setForeground(Color.BLUE);

        add(gridPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        requestFocus();
        
        // Brief delay to ensure OS displays window
        try { Thread.sleep(200); } catch (InterruptedException e) {}
        setAlwaysOnTop(false);
    }

    public void setStatus(String message, Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(message);
            statusLabel.setForeground(color);
        });
    }

    public void updateCell(int r, int c, char symbol) {
        SwingUtilities.invokeLater(() -> {
            cells[r][c].setText(String.valueOf(symbol));
            switch (symbol) {
                case 'S': cells[r][c].setBackground(Color.CYAN); break;
                case 'D': cells[r][c].setBackground(Color.GREEN); break;
                case '*': cells[r][c].setBackground(Color.YELLOW); break;
                case 'X': cells[r][c].setBackground(Color.RED); break;
                case '0': cells[r][c].setBackground(Color.WHITE); break;
            }
        });
    }

    public void resetGrid(int[][] gridLayout) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char c = (gridLayout[i][j] == 1) ? '1' : '0';
                    cells[i][j].setText(String.valueOf(c));
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        });
    }
}
