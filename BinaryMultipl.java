public class BinaryMultipl extends BinaryArithmetic{
    BinarySumDiff SumSolver; ///??????????
    BinaryCode IntermidRezult;


    public BinaryMultipl(int el1, int el2) {
        super(Math.abs(el1),Math.abs(el2));
        element1 = el1;
        element2 = el2;

        if (el1 < el2) changeSummand();

        int last_digit = (bi_element2.getBinRepresent().get(WordLength-1) == '1') ? 1 : 0;
        IntermidRezult = new BinaryCode(Math.abs(element1 * last_digit));
    }

    public void execBinaryMultipl() {
        for (int i = (WordLength-2), counter = 1; i > 0; i--, counter++) {
            if (bi_element2.getBinRepresent().get(i) == '0') continue;

            BinaryCode current_bi_element1 = IntermidRezult;
            BinaryCode current_bi_element2 = bi_element1.makeZeroShift(counter); // заготавливаем массив под код со свдигами

            SumSolver = new BinarySumDiff(0,0);
            SumSolver.bi_element1 = current_bi_element1;
            SumSolver.bi_element2 = current_bi_element2;
            SumSolver.execBinarySum();
            IntermidRezult = SumSolver.bi_result;
        }
        bi_result = IntermidRezult;
        processCodeRepresentation(); // kuda pihat'
        placeSign();
        if(bi_result.getBinRepresent().get(0) == '1') result *= -1;
        //System.out.println("Log: BiMulti = " + result);
    }

    @Override
    protected void placeSign() {
        if ((new BinaryCode(element1)).getBinRepresent().get(0)
                != (new BinaryCode(element2)).getBinRepresent().get(0)) {
            bi_result.getBinRepresent().set(0, '1');
        }
    }
}
