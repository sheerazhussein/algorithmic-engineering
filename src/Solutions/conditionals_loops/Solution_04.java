package Solutions.conditionals_loops;

import java.util.Scanner;

public class Solution_04 {

    /**
     * A03. AREA OF RECTANGLE
     * Logic: Length (columns) multiplied by Width (rows).
     * Formula: A = length * width
     */
    public static double getArea(double length, double width) {
        if (length < 0 || width < 0) {
            throw new IllegalArgumentException("Dimensions cannot be negative");
        }
        return length * width;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Length: ");
        double l = input.nextDouble();

        System.out.print("Enter Width: ");
        double w = input.nextDouble();

        try {
            double area = getArea(l, w);
            System.out.println("Area of Rectangle is: " + area);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        input.close();
    }
}
