public class BinarySumIEEE754 extends BinaryArithmeticIEEE754 {
    BinarySumDiff SumSolver = new BinarySumDiff(1,1);


    int size_of_int_part_el1 = 0;
    int size_of_int_part_el2 = 0;
    int size_of_fract_part_el1 = 0;
    int size_of_fract_part_el2 = 0;

    BinarySumIEEE754(double el1, double el2) {
        StringBuilder s_el1 = new StringBuilder(Double.toString(el1));
        StringBuilder s_el2 = new StringBuilder(Double.toString(el2));
        s_el1 = correctRepresent(s_el1);
        s_el2 = correctRepresent(s_el2);

        convertToBinaryRepresent(s_el1.toString(), s_el2.toString());

        bi_result = new BinaryCode(0, WordLength);
    }

    private static StringBuilder correctRepresent(StringBuilder s_el1) {                    //корректируем представление числа, если вдруг оно пришло в формате с Е
        if (s_el1.indexOf("E") >= 0){
            int index = s_el1.indexOf("E");
            double el = Double.parseDouble(s_el1.substring(0, index));
            int degree = Integer.parseInt(s_el1.substring(index + 1));
            s_el1 = new StringBuilder("");
            s_el1.append("0.");
            while (degree < -1) {
                s_el1.append('0');
                degree++;
            }
            s_el1.append(Double.toString(el).substring(0, Double.toString(el).indexOf('.')));
        }
        return s_el1;
    }

    public void execBinarySumIEEE754() {
        BinaryCode el1 = new BinaryCode(bi_element1, WordLength);
        BinaryCode el2 = new BinaryCode(bi_element2, WordLength);
        binaryAdder();

        bi_element1 = el1;
        bi_element2 = el2;

      if (checkForOverflow()) {
            int new_exp = Integer.parseInt(exp_element1.toString(), 2) + 1;
            exp_element1 = new BinaryCode(new_exp, ExpWordLength);
            exp_element2 = exp_element1;
        }
        processCodeRepresentation();

        double scale = Math.pow(10, Math.max(size_of_fract_part_el1, size_of_fract_part_el2));
        result = Math.ceil(result * scale) / scale;

    }

    private boolean checkForOverflow() {                        //проверка на выход за пределы отведенных разрядов
        int num_of_first_ones_el1 = 0;
        int num_of_first_ones_el2 = 0;
        for (int i = 0; i < WordLength; i++) {
            if (bi_element1.getBinRepresent().get(i) == '1') num_of_first_ones_el1++; else break;
            if (bi_element2.getBinRepresent().get(i) == '1') num_of_first_ones_el2++; else break;
        }
        int positon_to_repeat = Integer.max(num_of_first_ones_el1, num_of_first_ones_el2);

        if (binaryAdderFromCertainPlace(positon_to_repeat)) return true;
        return false;
    }

    private boolean binaryAdderFromCertainPlace(int positon_to_repeat) {                //сумматор для пересчета случая с переполнением
        for (int i = positon_to_repeat-1; i >= 0; i--) {
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
            if ((bi_result.getBinRepresent().get(i) =='0')) {
                makeShift(i); continue;
            }
            if (i == 0) {
                bi_result.getBinRepresent().add(0, '1');
                bi_result.getBinRepresent().remove(WordLength);
                return true;
            }
            makeShift(i);
        }
        return false;
    }

    void convertToBinaryRepresent(String el1, String el2 ) {
        convertToIntBinaryRepresent(el1, el2);

        exp_element1 = new BinaryCode(bi_element1.findUnsignedSignifPartSize(), ExpWordLength);
        exp_element2 = new BinaryCode(bi_element2.findUnsignedSignifPartSize(), ExpWordLength);

        convertToFractBinaryRepresent(el1, el2);
        toNormalizedForm();
    }

