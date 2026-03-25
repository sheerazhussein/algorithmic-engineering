package Solutions.ArraysSolutions;

public class Floor {
    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 6, 7, 8, 9, 11, 23};
        int target = 10;

        int result = floorIndex(arr, target);

        if (result == -1) {
            System.out.println("No floor found!");
        } else {
            System.out.println("Floor of " + target + " is: " + arr[result]);
        }
    }

    static int floorIndex(int[] arr, int target) {

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
        return end;
    }
}