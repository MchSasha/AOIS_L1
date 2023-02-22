public class BinarySumDiff extends BinaryArithmetic{
    public BinarySumDiff(int el1, int el2) {
        super(el1, el2); //binary

    }
    public void execBinarySum() {
        if (bi_element1.getBinRepresent().get(0) != bi_element2.getBinRepresent().get(0)
                && bi_element1.equals(bi_element2)) {
            return;
        }else if (element1 >= 0 && element2 <= 0 && element1 > Math.abs(element2)) {
            bi_element2.toAdditionalCode(element2);
        }
        else if (element1 >= 0 && element2 <= 0 && element1 < Math.abs(element2)) {
            bi_element2.toReverseCode();
            result_marker = biRepresentMarker.reversed;
        }
        else if (bi_element1.getBinRepresent().get(0)
                        != bi_element2.getBinRepresent().get(0)) {
            changeSummand();
            execBinarySum();
            return;
        }

        binaryAdder();
        placeSign();
        processCodeRepresentation();
        //System.out.println("Log: BiRezult = " + result);
    }

    public void execBinaryDiff() {
        element2 *= -1;
        if(bi_element2.getBinRepresent().get(0) == '0')
            bi_element2.getBinRepresent().set(0, '1');
        else bi_element2.getBinRepresent().set(0, '0');
        execBinarySum();
    }

    @Override
    protected void placeSign(){
        if( Math.abs(element1) > Math.abs(element2))
            bi_result.getBinRepresent().set(0, (new BinaryCode(element1)).getBinRepresent().get(0));
        else bi_result.getBinRepresent().set(0, (new BinaryCode(element2)).getBinRepresent().get(0));
    }

}

