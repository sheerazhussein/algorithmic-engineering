package Solutions.conditionals_loops;

import java.util.Scanner;

/**
 * ──────────────────────────────────────────────────────────
 * A08. PERIMETER OF CIRCLE (Circumference)
 * ──────────────────────────────────────────────────────────
 * FIRST PRINCIPLE:
 *   The perimeter of a circle is called the Circumference (C).
 *   The constant π (Pi) is the ratio of C to the diameter (d).
 *   Since d = 2 * r, the relationship is:
 *     C = π * d  =>  C = 2 * π * r.
 *
 *   Historical note: Archimedes approximated π by fitting
 *   polygons inside circles to measure their outer boundaries.
 *
 * FORMULA: C = 2 × π × r
 * ──────────────────────────────────────────────────────────
 */
public class Solution_08 {

    public static double getPerimeter(double radius) {
        // Validation: A circle cannot have a negative radius
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative");
        }

        return 2 * Math.PI * radius;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            System.out.print("Enter the radius (r) of the circle: ");
            double r = in.nextDouble();

            double circumference = getPerimeter(r);

            System.out.printf("The Circumference (Perimeter) of the Circle is: %.4f\n", circumference);

        } catch (IllegalArgumentException e) {
            System.out.println("Math Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input Error: Please enter a valid numerical radius.");
        } finally {
            in.close();
        }
    }
}
