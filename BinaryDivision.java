public class BinaryDivision extends BinaryArithmetic {
    private BinarySumDiff difSolver = new BinarySumDiff(0, 0);                      //9/3

    private BinaryCode intermidiateDividend = new BinaryCode(0);
    private BinaryCode intermidiateResult = new BinaryCode(0);
    private Character signOfResult = '0';

    private BinaryCode binResultRemainder = new BinaryCode(0); // binary fractional part of the result

    private final int significantPartSizeElement2;
    private final int significantPartPositionElement1;
    private int sizeOfIntPartOfResult;
    private int sizeOfRemainder;
    private int counter;

    public BinaryDivision(int el1, int el2) {
        super(Math.abs(el1), Math.abs(el2));
        setElement1(el1);
        setElement2(el2);
        placeSign();
        setElement1(Math.abs(el1));
        setElement2(Math.abs(el2));

        significantPartPositionElement1 = getBinElement1().getBinRepresent().indexOf('1');
        int significantPartSizeElement1 = getBinElement1().findSignificantPartSize();
        getBinElement2().getBinRepresent().indexOf('1');
        significantPartSizeElement2 = getBinElement2().findSignificantPartSize();

        sizeOfRemainder = significantPartSizeElement2;

        counter = (getWordLength()-1) - (significantPartSizeElement1 - significantPartSizeElement2);
    }

    public double execBinaryDiv() {
        if (checkForBelowOneResult()) {
            intermidiateResult = getBinElement1();
            return countRemainder();
        }
        double result = 0;
        intermidiateDividend = getBinElement1().leaveCertainDigits(significantPartSizeElement2);
        sizeOfRemainder = significantPartSizeElement2;

        for (int position = significantPartPositionElement1 + significantPartSizeElement2 - 1; position < getWordLength() && position > 0 && counter < 32; position++) {
            if (checkForSizeIncompatibilityToSkipIteration(position)) continue;
            if (checkForLastZeroesToSkipIteration(position)) break;

            difSolver = new BinarySumDiff(Integer.parseInt(intermidiateDividend.toString(), 2), getElement2());
            difSolver.execBinaryDiff();
            intermidiateResult = difSolver.getBinResult();

            processingCurrentRemainder(intermidiateDividend, intermidiateResult);

            if (position + 1 < getWordLength()) getNewDigit(position);
        }
        sizeOfIntPartOfResult = getBinResult().findSignificantPartSize();
        if (!intermidiateResult.checkForNullEquality()) {
            return countRemainder();
        }
        result += (double) processCodeRepresentation();
        getBinResult().getBinRepresent().set(0, signOfResult);
        if (signOfResult == '1') result *= -1;
        return result;
    }

    private boolean checkForBelowOneResult() {
        return Math.abs(getElement1()) < Math.abs(getElement2());
    }


    private double countRemainder() {
        counter = 0;
        intermidiateDividend = intermidiateResult;
        sizeOfIntPartOfResult = getBinResult().findSignificantPartSize();
        getNewDigit();
        double result;
        for (int iter = 0; iter < 5; iter++) {
            if (checkForSizeIncompatibilityToSkipIteration()) continue;

            difSolver = new BinarySumDiff(Integer.parseInt(intermidiateDividend.toString(), 2), getElement2());
            difSolver.execBinaryDiff();
            intermidiateResult = difSolver.getBinResult();

            if (processingCurrentRemainderOfRemainder()) break;

            getNewDigit();
        }

        result = processCodeRepresentation();
        getBinResult().getBinRepresent().set(0, signOfResult);
        if (signOfResult == '1') result *= -1;
        return result;
    }

    private boolean processingCurrentRemainderOfRemainder() {                       //анализ остатка при нахождении остатка от деления
        if (intermidiateResult.checkForNullEquality()) {//остаток равен 0
            setBinResult(getBinResult().makeZeroShift(1));
            getBinResult().getBinRepresent().set(31, '1');
            return true;
        } else if (intermidiateResult.getBinRepresent().get(0) == '0' && intermidiateResult.getBinRepresent().get(1) == '0') { //остаток положительный
            setBinResult(getBinResult().makeZeroShift(1));
            getBinResult().getBinRepresent().set((getWordLength()-1), '1');
            sizeOfRemainder = intermidiateResult.findSignificantPartSize();
            intermidiateDividend = intermidiateResult;
        } else { //другое
            setBinResult(getBinResult().makeZeroShift(1));
            sizeOfRemainder = intermidiateResult.findSignificantPartSize();
            intermidiateResult = intermidiateDividend;
        }
        return false;
    }


    private void processingCurrentRemainder(BinaryCode dividend, BinaryCode result) {
        if (result.checkForNullEquality()) {//остаток равен 0
            getBinResult().getBinRepresent().set(counter++, '1');
            sizeOfRemainder = 0;
        } else if (result.getBinRepresent().get(0) == '0' && result.getBinRepresent().get(1) == '0') { //остаток положительный
            getBinResult().getBinRepresent().set(counter++, '1');
            sizeOfRemainder = result.findSignificantPartSize();
        } else { //другое
            getBinResult().getBinRepresent().set(counter++, '0');
            sizeOfRemainder = dividend.findSignificantPartSize();
        }
    }

    private void getNewDigit(int position) {                   //сносим новую цифру при делении
        binResultRemainder = intermidiateResult;
        insertInRemainderNextDigitFromDivident();
        binResultRemainder.getBinRepresent().set((getWordLength()-1), getBinElement1().getBinRepresent().get(position + 1));
    }


    private void getNewDigit() {
        binResultRemainder = intermidiateResult;
        insertInRemainderNextDigitFromDivident();
    }

    private boolean checkForLastZeroesToSkipIteration(int position) {
        if (intermidiateDividend.checkForNullEquality() && (position == (getWordLength()-1)) && counter < getWordLength() && intermidiateResult.checkForNullEquality()) {
            getBinResult().getBinRepresent().set(counter++, '0');

            return true;
        }
        return false;
    }

    private boolean checkForSizeIncompatibilityToSkipIteration(int position) {
        if (sizeOfRemainder < significantPartSizeElement2 && position + 1 < getWordLength()) {

            getBinResult().getBinRepresent().set(counter++, '0');
            insertInRemainderNextDigitFromDivident(position);

            return true;
        }
        return false;
    }

    private void insertInRemainderNextDigitFromDivident() {
        binResultRemainder = binResultRemainder.makeZeroShift(1);

        sizeOfRemainder = binResultRemainder.findSignificantPartSize();
        intermidiateDividend = binResultRemainder;
    }

    private void insertInRemainderNextDigitFromDivident(int position) {
        insertInRemainderNextDigitFromDivident();
        binResultRemainder.getBinRepresent().set((getWordLength()-1), getBinElement1().getBinRepresent().get(position + 1));
    }


    private boolean checkForSizeIncompatibilityToSkipIteration() {
        if (sizeOfRemainder < significantPartSizeElement2) {

            setBinResult(getBinResult().makeZeroShift(1));
            insertInRemainderNextDigitFromDivident();

            return true;
        }
        return false;
    }

    @Override
    double processCodeRepresentation() {                              //перевод результата в десятичную СС
        double remainder = 0;
        double result = 0;

        sizeOfRemainder = getBinResult().findSignificantPartSize() - sizeOfIntPartOfResult;

        if (sizeOfRemainder != 0) {

            StringBuilder RemainderOnly = new StringBuilder();
            for (int iter = getWordLength() - sizeOfRemainder; iter < getWordLength(); iter++)
                RemainderOnly.append(getBinResult().getBinRepresent().get(iter));

            remainder = Integer.parseInt(RemainderOnly.toString(), 2);
            remainder /= Math.pow(2, sizeOfRemainder);
        }

        if(!getBinResult().checkForNullEquality())
            result = Integer.parseInt(
                    getBinResult().leaveCertainDigits(sizeOfIntPartOfResult).toString(),2);
        result += remainder;
        return result;
    }

    @Override
    protected void placeSign() {
        if (new BinaryCode(getElement1()).getBinRepresent().get(0)
                != new BinaryCode(getElement2()).getBinRepresent().get(0))
            signOfResult = '1';
    }
}
