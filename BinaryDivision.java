import java.util.ArrayList;

public class BinaryDivision extends BinaryArithmetic {
    BinarySumDiff DifSolver = new BinarySumDiff(0, 0); ///??????????
    BinaryCode IntermidDividend = new BinaryCode(0);
    BinaryCode IntermidResult = new BinaryCode(0);

    Character SignOfResult = '0';
    BinaryCode bi_result_remainder = new BinaryCode(0); // binary fractional part of the result

    int signif_part_size_element1;
    int signif_part_size_element2;
    int signif_part_position_element1;
    int signif_part_position_element2;
    int size_of_int_part_of_result = 0;
    int size_of_remainder = 0;
    int counter = 0;


    public BinaryDivision(int el1, int el2) {
        super(Math.abs(el1), Math.abs(el2));
        element1 = el1;
        element2 = el2;
        placeSign();
        element1 = Math.abs(el1);
        element2 = Math.abs(el2);

        signif_part_position_element1 = (bi_element1.getBinRepresent().indexOf('1'));
        signif_part_size_element1 = bi_element1.findSignifPartSize();
        signif_part_position_element2 = (bi_element2.getBinRepresent().indexOf('1'));
        signif_part_size_element2 = bi_element2.findSignifPartSize();

        size_of_remainder = signif_part_size_element2;

        counter = (WordLength-1) - (signif_part_size_element1 - signif_part_size_element2);
    }

    public void execBinaryDiv() {
        if (checkForBelowOneResult()) return;

        IntermidDividend = bi_element1.leaveCertainDigits(signif_part_size_element2);
        size_of_remainder = signif_part_size_element2;

        for (int i = signif_part_position_element1 + signif_part_size_element2 - 1; i < WordLength && i > 0 && counter < 32; i++) {
            if (checkForSizeIncompatibilityToSkipIteration(i)) continue;
            if (checkForLastZeroesToSkipIteration(i)) break;

            DifSolver = new BinarySumDiff(Integer.parseInt(IntermidDividend.toString(), 2), element2);
            DifSolver.execBinaryDiff();
            IntermidResult = DifSolver.bi_result;

            processingCurrentRemainder();

            if (i + 1 < WordLength) getNewDigit(i);
        }

        size_of_int_part_of_result = bi_result.findSignifPartSize();
        if (!IntermidResult.checkForNullEquality()) {
            countRemainder();
        }
        processCodeRepresentation();
        bi_result.getBinRepresent().set(0, SignOfResult);
        if (SignOfResult == '1') result *= -1;
        //System.out.println("Log:   BiDiv = " + result);
    }

    private boolean checkForBelowOneResult() {
        if (Math.abs(element1) < Math.abs(element2)) {
            IntermidResult = bi_element1;
            countRemainder();
            return true;
        }
        return false;
    }


    private void countRemainder() {
        counter = 0;
        IntermidDividend = IntermidResult;
        size_of_int_part_of_result = bi_result.findSignifPartSize();
        getNewDigit();
        for (int i = 0; i < 5; i++) {
            if (checkForSizeIncompatibilityToSkipIteration()) continue;

            DifSolver = new BinarySumDiff(Integer.parseInt(IntermidDividend.toString(), 2), element2);
            DifSolver.execBinaryDiff();
            IntermidResult = DifSolver.bi_result;

            if (processingCurrentRemainderOfRemainder()) break;

            getNewDigit();
        }
        processCodeRepresentation();
        bi_result.getBinRepresent().set(0, SignOfResult);
        if (SignOfResult == '1') result *= -1;
        //System.out.println("Log:   BiDiv = " + result);
    }

