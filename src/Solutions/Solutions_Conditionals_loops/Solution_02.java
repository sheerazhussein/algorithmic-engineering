package Solutions.Solutions_Conditionals_loops;

import java.util.Scanner;

/**
 * Solution_02: Calculates the area of a triangle.
 */
public class Solution_02 {

    public static double areaOfTriangle(double base, double height) {
        if (base < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensions cannot be negative");
        }
        return 0.5 * base * height;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter base: ");
        double b = input.nextDouble();

        System.out.print("Enter height: ");
        double h = input.nextDouble();

        double area = areaOfTriangle(b, h);

        System.out.println("Area of triangle is: " + area);

        input.close();
    }
}
