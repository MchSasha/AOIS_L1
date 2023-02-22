import java.util.ArrayList;
import java.util.Objects;

public class BinaryCode {

    private ArrayList<Character> BinaryRepresentation;
    private int WordLength = 32;

    public void setBiRepresent(ArrayList<Character> biRepresent) {
        BinaryRepresentation = biRepresent;
    }
    public ArrayList<Character> getBinRepresent() {
        return BinaryRepresentation;
    }
    public int getWordLength() {
        return WordLength;
    }
    public int setWordLength(int newWordLengt) {
        return WordLength = newWordLengt;
    }

    public BinaryCode(int i) {
        toDirectCode(i);
    }
    public BinaryCode(int i, int word_length) {
        WordLength = word_length;
        toDirectCode(i);
    }
    public BinaryCode(BinaryCode that, int word_length) {
        WordLength = word_length;
        BinaryRepresentation = (ArrayList<Character>) that.BinaryRepresentation.clone();
    }

    void toAdditionalCode(int i) { //was rename intejer -> i , i -> j
        if (i < 0){
            toReverseCode(i);
            plusOneBit();
        }
    }

    void toReverseCode(){
        toReverseCode(-1);
    }

    void toReverseCode(int i) {

        if (i < 0) {
            invertSignificantBits();
            BinaryRepresentation.set(0, '1');
        }
    }
    void toDirectCode(int i) {
        ArrayList<Character> DirectCode = new ArrayList<>();
        int modulo = Math.abs(i);

        for (char ch : Integer.toBinaryString(modulo).toCharArray()) {
            DirectCode.add(ch);
        }

        toFullLength(DirectCode);
        DirectCode.set(0, (i >= 0) ? '0' : '1');

        BinaryRepresentation = DirectCode;

    }
    void plusOneBit() {
        boolean BitShift = true;
        for (int j = (WordLength - 1); j >= 0 && BitShift; j--) {
            char to_be_put = (BinaryRepresentation.get(j) == '0') ? '1' : '0';
            BitShift = (to_be_put == '0') ? true : false;
            BinaryRepresentation.set(j, to_be_put);
        }
    }

    void minusOneBit() {
        boolean BitShift = true;
        for (int j = (WordLength - 1); j >= 0 && BitShift; j--) {
            char to_be_put = (BinaryRepresentation.get(j) == '1') ? '0' : '1';
            BitShift = (to_be_put == '0') ? false : true;
            BinaryRepresentation.set(j, to_be_put);
        }
    }

    protected BinaryCode makeZeroShift(int num_of_shifts) {             //сдвигает значущую часть числа на определенное кол-во нулей влево
        BinaryCode result = new BinaryCode(0, WordLength);
        for (int i = (WordLength - 1) - num_of_shifts, j = (WordLength - 1); i >= 0; i--, j--) {
            result.getBinRepresent().set(i, BinaryRepresentation.get(j));
        }
        return result;
    }

    public void toFullLength(ArrayList<Character> BinaryCode) {
        int leftBits = WordLength - BinaryCode.size();

        while (leftBits-- != 0) BinaryCode.add(0, '0');

    }
    public void invertSignificantBits() {
        for (int i = 0; i < BinaryRepresentation.size(); i++) {
            if (BinaryRepresentation.get(i) == '0') {
                BinaryRepresentation.set(i, '1');
            } else {
                BinaryRepresentation.set(i, '0');
            }
        }
    }

    public BinaryCode leaveCertainDigits(int num_of_digits) {               //оставляет определенное кол-во разрядов из представления числа, создавая новое число
        BinaryCode result = new BinaryCode(0, WordLength);
        int position_of_signif_part = BinaryRepresentation.indexOf('1');
        int shift = WordLength  - position_of_signif_part - num_of_digits;
        if(shift < 0)
            return this;
        for (int i = position_of_signif_part + shift, j = position_of_signif_part; i < WordLength; i++, j++) {
            result.getBinRepresent().set(i, BinaryRepresentation.get(j));
        }
        return result;
    }

    public boolean checkForNullEquality() {
        int counter = 0;
        for (Character ch : BinaryRepresentation) {
            if (ch == '1') counter++;
        }
        if(counter == WordLength)
            return true;
        counter = 0;
        for (Character ch : BinaryRepresentation) {
            if (ch == '0') counter++;
        }
        if(counter == WordLength)
            return true;

        return false;
    }

    public int findSignifPartSize() {
        char temp = BinaryRepresentation.get(0);
        BinaryRepresentation.set(0, '0');
        int signif_part_position_element = (BinaryRepresentation.indexOf('1'));
        BinaryRepresentation.set(0, temp);
        if (signif_part_position_element == -1) {
            return 0;
        }
        return WordLength - signif_part_position_element;
    }
    public int findUnsignedSignifPartSize() {
        int signif_part_position_element = (BinaryRepresentation.indexOf('1'));
        if (signif_part_position_element == -1) {
            return 0;
        }
        return WordLength - signif_part_position_element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryCode that = (BinaryCode) o;

        Character to_keep = that.BinaryRepresentation.get(0);
        that.BinaryRepresentation.set(0, BinaryRepresentation.get(0));

        boolean ans = BinaryRepresentation.equals(that.BinaryRepresentation);

        that.BinaryRepresentation.set(0, to_keep);

        return ans;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (Character ch : BinaryRepresentation) {
            res.append(ch);
        }

        return res.toString();
    }
    @Override
    public int hashCode() {
        return Objects.hash(BinaryRepresentation);
    }
}
