public class BinaryArithmeticIEEE754 extends BinaryArithmetic{
    private double fractionalElement1;             //дробные части исходных чисел
    private double fractionalElement2;

    private BinaryCode expElement1;
    private BinaryCode expElement2;

    private static final int EXP_WORD_LENGTH = 8;
    private static final int WORD_LENGTH = 23;

    public int getExpWordLength() {
        return EXP_WORD_LENGTH;
    }

    BinaryArithmeticIEEE754() {
        super(0, 0, WORD_LENGTH);
    }

    public BinaryCode getExpElement1() {
        return expElement1;
    }

    public void setExpElement1(BinaryCode expElement1) {
        this.expElement1 = expElement1;
    }

    public BinaryCode getExpElement2() {
        return expElement2;
    }

    public void setExpElement2(BinaryCode expElement2) {
        this.expElement2 = expElement2;
    }

    public double getFractionalElement1() {
        return fractionalElement1;
    }

    public void setFractionalElement1(double fractionalElement1) {
        this.fractionalElement1 = fractionalElement1;
    }

    public double getFractionalElement2() {
        return fractionalElement2;
    }

    public void setFractionalElement2(double fractionalElement2) {
        this.fractionalElement2 = fractionalElement2;
    }
}
