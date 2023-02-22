public class BinaryArithmeticIEEE754 extends BinaryArithmetic{
    Character Sign = '0';

    double fractional_element1;             //дробные части исходных чисел
    double fractional_element2;

    BinaryCode exp_element1;
    BinaryCode exp_element2;

    int ExpWordLength = 8;

    BinaryArithmeticIEEE754(int el1, int el2) {/////
        super(el1, el2, 23);
        exp_element1 = new BinaryCode(0, ExpWordLength);
        exp_element2 = new BinaryCode(0, ExpWordLength);
        correctWordLength();
    }
    BinaryArithmeticIEEE754() {
        super(0, 0, 23);
    }



    void correctWordLength() {
        bi_element1.setWordLength(WordLength);
        bi_element2.setWordLength(WordLength);
        bi_result.setWordLength(WordLength);
        exp_element1.setWordLength(ExpWordLength);
        exp_element2.setWordLength(ExpWordLength);
    }

}
