enum biRepresentMarker {
    direct,
    reversed,
    additional,
}
public class BinaryArithmetic {

    int element1;       BinaryCode bi_element1;
    int element2;       BinaryCode bi_element2;
    double result = 0;         BinaryCode bi_result;

    int WordLength = 32;
    biRepresentMarker result_marker = biRepresentMarker.direct;

    public BinaryArithmetic(int el1, int el2) {
        //System.out.println();
        this.element1 = el1;
        this.element2 = el2;
        //
        bi_element1 = new BinaryCode(el1);
        bi_element2 = new BinaryCode(el2);
        //
        bi_result = new BinaryCode(0, WordLength);
    }

    public BinaryArithmetic(int el1, int el2, int word_length) {
        new BinaryArithmetic(el1, el2);
        WordLength = word_length;
    }
    void processCodeRepresentation() {                          //при необходимости возвращает представление числа в прямой код
        Character Sign = bi_result.getBinRepresent().get(0);
        if (result_marker == biRepresentMarker.reversed) {
            bi_result.invertSignificantBits();
        }
        if (result_marker == biRepresentMarker.additional) {
            bi_result.minusOneBit();
            bi_result.invertSignificantBits();
        }
        bi_result.getBinRepresent().set(0, '0');
        result = Integer.parseInt(bi_result.toString(), 2);
        bi_result.getBinRepresent().set(0, Sign);
        if(Sign == '1') result *= -1;
    }

    public void binaryAdder() {
        for (int i = WordLength-1; i > 0; i--) {
            char to_be_put = bi_element2.getBinRepresent().get(i);
            char already_there = bi_element1.getBinRepresent().get(i);

            if (to_be_put == '0') {
                bi_result.getBinRepresent().set(i, already_there);
                continue;
            }
            if (already_there == '0') {
                bi_result.getBinRepresent().set(i, to_be_put);
                continue;
            }
            if(bi_result.getBinRepresent().get(i)!='1') bi_result.getBinRepresent().set(i, '0');
            makeShift(i);
        }
    }

    protected void makeShift(int index) {                               //переносит единицу в старший разряд
        if (index < 1) {
            bi_result.getBinRepresent().set(index, '1');
            return;
        }
        if (bi_element1.getBinRepresent().get(index-1) == '0') {
            bi_element1.getBinRepresent().set(index-1, '1');
            return;
        }
        if (bi_element2.getBinRepresent().get(index-1) == '0') {
            bi_element2.getBinRepresent().set(index-1, '1');
            return;
        }
        bi_result.getBinRepresent().set(index-1, '1');

    }
    protected void changeSummand() {                                    //меняет слагаемые местами
        int t_el = element1;
        BinaryCode t_bi_el = bi_element1;

        element1 = element2;
        bi_element1 = bi_element2;
        element2 = t_el;
        bi_element2 = t_bi_el;
    }

    protected void placeSign(){}
}
