package com.karbaros.userregistrationusingsqlite;

/**
 * Created by shanu on 21-Feb-17.
 */

public class AppSupport {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static char[] getPassword() {
        char[] array = new char[9];
        for (int i = 0; i<array.length; i++) {
            array[i] = (char) (Math.random()*1000);
        }
        return array;
    }
}
