package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_08 {

    public static String getGrade(int marks) {
        if (marks < 0 || marks > 100) {
            return "Invalid marks (must be 0-100)";
        }

        if (marks >= 91) return "A1";
        else if (marks >= 81) return "A+";
        else if (marks >= 71) return "A";
        else if (marks >= 61) return "B";
        else if (marks >= 51) return "C";
        else if (marks >= 41) return "D";
        else return "Fail";
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your marks (0-100): ");

        if (input.hasNextInt()) {
            int userMarks = input.nextInt();

            String grade = getGrade(userMarks);
            System.out.println("Result: " + grade);
        } else {
            System.out.println("Error: Please enter a whole number.");
        }

        input.close();
    }
}
