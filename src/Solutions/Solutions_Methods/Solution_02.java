package Solutions.Solutions_Methods;

public class Solution_02 {

    public static String evenOrOdd(int n) {
        return (n % 2 == 0) ? "Even" : "Odd";
    }

    public static void main(String[] args) {
        System.out.println(evenOrOdd(10));
        System.out.println(evenOrOdd(7));
    }
}
