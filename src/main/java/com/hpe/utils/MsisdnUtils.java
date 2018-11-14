package com.hpe.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * The helper wraps a few functions to validate and extract certain information from a (possible) msisdn.
 * <p>
 * <b>Important note:</b>
 * It uses the phonenumbers lib in order to achieve its goals and makes a few assumptions in the process
 * as it takes a simplistic view on the format of the numbers.
 * More analysis of the lib and on the msisdn format is required before making it bullet-proof.
 */
public class MsisdnUtils {

    public static boolean isValid(long possibleMsisdn) {
        Result<Phonenumber.PhoneNumber> numberResult = getPossiblePhoneNumber(possibleMsisdn);
        if (numberResult.isError()) {
            return false;
        }
        return PhoneNumberUtil.getInstance().isValidNumber(numberResult.getValue());
    }

    /**
     * Precondition: the msisdn is valid.
     */
    public static long getCountryCode(long msisdn) {
        Result<Phonenumber.PhoneNumber> numberResult = getPossiblePhoneNumber(msisdn);
        if (numberResult.isError()) {
            return 0;
        }
        return numberResult.getValue().getCountryCode();
    }

    /**
     * We assume the number is international hence we prepend a '+' to check its validity.
     */
    private static Result<Phonenumber.PhoneNumber> getPossiblePhoneNumber(long possibleMsisdn) {
        try {
            return Result.ok(PhoneNumberUtil.getInstance().parse(String.format("+%s", possibleMsisdn), ""));
        } catch (NumberParseException e) {
            return Result.error("");
        }
    }
}
