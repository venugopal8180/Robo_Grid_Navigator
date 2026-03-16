import java.util.Scanner;
import java.util.InputMismatchException;

public class Grid {

    int rows;
    int cols;
    int[][] grid; // 0 = free, 1 = obstacle
    int[][] costs;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new int[rows][cols];
        costs = new int[rows][cols];
    }

    public void inputGrid(Scanner sc, PathVisualizer visualizer) {
        System.out.println("Step 1: Define Grid Layout (0 = free, 1 = obstacle)");
        visualizer.setStatus("Define Grid Layout (0 = free, 1 = obstacle)", java.awt.Color.BLUE);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                while (true) {
                    try {
                        System.out.print("Grid[" + i + "][" + j + "] (0/1): ");
                        int val = sc.nextInt();
                        if (val != 0 && val != 1) {
                            System.out.println("Invalid value. Use 0 or 1.");
                            continue;
                        }
                        grid[i][j] = val;
                        visualizer.updateCell(i, j, (val == 1 ? '1' : '0'));
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Enter 0 or 1.");
                        sc.next();
                    }
                }
            }
        }

        System.out.println("\nStep 2: Define Costs for Free Cells (Obstacles '1' have infinite cost)");
        visualizer.setStatus("Define Costs for Free Cells (Obstacles = INF)", java.awt.Color.BLUE);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    while (true) {
                        try {
                            System.out.print("Cost for Grid[" + i + "][" + j + "]: ");
                            costs[i][j] = sc.nextInt();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Enter an integer cost.");
                            sc.next();
                        }
                    }
                } else {
                    costs[i][j] = 1000000; // INF for obstacles
                }
            }
        }
    }

    public void displayGrid() {
        System.out.println("\nGrid Layout (Obstacles marked as 1):");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
