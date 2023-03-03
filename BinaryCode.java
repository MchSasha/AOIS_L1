import java.util.ArrayList;
import java.util.Objects;

public class BinaryCode {

    private ArrayList<Character> binaryRepresentation;
    private final int WORD_LENGTH;
    public static final int WORLD_LENGTH32 = 32;

    public void setBiRepresent(ArrayList<Character> biRepresent) {
        binaryRepresentation = biRepresent;
    }
    public ArrayList<Character> getBinRepresent() {
        return binaryRepresentation;
    }
    public int getWordLength() {
        return WORD_LENGTH;
    }
    public BinaryCode(int integer) {
        WORD_LENGTH = WORLD_LENGTH32;
        toDirectCode(integer);
    }
    public BinaryCode(int integer, int wordLength) {
        WORD_LENGTH = wordLength;
        toDirectCode(integer);
    }
    public BinaryCode(BinaryCode that, int wordLength) {
        WORD_LENGTH = wordLength;
        binaryRepresentation = (ArrayList<Character>) that.binaryRepresentation.clone();
    }

    void toAdditionalCode(int integer) {
        if (integer < 0){
            toReverseCode(integer);
            plusOneBit();
        }
    }

    void toReverseCode(){
        toReverseCode(-1);
    }

    void toReverseCode(int integer) {

        if (integer < 0) {
            invertSignificantBits();
            binaryRepresentation.set(0, '1');
        }
    }
    void toDirectCode(int integer) {
        ArrayList<Character> directCode = new ArrayList<>();
        int modulo = Math.abs(integer);

        for (char character : Integer.toBinaryString(modulo).toCharArray()) {
            directCode.add(character);
        }

        toFullLength(directCode);
        directCode.set(0, (integer >= 0) ? '0' : '1');

        binaryRepresentation = directCode;

    }
    void plusOneBit() {
        boolean bitShift = true;
        for (int iter = (WORD_LENGTH - 1); iter >= 0 && bitShift; iter--) {
            char toBePut = (binaryRepresentation.get(iter) == '0') ? '1' : '0';
            bitShift = (toBePut == '0');
            binaryRepresentation.set(iter, toBePut);
        }
    }

    void minusOneBit() {
        boolean bitShift = true;
        for (int j = (WORD_LENGTH - 1); j >= 0 && bitShift; j--) {
            char toBePut = (binaryRepresentation.get(j) == '1') ? '0' : '1';
            bitShift = (toBePut != '0');
            binaryRepresentation.set(j, toBePut);
        }
    }

    protected BinaryCode makeZeroShift(int numOfShifts) {             //сдвигает значущую часть числа на определенное кол-во нулей влево
        BinaryCode result = new BinaryCode(0, WORD_LENGTH);
        for (int iter1 = (WORD_LENGTH - 1) - numOfShifts, iter2 = (WORD_LENGTH - 1); iter1 >= 0; iter1--, iter2--) {
            result.getBinRepresent().set(iter1, binaryRepresentation.get(iter2));
        }
        return result;
    }

    public void toFullLength(ArrayList<Character> BinaryCode) {
        int leftBits = WORD_LENGTH - BinaryCode.size();

        while (leftBits-- != 0) BinaryCode.add(0, '0');

    }
    public void invertSignificantBits() {
        for (int iter = 0; iter < WORD_LENGTH; iter++) {
            if (binaryRepresentation.get(iter) == '0') {
                binaryRepresentation.set(iter, '1');
            } else {
                binaryRepresentation.set(iter, '0');
            }
        }
    }

    public BinaryCode leaveCertainDigits(int numOfDigits) {               //оставляет определенное кол-во разрядов из представления числа, создавая новое число
        BinaryCode result = new BinaryCode(0, WORD_LENGTH);
        int positionOfSignificantPart = binaryRepresentation.indexOf('1');
        int shift = WORD_LENGTH - positionOfSignificantPart - numOfDigits;
        if(shift < 0)
            return this;
        for (int iter1 = positionOfSignificantPart + shift, iter2 = positionOfSignificantPart; iter1 < WORD_LENGTH; iter1++, iter2++) {
            result.getBinRepresent().set(iter1, binaryRepresentation.get(iter2));
        }
        return result;
    }

    public boolean checkForNullEquality() {
        int counter = 0;
        for (Character character : binaryRepresentation) {
            if (character == '1') counter++;
        }
        if(counter == WORD_LENGTH)
            return true;
        counter = 0;
        for (Character character : binaryRepresentation) {
            if (character == '0') counter++;
        }

        return counter == WORD_LENGTH;
    }

    public int findSignificantPartSize() {
        char temp = binaryRepresentation.get(0);
        binaryRepresentation.set(0, '0');
        int significantPartPositionElement = (binaryRepresentation.indexOf('1'));
        binaryRepresentation.set(0, temp);
        if (significantPartPositionElement == -1) {
            return 0;
        }
        return WORD_LENGTH - significantPartPositionElement;
    }
    public int findUnsignedSignificantPartSize() {
        int significantPartPositionElement = (binaryRepresentation.indexOf('1'));
        if (significantPartPositionElement == -1) {
            return 0;
        }
        return WORD_LENGTH - significantPartPositionElement;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BinaryCode that = (BinaryCode) obj;

        Character toKeep = that.binaryRepresentation.get(0);
        that.binaryRepresentation.set(0, binaryRepresentation.get(0));

        boolean ans = binaryRepresentation.equals(that.binaryRepresentation);

        that.binaryRepresentation.set(0, toKeep);
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (Character ch : binaryRepresentation) {
            res.append(ch);
        }

        return res.toString();
    }
    @Override
    public int hashCode() {
        return Objects.hash(binaryRepresentation);
    }
}
