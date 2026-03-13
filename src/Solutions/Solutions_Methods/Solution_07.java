package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_07 {

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter an integer to check if it's prime: ");

        if (input.hasNextInt()) {
            int number = input.nextInt();

            if (isPrime(number)) {
                System.out.println(number + " is a Prime number.");
            } else {
                System.out.println(number + " is NOT a Prime number.");
            }
        } else {
            System.out.println("Invalid input. Please enter a whole number.");
        }

        input.close();
    }
}
