package Solutions.Solutions_Methods;

public class Solution_03 {

    public static boolean isEligibleToVote(int age) {
        return age >= 18;
    }

    public static void main(String[] args) {
        System.out.println("Is 10 eligible? " + isEligibleToVote(10));
        System.out.println("Is 21 eligible? " + isEligibleToVote(21));
    }

}
