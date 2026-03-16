import java.util.ArrayList;
import java.util.List;

public class PathCalculator {

    int[][] grid;
    int[][] costs;
    int rows;
    int cols;
    private static final int INF = 1000000;

    public PathCalculator(int[][] grid, int[][] costs, int rows, int cols) {
        this.grid = grid;
        this.costs = costs;
        this.rows = rows;
        this.cols = cols;
    }

    public long countPaths() {
        if (rows == 0 || cols == 0) return 0;
        if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1)
            return 0;

        long[][] dp = new long[rows][cols];
        dp[0][0] = 1;

        for (int i = 1; i < rows; i++)
            dp[i][0] = (grid[i][0] == 0) ? dp[i - 1][0] : 0;

        for (int j = 1; j < cols; j++)
            dp[0][j] = (grid[0][j] == 0) ? dp[0][j - 1] : 0;

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (grid[i][j] == 1)
                    dp[i][j] = 0;
                else
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[rows - 1][cols - 1];
    }

    public int minCostPath() {
        if (rows == 0 || cols == 0) return 0;
        int[][] dpCost = getCostMatrix();
        return dpCost[rows - 1][cols - 1];
    }

    private int[][] getCostMatrix() {
        int[][] dpCost = new int[rows][cols];
        dpCost[0][0] = costs[0][0];

        for (int i = 1; i < rows; i++)
            dpCost[i][0] = (costs[i][0] >= INF || dpCost[i-1][0] >= INF) ? INF : dpCost[i - 1][0] + costs[i][0];

        for (int j = 1; j < cols; j++)
            dpCost[0][j] = (costs[0][j] >= INF || dpCost[0][j-1] >= INF) ? INF : dpCost[0][j - 1] + costs[0][j];

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (grid[i][j] == 1) {
                    dpCost[i][j] = INF;
                } else {
                    int minPrev = Math.min(dpCost[i - 1][j], dpCost[i][j - 1]);
                    dpCost[i][j] = (minPrev >= INF) ? INF : minPrev + costs[i][j];
                }
            }
        }
        return dpCost;
    }

    public List<List<int[]>> findAllPaths() {
        List<List<int[]>> allPaths = new ArrayList<>();
        if (rows > 0 && cols > 0) {
            findPathsRecursive(0, 0, new ArrayList<>(), allPaths);
        }
        return allPaths;
    }

    public boolean[][] getPathMarkers() {
        boolean[][] path = new boolean[rows][cols];
        int[][] dpCost = getCostMatrix();
        
        if (dpCost[rows - 1][cols - 1] >= INF) return path;

        int r = rows - 1;
        int c = cols - 1;
        while (r >= 0 && c >= 0) {
            path[r][c] = true;
            if (r == 0 && c == 0) break;
            
            if (r > 0 && c > 0) {
                if (dpCost[r - 1][c] <= dpCost[r][c - 1]) r--;
                else c--;
            } else if (r > 0) {
                r--;
            } else {
                c--;
            }
        }
        return path;
    }

    private void findPathsRecursive(int r, int c, List<int[]> currentPath, List<List<int[]>> allPaths) {
        currentPath.add(new int[]{r, c});

        // Check if current cell is an obstacle
        if (grid[r][c] == 1) {
            allPaths.add(new ArrayList<>(currentPath));
            currentPath.remove(currentPath.size() - 1);
            return;
        }

        // Check if reached destination
        if (r == rows - 1 && c == cols - 1) {
            allPaths.add(new ArrayList<>(currentPath));
            currentPath.remove(currentPath.size() - 1);
            return;
        }

        // Move Right
        if (c + 1 < cols) {
            findPathsRecursive(r, c + 1, currentPath, allPaths);
        }

        // Move Down
        if (r + 1 < rows) {
            findPathsRecursive(r + 1, c, currentPath, allPaths);
        }

        currentPath.remove(currentPath.size() - 1);
    }
}