    @Override
    void processCodeRepresentation() {                          //переводит результат в десятичную СС
        double remainder = 0;

        int size_of_int_part_of_result = Integer.parseInt(exp_element1.toString(), 2);
        int size_of_remainder = WordLength - size_of_int_part_of_result; //bi_result.findUnsignedSignifPartSize() - size_of_int_part_of_result;
        if (size_of_remainder != 0) {

            StringBuilder RemainderOnly = new StringBuilder();
            for (int i = WordLength - size_of_remainder; i < WordLength; i++)
                RemainderOnly.append(bi_result.getBinRepresent().get(i));

            remainder = Integer.parseInt(RemainderOnly.toString(), 2);
            remainder /= Math.pow(2, size_of_remainder);
        }

        if(!bi_result.checkForNullEquality())
            result = Integer.parseInt(
                    bi_result.leaveCertainDigits(size_of_int_part_of_result).toString(),2);
        result += remainder;
    }

    private void convertToFractBinaryRepresent(String el1, String el2) {                //переводит дробную часть исходного числа в двоичную СС
        int dot_position = el1.indexOf('.');
        String fractional_part = el1.substring(dot_position + 1, el1.length());
        size_of_fract_part_el1 = fractional_part.length();
        fractional_element1 = Integer.parseInt(fractional_part) / Math.pow(10, size_of_fract_part_el1);

        bi_element1 = getBiRepresentOfFractPart(bi_element1, fractional_element1);

        dot_position = el2.indexOf('.');
        fractional_part = el2.substring(dot_position + 1, el2.length());
        size_of_fract_part_el2 = fractional_part.length();
        fractional_element2 = Integer.parseInt(fractional_part) / Math.pow(10, size_of_fract_part_el2);

        bi_element2 = getBiRepresentOfFractPart(bi_element2, fractional_element2);
    }

    private BinaryCode getBiRepresentOfFractPart(BinaryCode int_part, double int_fract_el) {                //алгоритм перевода дробной части в двоичную СС

        double remainder = Double.valueOf(int_fract_el);
        int available_digits = WordLength - int_part.findSignifPartSize();

        while (remainder > 0 && (available_digits--) > 0) {
            remainder *= 2.0;

            if (remainder >= 1) {
                remainder -= 1.0;
                int_part = int_part.makeZeroShift(1);
                int_part.getBinRepresent().set(WordLength - 1, '1');
            } else int_part = int_part.makeZeroShift(1);
        }
        return int_part;
    }

    private void convertToIntBinaryRepresent(String el1, String el2) {                          //перевод целой части числа в двоичную СС
        int dot_position = el1.indexOf('.');

        String int_part = el1.substring(0, dot_position);
        size_of_int_part_el1 = int_part.length();
        element1 = Integer.parseInt(int_part);
        bi_element1 = new BinaryCode(element1, WordLength);

        dot_position = el2.indexOf('.');

        int_part = el2.substring(0, dot_position);
        size_of_int_part_el2 = int_part.length();
        element2 = Integer.parseInt(int_part);
        bi_element2 = new BinaryCode(element2, WordLength);
    }

    private void toNormalizedForm() {                                                       //нормализация мантисс
        SumSolver.bi_element1 = new BinaryCode(exp_element1, ExpWordLength);
        SumSolver.bi_element2 = new BinaryCode(exp_element2, ExpWordLength);
        SumSolver.bi_result = new BinaryCode(0, ExpWordLength);
        SumSolver.element1 = Integer.parseInt(exp_element1.toString(),2);
        SumSolver.element2 = Integer.parseInt(exp_element2.toString(),2);
        SumSolver.WordLength = ExpWordLength;
        SumSolver.execBinaryDiff();

        int num_of_shifts = (int) SumSolver.result;

        if (num_of_shifts > 0) {
            exp_element2 = exp_element1;
            bi_element2 = bi_element2.leaveCertainDigits(bi_element2.findUnsignedSignifPartSize() - num_of_shifts);
        } else {
            exp_element1 = exp_element2;
            bi_element1 = bi_element1.leaveCertainDigits(bi_element1.findUnsignedSignifPartSize() - num_of_shifts);
        }
    }

}
