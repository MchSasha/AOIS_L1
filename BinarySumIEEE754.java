public class BinarySumIEEE754 extends BinaryArithmeticIEEE754 {
    private  int sizeOfFractPartEl1 = 0;
    private int sizeOfFractPartEl2 = 0;

    BinarySumIEEE754(double el1, double el2) {
        StringBuilder strEl1 = new StringBuilder(Double.toString(el1));
        StringBuilder strEl2 = new StringBuilder(Double.toString(el2));
        strEl1 = correctInput(strEl1);
        strEl2 = correctInput(strEl2);

        convertToBinaryRepresent(strEl1.toString(), strEl2.toString());

        setBinResult(new BinaryCode(0, getWordLength()));
    }

    private static StringBuilder correctInput(StringBuilder strEl) {                    //корректируем представление числа, если вдруг оно пришло в формате с Е
        if (strEl.indexOf("E") >= 0){
            int index = strEl.indexOf("E");
            double el = Double.parseDouble(strEl.substring(0, index));
            int degree = Integer.parseInt(strEl.substring(index + 1));
            strEl = new StringBuilder();
            strEl.append("0.");
            while (degree < -1) {
                strEl.append('0');
                degree++;
            }
            strEl.append(Double.toString(el), 0, Double.toString(el).indexOf('.'));
        }
        return strEl;
    }

    public double execBinarySumIEEE754() {
        BinaryCode el1 = new BinaryCode(getBinElement1(), getWordLength());
        BinaryCode el2 = new BinaryCode(getBinElement2(), getWordLength());
        setBinResult(execBinaryAdder());
        if (el1.getBinRepresent().get(0) != el2.getBinRepresent().get(0)) {
            getBinResult().getBinRepresent().set(0, '1');
        }
        if(el1.getBinRepresent().get(0) == el2.getBinRepresent().get(0) && el2.getBinRepresent().get(0)  == '1' && getExpElement1().checkForNullEquality())
        {
            getBinResult().getBinRepresent().add(0, '1');
            setExpElement1(new BinaryCode(Integer.parseInt(getExpElement1().toString(), 2) + 1,
                    getExpWordLength()));
            setBinResult(getBinResult().leaveCertainDigits(getWordLength()));
        }
        setBinElement1(el1);
        setBinElement2(el2);

        double result = processCodeRepresentation();
        double scale = Math.pow(10, Math.max(sizeOfFractPartEl1, sizeOfFractPartEl2));
        result = Math.ceil(result * scale) / scale;
        return result;
    }

    void convertToBinaryRepresent(String el1, String el2 ) {
        convertToIntBinaryRepresent(el1, el2);

        setExpElement1( new BinaryCode(getBinElement1().findUnsignedSignificantPartSize(), getExpWordLength()));
        setExpElement2 (new BinaryCode(getBinElement2().findUnsignedSignificantPartSize(), getExpWordLength()));

        convertToFractBinaryRepresent(el1, el2);
        toNormalizedForm();
    }

    double processCodeRepresentation() {                          //переводит результат в десятичную СС
        double remainder = 0;
        double result = 0;

        int sizeOfIntPartOfResult = Integer.parseInt(getExpElement1().toString(), 2);
        int sizeOfRemainder = getWordLength() - sizeOfIntPartOfResult;

        if (sizeOfRemainder != 0){
            remainder = getRemainder(sizeOfRemainder);
        }

        if(!(getBinResult().checkForNullEquality())) {
            result = Integer.parseInt(
                    getBinResult().leaveCertainDigits(sizeOfIntPartOfResult).toString(), 2);
        }

        result += remainder;
        return result;
    }

    private double getRemainder(int sizeOfRemainder) {
        StringBuilder RemainderOnly = new StringBuilder();
        for (int iter = getWordLength() - sizeOfRemainder; iter < getWordLength(); iter++)
            RemainderOnly.append(getBinResult().getBinRepresent().get(iter));

        double remainder = Integer.parseInt(RemainderOnly.toString(), 2);
        remainder /= Math.pow(2, sizeOfRemainder);

        return remainder;
    }

    private void convertToFractBinaryRepresent(String el1, String el2) {                //переводит дробную часть исходного числа в двоичную СС
        int dotPosition = el1.indexOf('.');
        String fractionalPart = el1.substring(dotPosition + 1);
        sizeOfFractPartEl1 = fractionalPart.length();
        setFractionalElement1(Integer.parseInt(fractionalPart) / Math.pow(10, sizeOfFractPartEl1));

        setBinElement1(getBiRepresentOfFractPart(getBinElement1(), getFractionalElement1()));

        dotPosition = el2.indexOf('.');
        fractionalPart = el2.substring(dotPosition + 1);
        sizeOfFractPartEl2 = fractionalPart.length();
        setFractionalElement2(Integer.parseInt(fractionalPart) / Math.pow(10, sizeOfFractPartEl2));

        setBinElement2(getBiRepresentOfFractPart(getBinElement2(), getFractionalElement2()));
    }

    private BinaryCode getBiRepresentOfFractPart(BinaryCode intPart, double fractionalPart) {                //алгоритм перевода дробной части в двоичную СС

        double remainder = fractionalPart;
        int availableDigits = getWordLength() - intPart.findSignificantPartSize();

        while (remainder > 0 && (availableDigits--) > 0) {
            remainder *= 2.0;

            if (remainder >= 1) {
                remainder -= 1.0;
                intPart = intPart.makeZeroShift(1);
                intPart.getBinRepresent().set(getWordLength() - 1, '1');
            } else intPart = intPart.makeZeroShift(1);
        }
        return intPart;
    }

    private void convertToIntBinaryRepresent(String el1, String el2) {                          //перевод целой части числа в двоичную СС
        int dotPosition = el1.indexOf('.');

        String intPart = el1.substring(0, dotPosition);
        setElement1(Integer.parseInt(intPart));
        setBinElement1(new BinaryCode(getElement1(), getWordLength()));

        dotPosition = el2.indexOf('.');

        intPart = el2.substring(0, dotPosition);
        setElement2(Integer.parseInt(intPart));
        setBinElement2(new BinaryCode(getElement2(), getWordLength()));
    }

    private void toNormalizedForm() {
        int el1 = Integer.parseInt(getExpElement1().toString(), 2);  //нормализация мантисс
        int el2 = Integer.parseInt(getExpElement2().toString(), 2);

        BinarySumDiff sumSolver = new BinarySumDiff(el1, el2, getExpWordLength());
        sumSolver.setBinResult(new BinaryCode(0, getExpWordLength()));

        int numOfShifts = sumSolver.execBinaryDiff();;

        if (numOfShifts > 0) {
            setExpElement2(getExpElement1());
            setBinElement2(getBinElement2().leaveCertainDigits(getBinElement2().findUnsignedSignificantPartSize() - numOfShifts));
        } else {
            setExpElement1(getExpElement2());
            setBinElement1(getBinElement1().leaveCertainDigits(getBinElement1().findUnsignedSignificantPartSize() + numOfShifts));
        }
    }
}
