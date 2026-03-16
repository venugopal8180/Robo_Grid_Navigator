import java.util.List;
import java.awt.Color;

public class RobotNavigator {

    Grid grid;
    PathVisualizer visualizer;

    public RobotNavigator(Grid grid, PathVisualizer visualizer) {
        this.grid = grid;
        this.visualizer = visualizer;
    }

    public void navigate() {

        PathCalculator calculator =
                new PathCalculator(grid.grid, grid.costs, grid.rows, grid.cols);

        long totalPaths = calculator.countPaths();
        int minCost = calculator.minCostPath();
        List<List<int[]>> allPossiblePaths = calculator.findAllPaths();

        System.out.println("\n--- Starting Live Path Exploration ---");
        
        for (int i = 0; i < allPossiblePaths.size(); i++) {
            List<int[]> path = allPossiblePaths.get(i);
            
            // Reset for each path
            visualizer.resetGrid(grid.grid);
            visualizer.setStatus("Path " + (i + 1) + ": Starting from (0,0)...", Color.BLUE);
            sleep(300);

            boolean blocked = false;
            for (int step = 0; step < path.size(); step++) {
                int[] cell = path.get(step);
                int r = cell[0];
                int c = cell[1];

                char symbol = '*';
                if (step == 0) symbol = 'S';
                else if (step == path.size() - 1 && r == grid.rows - 1 && c == grid.cols - 1 && grid.grid[r][c] == 0) symbol = 'D';
                else if (grid.grid[r][c] == 1) symbol = 'X';

                visualizer.updateCell(r, c, symbol);

                if (grid.grid[r][c] == 1) {
                    visualizer.setStatus("Obstacle found! finding alternative path to reach the destination", Color.RED);
                    blocked = true;
                    sleep(1500); // Increased delay for obstacles
                    break;
                }

                sleep(600); // Increased delay for steps (from 200ms to 600ms)
            }

            if (!blocked) {
                int cost = calculatePathCost(path);
                visualizer.setStatus("Path " + (i + 1) + " found! (Total Cost: " + cost + ")", new Color(0, 153, 0));
                sleep(1500); // Increased delay for successful paths
            }
        }

        int optimumPathIndex = -1;
        for (int i = 0; i < allPossiblePaths.size(); i++) {
            List<int[]> path = allPossiblePaths.get(i);
            if (isPathSuccessful(path) && calculatePathCost(path) == minCost) {
                optimumPathIndex = i + 1;
                break;
            }
        }

        // Final optimum path display
        if (minCost < 1000000 && optimumPathIndex != -1) {
            visualizer.resetGrid(grid.grid);
            String finalMsg = "Path " + optimumPathIndex + " is the optimum path with least cost " + minCost + " and shortest path.";
            System.out.println("\n--- " + finalMsg + " ---");
            visualizer.setStatus(finalMsg, new Color(0, 102, 204));
            
            boolean[][] markers = calculator.getPathMarkers();
            for (int i = 0; i < grid.rows; i++) {
                for (int j = 0; j < grid.cols; j++) {
                    if (i == 0 && j == 0) visualizer.updateCell(i, j, 'S');
                    else if (i == grid.rows - 1 && j == grid.cols - 1) visualizer.updateCell(i, j, 'D');
                    else if (markers[i][j]) visualizer.updateCell(i, j, '*');
                    else if (grid.grid[i][j] == 1) visualizer.updateCell(i, j, 'X');
                }
            }
        } else {
            visualizer.setStatus("No path to destination found.", Color.RED);
        }
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }

    private int calculatePathCost(List<int[]> path) {
        int cost = 0;
        for (int[] cell : path) {
            cost += grid.costs[cell[0]][cell[1]];
        }
        return cost;
    }

    private boolean isPathSuccessful(List<int[]> path) {
        int[] lastCell = path.get(path.size() - 1);
        return lastCell[0] == grid.rows - 1 && lastCell[1] == grid.cols - 1 && grid.grid[lastCell[0]][lastCell[1]] == 0;
    }
}
