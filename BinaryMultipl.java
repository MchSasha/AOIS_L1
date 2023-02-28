public class BinaryMultipl extends BinaryArithmetic{
    private BinaryCode intermidiateResult;


    public BinaryMultipl(int el1, int el2) {
        super(Math.abs(el1),Math.abs(el2));
        setElement1(el1);
        setElement2(el2);

        if (el1 < el2) changeSummand();

        int last_digit = (getBinElement2().getBinRepresent().get(getWordLength()-1) == '1') ? 1 : 0;
        intermidiateResult = new BinaryCode(Math.abs(getElement1() * last_digit));
    }

    public int execBinaryMultipl() {
        int result;
        for (int iter = (getWordLength()-2), counter = 1; iter > 0; iter--, counter++) {
            if (getBinElement2().getBinRepresent().get(iter) == '0') continue;

            BinaryCode currentBinElement1 = intermidiateResult;
            BinaryCode currentBinElement2 = getBinElement1().makeZeroShift(counter); // заготавливаем массив под код со свдигами

            BinarySumDiff sumSolver = new BinarySumDiff(0, 0);
            sumSolver.setBinElement1(currentBinElement1);
            sumSolver.setBinElement2(currentBinElement2);

            sumSolver.execBinarySum();
            intermidiateResult = sumSolver.getBinResult();
        }
        setBinResult(intermidiateResult);
        result = (int) processCodeRepresentation();
        placeSign();
        if(getBinResult().getBinRepresent().get(0) == '1') result *= -1;
        return result;
    }

    @Override
    protected void placeSign() {
        if ((new BinaryCode(getElement1())).getBinRepresent().get(0)
                != (new BinaryCode(getElement2())).getBinRepresent().get(0)) {
            getBinResult().getBinRepresent().set(0, '1');
        }
    }
}
