package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_09 {

    public static long factorial(int n) {
        if (n < 0) {
            System.out.println("Error: Factorial undefined for negative numbers.");
            return -1;
        }
        if (n == 0 || n == 1) return 1;

        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a number to find its factorial: ");

        if (input.hasNextInt()) {
            int num = input.nextInt();
            long result = factorial(num);

            if (result != -1) {
                System.out.println(num + "! = " + result);
            }
        } else {
            System.out.println("Invalid input. Please enter an integer.");
        }

        input.close();
    }
}
