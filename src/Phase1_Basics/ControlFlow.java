package src.Phase1_Basics;

public class ControlFlow {
    public static void main(String[] args) {
        int score = 85;

        if(score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 80) {
            System.out.println("Grade B");
        } else {
            System.out.println("Grade: C or lover");
        }
    }
}
