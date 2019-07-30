package constants;

public class Constants {
    public static final String PHONE_NUMBER_REGEX = "^\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})$";
    public static final String BLANK_VALUE_REGEX = "^\\s*$";
    public static final String EMAIL_REGEX = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s" +
                                             "@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}" +
                                             "\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    public static final String CAR_REGISTRATION_NUMBER_REGEX = "^\\w{1,2}\\s\\d{4}\\s\\w{2}$";
    public static final String NUMBER_REGEX = "^\\d+$";
    public static final String USERNAME_REGEX = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public static final String NAME_REGEX = "^([A-Z][a-z]{1,15})$";
    public static final String EGN_REGEX = "^\\d{10}$";
    public static final String CREDIT_CARD_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|" +
                                                   "6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}" +
                                                   "|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
    public static final String CAR_BRAND_REGEX = "^[A-Z][a-z][^#&<>\\\"~;$^%{}?]{1,20}$";
    public static final String CAR_MODEL_REGEX = "^[A-Z][A-Za-z0-9][^#&<>\\\"~;$^%{}?]{1,20}$";
    public static final String FLOATING_POINT_NUMBER_REGEX = "^\\d+(\\.\\d+)*$";
    public static final double MIN_SALARY = 400;
}
