package sort;

import java.util.Arrays;

/**
 * description: 快速排序
 * <br />
 * Created by mace on 2019/1/2 17:33.
 */
public class QuickSort {

    public static void quickSort(int[] arr, int left, int right){
        // 1.结束递归
        if(left >= right){
            return;
        }
        // 2.保存变量
        int pivot;
        // 3.完成一趟排序
        pivot = partSort(arr, left, right);
        // 4.调整基准值
        swap(arr, left, pivot);
        // 5.对key左边的数快排
        quickSort(arr, left, pivot-1);
        // 6.对key右边的数快排
        quickSort(arr, pivot+1, right);
    }

    public static int partSort(int[] arr, int left, int right){
        int key = arr[left];//基准值
        while(left < right){
            // 1.从后往前找到比key小的
            while(left < right && arr[right] >= key){
                right--;
            }
            // 2.从前往后找到比key大的
            while(left < right && arr[left] <= key){
                left++;
            }
            swap(arr, left, right);
        }
        return left;
    }

    //交换数组的两个位置
    public static void swap(int[] arr,int i,int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {

        int[] arr = {2,0,1,5,8,3,6,4,9,7};
        System.out.println("排序前......");
        System.out.println(Arrays.toString(arr));
        quickSort(arr, 0, arr.length-1);
        System.out.println("排序后......");
        System.out.println(Arrays.toString(arr));

    }
}
