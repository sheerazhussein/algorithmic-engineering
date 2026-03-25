package Solutions.ArraysSolutions;

public class FirstandLast {
    public static void main(String[] args) {
        int[] arr = {2, 4, 6, 6, 6, 8, 9, 11, 23};
        int target = 6;

        int first = firstPosition(arr, target);
        int last = lastPosition(arr, target);

        if (first == -1) {
            System.out.println("Target " + target + " not found!");
        } else {
            System.out.println("First position: " + first);
            System.out.println("Last position: " + last);
        }
    }

    static int firstPosition(int[] arr, int target) {
        if (target > arr[arr.length - 1]) return -1;

        int start = 0;
        int end = arr.length - 1;
        int result = -1;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (arr[mid] == target) {
                result = mid;   // ✅ save the position
                end = mid - 1; // ✅ keep searching LEFT for first occurrence
            } else if (arr[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return result;
    }

    static int lastPosition(int[] arr, int target) {
        if (target > arr[arr.length - 1]) return -1;

        int start = 0;
        int end = arr.length - 1;
        int result = -1;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (arr[mid] == target) {
                result = mid;    // ✅ save the position
                start = mid + 1; // ✅ keep searching RIGHT for last occurrence
            } else if (arr[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return result;
    }
}