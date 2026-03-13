package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_14 {

    public static long sumOfNaturalNumbers(int n) {
        if (n < 1) {
            System.out.println("Error: n must be ≥ 1");
            return -1;
        }
        return (long) n * (n + 1) / 2;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter N to find the sum of the first N natural numbers: ");

        if (input.hasNextInt()) {
            int n = input.nextInt();
            long totalSum = sumOfNaturalNumbers(n);

            if (totalSum != -1) {
                System.out.println(STR."The sum of the first \{n} natural numbers is: \{totalSum}");
            }
        } else {
            System.out.println("Invalid input. Please enter a positive integer.");
        }

        input.close();
    }
}