import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Введите через пробел два операнда в римской или арабской системе счисления " +
                "в диапазоне от 1 до 10 включительно и один оператор (+, -, /, *) между ними");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        try {
            String result = calc(input);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String calc(String input) throws Exception {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new Exception("Формат математической операции не удовлетворяет заданию");
        }

        int num1;
        int num2;
        boolean isRoman = false;
        if (parts[0].matches("[IVXLCDM]+") && parts[2].matches("[IVXLCDM]+")) {
            isRoman = true;
            num1 = romanToArabic(parts[0]);
            num2 = romanToArabic(parts[2]);
        } else if (parts[0].matches("\\d+") && parts[2].matches("\\d+")) {
            num1 = Integer.parseInt(parts[0]);
            num2 = Integer.parseInt(parts[2]);
        } else {
            throw new Exception("Операнды должны быть римскими или арабскими цифрами");
        }

        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new Exception("Операнды должны быть в диапазоне от 1 до 10 включительно");
        }

        int result = switch (parts[1]) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "/" -> num1 / num2;
            case "*" -> num1 * num2;
            default -> throw new Exception("Недопустимый оператор");
        };

        if (isRoman) {
            if (result < 1) {
                throw new Exception("В римской системе нет отрицательных чисел");
            }
            return arabicToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static int romanToArabic(String roman) {
        Map<Character, Integer> numbers = Map.of(
                'I', 1, 'V', 5, 'X', 10,
                'L', 50, 'C', 100, 'D', 500, 'M', 1000);

        int result = 0;
        int prev = 0;
        for (int i = 0; i < roman.length(); i++) {
            char ch = roman.charAt(i);
            int curr = numbers.get(ch);
            if (curr > prev) {
                result += curr - 2 * prev;
            } else {
                result += curr;
            }
            prev = curr;
        }
        return result;
    }

    private static String arabicToRoman(int number) {
        List<RomanNumber> romanNumbers = Arrays.asList(
                new RomanNumber(100, "C"),
                new RomanNumber(90, "XC"),
                new RomanNumber(50, "L"),
                new RomanNumber(40, "XL"),
                new RomanNumber(10, "X"),
                new RomanNumber(9, "IX"),
                new RomanNumber(5, "V"),
                new RomanNumber(4, "IV"),
                new RomanNumber(1, "I")
        );
        StringBuilder roman = new StringBuilder();

        for (RomanNumber num : romanNumbers) {
            while (number >= num.arab) {
                number -= num.arab;
                roman.append(num.roman);
            }
        }
        return roman.toString();
    }

    private static class RomanNumber {
        int arab;
        String roman;

        RomanNumber(int arab, String roman) {
            this.arab = arab;
            this.roman = roman;
        }
    }
}
