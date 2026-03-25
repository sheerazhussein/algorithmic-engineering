package Solutions.ArraysSolutions;

public class SmallestPosition {

    //Q3 : Smallest Letter
    //Given a sorted array of letters and a target letter, find the smallest letter greater than the target.

    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 6, 7, 8, 9, 11, 23};
        int target = 10;

        int result = smallestPositionIndex(arr, target);

        if (result == - 1) {
            System.out.println("No smallestPositionIndex found!");
        } else {
            System.out.println("SmallestPositionIndex of " + target + " is: " + arr[result]);
        }
    }

    static int smallestPositionIndex(int[] arr, int target) {

        if (target > arr[arr.length - 1]) return -1;

        int start = 0;
        int end = arr.length - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return start % arr.length;
    }
}
