package me.dong.exwindowmanager.util;


public class PhoneNumberUtils {

    public static String formatNumber(String phoneNumber) {
        StringBuilder sbPhoneNumber = new StringBuilder();

        if (phoneNumber.length() == 11){
            sbPhoneNumber.append(phoneNumber.subSequence(0,3))
                    .append("-")
                    .append(phoneNumber.subSequence(3,7))
                    .append("-")
                    .append(phoneNumber.subSequence(7,phoneNumber.length()));
        }else if(phoneNumber.length() == 10){
            sbPhoneNumber.append(phoneNumber.subSequence(0,3))
                    .append("-")
                    .append(phoneNumber.subSequence(3,6))
                    .append("-")
                    .append(phoneNumber.subSequence(6,phoneNumber.length()));
        }else if(phoneNumber.length() == 9){
            sbPhoneNumber.append(phoneNumber.subSequence(0,2))
                    .append("-")
                    .append(phoneNumber.subSequence(2,5))
                    .append("-")
                    .append(phoneNumber.subSequence(5,phoneNumber.length()));
        }

        return sbPhoneNumber.toString();
    }
}
