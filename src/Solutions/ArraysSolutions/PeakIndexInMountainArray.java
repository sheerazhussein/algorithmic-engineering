package Solutions.ArraysSolutions;

public class PeakIndexInMountainArray {

    public static void main(String[] args) {
        int[] arr = {0, 1, 3, 4, 5, 6, 8, 4, 2, 1};

        int result = peakIndex(arr);
        System.out.println("The peak Index in given array is: " + result);
    }

    static int peakIndex(int[] arr){
        int start = 0;
        int end = arr.length - 1;

        while(start < end){
            int mid = start + (end - start ) / 2;
            if(arr[mid] < arr[mid + 1]) {
                start = mid + 1;
            }else {
                end = mid;
            }
        }
        return start;

    }
}