package Solutions_Conditionals_loops;

import java.util.Scanner;

/**
 * ──────────────────────────────────────────────────────────
 * A06. AREA OF RHOMBUS
 * ──────────────────────────────────────────────────────────
 * FIRST PRINCIPLE:
 *   A rhombus is composed of 4 right triangles.
 *   The legs of these triangles are half of each diagonal: (d1/2) and (d2/2).
 *   Area of one triangle = (1/2) * (d1/2) * (d2/2) = (d1 * d2) / 8.
 *   Total Area = 4 * ((d1 * d2) / 8) = (d1 * d2) / 2.
 *
 * FORMULA: A = (d1 × d2) / 2
 * ──────────────────────────────────────────────────────────
 */
public class Solution_06 {

    public static double getArea(double d1, double d2) {
        // Validation: Diagonals must be non-negative
        if (d1 < 0 || d2 < 0) {
            throw new IllegalArgumentException("Diagonals cannot be negative");
        }

        return (d1 * d2) / 2.0;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            System.out.print("Enter diagonal 1 (d1): ");
            double d1 = in.nextDouble();

            System.out.print("Enter diagonal 2 (d2): ");
            double d2 = in.nextDouble();

            double area = getArea(d1, d2);

            System.out.printf("The Area of the Rhombus is: %.2f\n", area);

        } catch (IllegalArgumentException e) {
            System.out.println("Math Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input Error: Please enter numeric values only.");
        } finally {
            in.close();
        }
    }
}
