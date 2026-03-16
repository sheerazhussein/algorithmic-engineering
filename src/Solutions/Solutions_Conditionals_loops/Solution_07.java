package Solutions.Solutions_Conditionals_loops;

import java.util.Scanner;

/**
 * ──────────────────────────────────────────────────────────
 * A07. AREA OF EQUILATERAL TRIANGLE
 * ──────────────────────────────────────────────────────────
 * FIRST PRINCIPLE:
 *   All 3 sides (s) are equal, and all angles are 60°.
 *   The height (h) splits the base (s) into two halves (s/2).
 *   Using Pythagorean theorem or Trigonometry (sin 60°):
 *     h = s * (√3 / 2)
 *
 *   Area = (1/2) * base * height
 *   Area = (1/2) * s * (s * √3 / 2) = (√3 / 4) * s²
 *
 * FORMULA: A = (√3 / 4) × s²
 * ──────────────────────────────────────────────────────────
 */
public class Solution_07 {

    public static double getArea(double side) {
        // Validation: A triangle side cannot have a negative length
        if (side < 0) {
            throw new IllegalArgumentException("Side cannot be negative");
        }

        // Math.sqrt(3) provides the high-precision value of √3
        return (Math.sqrt(3) / 4.0) * (side * side);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            System.out.print("Enter the side length (s) of the equilateral triangle: ");
            double s = in.nextDouble();

            double area = getArea(s);

            System.out.printf("The Area of the Equilateral Triangle is: %.4f\n", area);

        } catch (IllegalArgumentException e) {
            System.out.println("Math Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input Error: Please enter a valid numerical side length.");
        } finally {
            in.close();
        }
    }
}
