package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_13 {

    public static boolean isPrime(int n) {
        return Solution_07.isPrime(n);
    }
    public static void printPrimesInRange(int start, int end) {
        if (start > end) {
            System.out.println("Error: start must be ≤ end");
            return;
        }

        StringBuilder result = new StringBuilder("Primes between " + start + " and " + end + ": ");
        boolean foundAny = false;
        int from = Math.max(2, start);

        for (int i = from; i <= end; i++) {
            if (isPrime(i)) {
                result.append(i).append(" ");
                foundAny = true;
            }
        }

        if (!foundAny) result.append("none");
        System.out.println(result.toString().trim());
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter start number: ");
        int start = input.nextInt();
        System.out.print("Enter end number: ");
        int end = input.nextInt();

        printPrimesInRange(start, end);

        input.close();
    }
}
