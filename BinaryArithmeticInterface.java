import java.util.Scanner;

public class BinaryArithmeticInterface {
    private static boolean isOn = true;
    private static final Scanner in = new Scanner(System.in);

    static void run() {
        while (isOn) {
            System.out.println("Choose the task you want to check\n 1. Sum\t 2. Difference" +
                    "\t 3. Multiplication\t 4. Division\t 5. Sum according to IEEE754-2008\t 0. Exit");
            getArithmeticCase(in.nextInt());
        }
    }

    private static void getArithmeticCase(int numberOfCase) {
        System.out.println("Enter values");
        switch (numberOfCase) {
            case 1:
                runBinarySum(in.nextInt(), in.nextInt());
                break;
            case 2:
                runBinaryDiff(in.nextInt(), in.nextInt());
                break;
            case 3:
                runBinaryMultipl(in.nextInt(), in.nextInt());
                break;
            case 4:
                runBinaryDivision(in.nextInt(), in.nextInt());
                break;
            case 5:
                runBinarySumIEEE754(Double.parseDouble(in.next()), Double.parseDouble(in.next()));
                break;
            case 0:
                isOn = false;
        }
    }

    private static void runBinarySum(int el1, int el2) {
        BinarySumDiff sum = new BinarySumDiff(el1, el2);
        System.out.printf("%d + %d = %d\n", el1, el2, sum.execBinarySum());
    }
    private static void runBinaryDiff(int el1, int el2) {
        BinarySumDiff dif = new BinarySumDiff(el1, el2);
        System.out.printf("%d - %d = %d\n", el1, el2, dif.execBinaryDiff());
    }
    private static void runBinaryMultipl(int el1, int el2) {
        BinaryMultipl multi = new BinaryMultipl(el1, el2);
        System.out.printf("%d * %d = %d\n", el1, el2, multi.execBinaryMultipl());
    }
    private static void runBinaryDivision(int el1, int el2) {
        BinaryDivision div = new BinaryDivision(el1, el2);
        double result = div.execBinaryDiv();
        System.out.printf("%d / %d = %.8f\n", el1, el2, result);
    }

    private static void runBinarySumIEEE754(double el1, double el2) {

        BinarySumIEEE754 sumInDouble = new BinarySumIEEE754(el1, el2);
        double result = sumInDouble.execBinarySumIEEE754();
        if (result < 1 || el1 < 1 || el2 < 1) {
            System.out.printf("%.5f + %.5f = %.5f\n", el1, el2, result);
        } else {
            System.out.printf("%f + %f = %f\n", el1, el2, result);
        }

    }
}
