package Solutions.Solutions_Conditionals_loops;

import java.util.Scanner;

/**
 * ──────────────────────────────────────────────────────────
 * A05. AREA OF PARALLELOGRAM
 * ──────────────────────────────────────────────────────────
 * FIRST PRINCIPLE:
 *   A parallelogram is essentially a "sheared" rectangle.
 *   By moving a triangular section from one side to the other,
 *   it forms a rectangle with the same base and height.
 *
 *   Key insight: Area is preserved during shearing.
 *   Only the PERPENDICULAR height matters.
 *
 * FORMULA: A = base × height
 * ──────────────────────────────────────────────────────────
 */
public class Solution_05 {

    public static double getArea(double base, double height) {
        if (base < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensions cannot be negative");
        }

        return base * height;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            System.out.print("Enter the base of the parallelogram: ");
            double base = in.nextDouble();

            System.out.print("Enter the perpendicular height: ");
            double height = in.nextDouble();

            double area = getArea(base, height);

            System.out.printf("The Area of the Parallelogram is: %.2f\n", area);

        } catch (IllegalArgumentException e) {
            System.out.println("Math Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input Error: Please enter numeric values (e.g., 10.5).");
        } finally {
            in.close();
        }
    }
}