    private boolean processingCurrentRemainderOfRemainder() {                       //анализ остатка при нахождении остатка от деления
        if (IntermidResult.checkForNullEquality()) {//остаток равен 0
            bi_result = bi_result.makeZeroShift(1);
            bi_result.getBinRepresent().set(31, '1');
            return true;
        } else if (IntermidResult.getBinRepresent().get(0) == '0' && IntermidResult.getBinRepresent().get(1) == '0') { //остаток положительный
            bi_result = bi_result.makeZeroShift(1);
            bi_result.getBinRepresent().set((WordLength-1), '1');
            size_of_remainder = IntermidResult.findSignifPartSize();
            IntermidDividend = IntermidResult;
        } else { //другое
            bi_result = bi_result.makeZeroShift(1);
            size_of_remainder = IntermidResult.findSignifPartSize();
            IntermidResult = IntermidDividend;
        }
        return false;
    }


    private void processingCurrentRemainder() {
        if (IntermidResult.checkForNullEquality()) {//остаток равен 0
            bi_result.getBinRepresent().set(counter++, '1');
            size_of_remainder = 0;
            IntermidResult = new BinaryCode(0);
        } else if (IntermidResult.getBinRepresent().get(0) == '0' && IntermidResult.getBinRepresent().get(1) == '0') { //остаток положительный
            bi_result.getBinRepresent().set(counter++, '1');
            size_of_remainder = IntermidResult.findSignifPartSize();
        } else { //другое
            bi_result.getBinRepresent().set(counter++, '0');
            size_of_remainder = IntermidDividend.findSignifPartSize();
            IntermidResult = IntermidDividend;
        }
    }

    private void getNewDigit(int i) {                   //сносим новую цифру при делении
        bi_result_remainder = IntermidResult;
        getNewDigit();
        bi_result_remainder.getBinRepresent().set((WordLength-1), bi_element1.getBinRepresent().get(i + 1));
    }

    private void getNewDigit() {
        bi_result_remainder = IntermidResult;
        insertInRemainderNextDigitFromDivident();
    }

    private boolean checkForLastZeroesToSkipIteration(int i) {
        if (IntermidDividend.checkForNullEquality() && (i == (WordLength-1)) && counter < WordLength && IntermidResult.checkForNullEquality()) {
            bi_result.getBinRepresent().set(counter++, '0');

            return true;
        }
        return false;
    }

    private boolean checkForSizeIncompatibilityToSkipIteration(int i) {
        if (size_of_remainder < signif_part_size_element2 && i + 1 < WordLength) {

            bi_result.getBinRepresent().set(counter++, '0');
            insertInRemainderNextDigitFromDivident(i);

            return true;
        }
        return false;
    }

    private void insertInRemainderNextDigitFromDivident() {
        bi_result_remainder = bi_result_remainder.makeZeroShift(1);

        size_of_remainder = bi_result_remainder.findSignifPartSize();
        IntermidDividend = bi_result_remainder;
    }
    private void insertInRemainderNextDigitFromDivident(int i) {
        insertInRemainderNextDigitFromDivident();

        bi_result_remainder.getBinRepresent().set((WordLength-1), bi_element1.getBinRepresent().get(i + 1));
    }


    private boolean checkForSizeIncompatibilityToSkipIteration() {
        if (size_of_remainder < signif_part_size_element2) {

            bi_result = bi_result.makeZeroShift(1);
            insertInRemainderNextDigitFromDivident();

            return true;
        }
        return false;
    }

    @Override
    void processCodeRepresentation() {                              //перевод результата в десятичную СС
        double remainder = 0;

        size_of_remainder = bi_result.findSignifPartSize() - size_of_int_part_of_result;
        if (size_of_remainder != 0) {

            StringBuilder RemainderOnly = new StringBuilder();
            for (int i = WordLength - size_of_remainder; i < WordLength; i++)
                RemainderOnly.append(bi_result.getBinRepresent().get(i));

            remainder = Integer.parseInt(RemainderOnly.toString(), 2);
            remainder /= Math.pow(2, size_of_remainder);
        }

        if(!bi_result.checkForNullEquality())
            result = Integer.parseInt(
                    bi_result.leaveCertainDigits(size_of_int_part_of_result).toString(),2);
        result += remainder;
    }

    @Override
    protected void placeSign() {
        if (new BinaryCode(element1).getBinRepresent().get(0)
                != new BinaryCode(element2).getBinRepresent().get(0))
            SignOfResult = '1';
    }

}
