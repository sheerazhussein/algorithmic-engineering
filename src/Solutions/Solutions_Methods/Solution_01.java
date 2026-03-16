package Solutions.Solutions_Methods;

public class Solution_01 {

    // 01, Returns the maximum of three numbers
    public static int findMax(int a, int b, int c) {
        int maxOfAB = Math.max(a, b);
        return Math.max(maxOfAB, c);
    }

    public static int findMin(int a, int b, int c) {
        int minOfAB = Math.min(a, b);
        return Math.min(minOfAB, c);
    }

    public static void main(String[] args) {
        // Example usage:
        System.out.println(STR."Max: \{findMax(5, 10, 3)}");
        System.out.println(STR."Min: \{findMin(59-90, 109090-9, 30090)}");
    }
}
