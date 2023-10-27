package project.cms.cafemanagementsystem.utils;

import java.util.Random;

public class RandomPasswordUtils {
    public String generatePassword(String pattern, int minLength, int maxLength) {
        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder password = new StringBuilder();

        while (password.length() < length) {
            char ch = pattern.charAt(random.nextInt(pattern.length()));
            if (isValidCharacter(ch, password.toString(), pattern)) {
                password.append(ch);
            }
        }
        return password.toString();
    }

    public boolean isValidCharacter(char ch, String password, String pattern) {
        if (password.contains(String.valueOf(ch))) {
            return false;
        }
        if (pattern.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]).{8,25}$")) {
            if (Character.isUpperCase(ch) && !pattern.contains("[A-Z]")) {
                return false;
            }
            if (Character.isLowerCase(ch) && !pattern.contains("[a-z]")) {
                return false;
            }
            if (Character.isDigit(ch) && !pattern.contains("[0-9]")) {
                return false;
            }
            if (isSpecialCharacter(ch) && !pattern.contains("[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]")) {
                return false;
            }
        }

        return true;
    }

    public boolean isSpecialCharacter(char ch) {
        String specialCharacters = " !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~";
        return specialCharacters.contains(String.valueOf(ch));
    }

}
