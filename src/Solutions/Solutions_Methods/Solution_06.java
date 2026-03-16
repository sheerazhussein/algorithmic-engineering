package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_06 {

    public static void circleInfo(double radius) {
        if (radius < 0) {
            System.out.println("Error: radius cannot be negative");
            return;
        }
        double area = Math.PI * Math.pow(radius, 2);
        double circumference = 2 * Math.PI * radius;

        System.out.printf("Radius:        %.4f%n", radius);
        System.out.printf("Area:          %.4f%n", area);
        System.out.printf("Circumference: %.4f%n", circumference);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter the circle radius: ");
        double radius = input.nextDouble();

        circleInfo(radius);

        input.close();
    }
}
