enum BinRepresentationMarker {
    DIRECT,
    REVERSED,
    ADDITIONAL,
}
public class BinaryArithmetic {

    private int element1;
    private BinaryCode binElement1;
    private int element2;
    private BinaryCode binElement2;
    private BinaryCode binResult;
    private final int WORD_LENGTH;

    BinRepresentationMarker resultMarker = BinRepresentationMarker.DIRECT;

    public int getElement1() {
        return element1;
    }

    public int getElement2() {
        return element2;
    }

    public void setElement1(int element1) {
        this.element1 = element1;
    }

    public BinaryCode getBinElement1() {
        return binElement1;
    }

    public void setBinElement1(BinaryCode binElement1) {
        this.binElement1 = binElement1;
    }

    public void setElement2(int element2) {
        this.element2 = element2;
    }

    public BinaryCode getBinElement2() {
        return binElement2;
    }

    public void setBinElement2(BinaryCode binElement2) {
        this.binElement2 = binElement2;
    }

    public BinaryCode getBinResult() {
        return binResult;
    }

    public void setBinResult(BinaryCode binResult) {
        this.binResult = binResult;
    }

    public int getWordLength() {
        return WORD_LENGTH;
    }

    public BinaryArithmetic(int el1, int el2) {
        WORD_LENGTH = 32;
        element1 = el1;
        element2 = el2;

        binElement1 = new BinaryCode(el1);
        binElement2 = new BinaryCode(el2);

        binResult = new BinaryCode(0, WORD_LENGTH);
    }


    public BinaryArithmetic(int el1, int el2, int wordLength) {
        WORD_LENGTH = wordLength;
        element1 = el1;
        element2 = el2;

        binElement1 = new BinaryCode(el1, WORD_LENGTH);
        binElement2 = new BinaryCode(el2, WORD_LENGTH);

        binResult = new BinaryCode(0, WORD_LENGTH);
    }
    double processCodeRepresentation() {                          //при необходимости возвращает представление числа в прямой код
        Character sign = binResult.getBinRepresent().get(0);
        int result;
        returnToDirectCode(binResult);
        binResult.getBinRepresent().set(0, '0');
        result = Integer.parseInt(binResult.toString(), 2);
        binResult.getBinRepresent().set(0, sign);
        if(sign == '1') result *= -1;
        return result;
    }

    private void returnToDirectCode(BinaryCode code) {
        if (resultMarker == BinRepresentationMarker.REVERSED) {
            code.invertSignificantBits();
        }
        if (resultMarker == BinRepresentationMarker.ADDITIONAL) {
            code.minusOneBit();
            code.invertSignificantBits();
        }
    }

    public BinaryCode execBinaryAdder() {
        for (int iter = WORD_LENGTH -1; iter > 0; iter--) {
            char toBePut = binElement2.getBinRepresent().get(iter);
            char alreadyThere = binElement1.getBinRepresent().get(iter);

            if (toBePut == '0') {
                binResult.getBinRepresent().set(iter, alreadyThere);
                continue;
            }
            if (alreadyThere == '0') {
                binResult.getBinRepresent().set(iter, toBePut);
                continue;
            }
            if(binResult.getBinRepresent().get(iter)!='1') binResult.getBinRepresent().set(iter, '0');
            makeShift(iter);
        }
        return binResult;
    }

    protected void makeShift(int index) {                               //переносит единицу в старший разряд
        if (index < 1) {
            binResult.getBinRepresent().set(index, '1');
            return;
        }
        if (binElement1.getBinRepresent().get(index-1) == '0') {
            binElement1.getBinRepresent().set(index-1, '1');
            return;
        }
        if (binElement2.getBinRepresent().get(index-1) == '0') {
            binElement2.getBinRepresent().set(index-1, '1');
            return;
        }
        binResult.getBinRepresent().set(index-1, '1');

    }
    protected void changeSummand() {                                   //меняет слагаемые местами
        int tempElement = element1;
        BinaryCode tempBinElement = binElement1;

        element1 = element2;
        binElement1 = binElement2;
        element2 = tempElement;
        binElement2 = tempBinElement;
    }

    protected void placeSign(){}
}
