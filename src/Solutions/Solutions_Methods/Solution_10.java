package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_10 {

    public static boolean isPalindrome(int n) {
        if (n < 0 || (n % 10 == 0 && n != 0)) return false;

        int original = n;
        int reversed = 0;

        while (n > 0) {
            int digit = n % 10;
            reversed = reversed * 10 + digit;
            n /= 10;
        }

        return original == reversed;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a number to check if it's a palindrome: ");

        if (input.hasNextInt()) {
            int num = input.nextInt();
            if (isPalindrome(num)) {
                System.out.println(num + " is a palindrome! ");
            } else {
                System.out.println(num + " is NOT a palindrome.");
            }
        } else {
            System.out.println("Invalid input. Please enter an integer.");
        }

        input.close();
    }
}
