package com.example.freelance_project_management_platform.data.utils;

import com.example.freelance_project_management_platform.business.service.exceptions.UserInfoException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Utils {

    public static void validatePassword(String password) {
        String password_pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$";
        Pattern pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new UserInfoException(
                    "Invalid password. " +
                            "The password must have " +
                            "a minimum of 8 characters, " +
                            "1 lower case character, " +
                            "1 upper case character, " +
                            "1 number and 1 special character");
        }
    }

    /**
     * This method will match EMail with the given regex
     *
     * @param emailAddress  email address
     * @param regexPattern  regex pattern
     * @return  boolean
     */
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}
