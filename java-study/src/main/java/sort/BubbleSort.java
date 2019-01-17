package sort;

import java.util.Scanner;

/**
 * description: 冒泡排序
 * <br />
 * Created by mace on 2019/1/2 16:40.
 */
public class BubbleSort {

    /**
     * description: 冒泡排序可指定排序顺序 时间复杂度 O(n2)
     * <br /><br />
     * create by mace on 2019/1/2 17:12.
     * @param arr   待排序的数组
     * @param desc  升序:true 降序:false
     * @return: void
     */
    public static void bubbleSort(int[] arr,boolean desc) {
        for (int i = 0; i < arr.length - 1; i++) {// 外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {// 内层循环控制每一趟排序多少次
                if(desc){
                    if (arr[j] > arr[j + 1]) {//左边比右边的值大,交换位置   升序
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }else{
                    if (arr[j] < arr[j + 1]) {//左边比右边的值小,交换位置   降序
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[5];
        Scanner input = new Scanner(System.in);
        System.out.println("请输入五个整数");
        for (int i = 0; i < 5; i++) {
            nums[i] = input.nextInt();
        }
        System.out.println("排序前...");
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + "\t");
        }
        System.out.println();
        System.out.println();
        bubbleSort(nums, true);
        System.out.println("排序后..."+nums);
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + "\t");
        }
        System.out.println();
    }
}
