import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            int rows = 0;
            int cols = 0;

            while (true) {
                try {
                    System.out.print("Enter number of rows: ");
                    rows = sc.nextInt();
                    if (rows <= 0) {
                        System.out.println("Rows must be a positive integer.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a positive integer.");
                    sc.next();
                }
            }

            while (true) {
                try {
                    System.out.print("Enter number of columns: ");
                    cols = sc.nextInt();
                    if (cols <= 0) {
                        System.out.println("Columns must be a positive integer.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a positive integer.");
                    sc.next();
                }
            }

            // Pop up the window immediately
            PathVisualizer visualizer = new PathVisualizer(rows, cols);
            visualizer.setStatus("Setting up grid layout...", java.awt.Color.BLACK);

            Grid grid = new Grid(rows, cols);
            grid.inputGrid(sc, visualizer);
            grid.displayGrid();

            RobotNavigator navigator = new RobotNavigator(grid, visualizer);
            navigator.navigate();
            
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
