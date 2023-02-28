public class BinarySumDiff extends BinaryArithmetic{
    public BinarySumDiff(int el1, int el2) {
        super(el1, el2);
    }
    public BinarySumDiff(int el1, int el2, int wordLength) {
        super(el1, el2, wordLength);
    }

    public int execBinarySum() {
        int result;
        if (getBinElement1().getBinRepresent().get(0) != getBinElement2().getBinRepresent().get(0)
                && getBinElement1().equals(getBinElement2())) {
            return 0;
        }else if (getElement1() >= 0 && getElement2() <= 0 && getElement1() > Math.abs(getElement2())) {
            getBinElement2().toAdditionalCode(getElement2());
        }
        else if (getElement1() >= 0 && getElement2() <= 0 && getElement1() < Math.abs(getElement2())) {
            getBinElement2().toReverseCode();
            resultMarker = BinRepresentationMarker.REVERSED;
        }
        else if (getBinElement1().getBinRepresent().get(0)
                        != getBinElement2().getBinRepresent().get(0)) {
            changeSummand();
            return execBinarySum();
        }
        setBinResult(execBinaryAdder());
        placeSign();
        result = (int) processCodeRepresentation();
        return result;
    }

    public int execBinaryDiff() {
        setElement2(getElement2() * -1);
        if(getBinElement2().getBinRepresent().get(0) == '0')
            getBinElement2().getBinRepresent().set(0, '1');
        else getBinElement2().getBinRepresent().set(0, '0');
        return execBinarySum();
    }

    @Override
    protected void placeSign(){
        if( Math.abs(getElement1()) > Math.abs(getElement2()))
            getBinResult().getBinRepresent().set(0, (new BinaryCode(getElement1())).getBinRepresent().get(0));
        else getBinResult().getBinRepresent().set(0, (new BinaryCode(getElement2())).getBinRepresent().get(0));
    }

}

