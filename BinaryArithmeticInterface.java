import java.util.Scanner;

public class BinaryArithmeticInterface {
    static boolean isOn = true;

    static void run() {
        while (isOn) {
            System.out.println("Choose the task you want to check\n 1. Sum\t 2. Difference" +
                    "\t 3. Multiplication\t 4. Division\t 5. Sum according to IEEE754-2008\t 0. Exit");
            Scanner in = new Scanner(System.in);
            int num = in.nextInt();
            int el1 = 0, el2 = 0;
            switch (num) {
                case 1:
                    System.out.println("Enter values");
                    el1 = in.nextInt();
                    el2 = in.nextInt();
                    BinarySumDiff sum = new BinarySumDiff(el1, el2);
                    sum.execBinarySum();
                    System.out.printf("%d + %d = %d \n", el1, el2, (int)sum.result);
                    break;
                case 2:
                    System.out.println("Enter values");
                    el1 = in.nextInt();
                    el2 = in.nextInt();
                    BinarySumDiff dif = new BinarySumDiff(el1, el2);
                    dif.execBinaryDiff();
                    System.out.printf("%d - %d = %d \n", el1, el2, (int)dif.result);
                    break;
                case 3:
                    System.out.println("Enter values");
                    el1 = in.nextInt();
                    el2 = in.nextInt();
                    BinaryMultipl multi = new BinaryMultipl(el1, el2);
                    multi.execBinaryMultipl();
                    System.out.printf("%d * %d = %d \n" , el1, el2,(int) multi.result);
                    break;
                case 4:
                    System.out.println("Enter values");
                    el1 = in.nextInt();
                    el2 = in.nextInt();
                    BinaryDivision div = new BinaryDivision(el1, el2);
                    div.execBinaryDiv();
                    System.out.printf("%d / %d = %f \n" , el1, el2, div.result);
                    break;
                case 5:
                    System.out.println("Enter values");
                    double d_el1 = Double.parseDouble(in.next());
                    double d_el2 = Double.parseDouble(in.next());
                    BinarySumIEEE754 d_sum = new BinarySumIEEE754(d_el1, d_el2);
                    d_sum.execBinarySumIEEE754();
                    if (d_sum.result < 1 || d_el1 < 1 || d_el2 < 1) {
                        System.out.printf("%s + %s = %s\n" , Double.toString(d_el1), Double.toString(d_el2), Double.toString(d_sum.result));
                        break;
                    }
                    System.out.printf("%f + %f = %f\n" , d_el1, d_el2, d_sum.result);
                    break;
                case 0:
                    isOn = false;
            }
        }
    }
}
