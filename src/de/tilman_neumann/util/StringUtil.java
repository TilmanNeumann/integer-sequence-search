package de.tilman_neumann.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil {
	private StringUtil() {
		// static class
	}
	
    /**
     * Creates a string from n repeats of the given substring.
     *
     * @param substr substring to repeat
     * @param n number of repetitions
     * @return n repetitions of the substring, or null if n negative
     */
    public static String repeat(String substr, int n) {
        return StringUtils.repeat(substr, n);
    }

    /**
     * Schneidet Text auf maximal Maskenlänge.
     *
     * @param text Zu behandelnder Text
     * @param mask Maske
     * @return Text, wenn zu lang bei Maskenlaenge abgeschnitten
     */
    public static String cutToMask(String text, String mask) {
        // Sichergehen dass Text nicht null ist und Länge bestimmen:
        if (text == null) {
            text = "";
        }
        int textLen = text.length();
        // Evtl. Text auf Maskenlänge als maximale Länge kürzen:
        int maskLen = (mask != null) ? mask.length() : 0;
        if (textLen > maskLen) {
            text = text.substring(0, maskLen);
        }
        return text;
    }

    /**
     * Formatiert einen Text, indem dieser LINKSBÜNDIG in die Maske geschrieben wird.
     * Ist der Eingabetext länger als die Maske, wird er unverändert zurückgegeben.
     * Es gehen also keine Informationen verloren, aber dafür wird durch die Länge der
     * Maske nur die minimale Länge des Rückgabetextes definiert.<br><br>
     *
     * Beispiele:<br>formatLeft("abc", "123456") -> "abc456"<br>
     *               formatLeft("abcdef", "123") -> "abcdef"<br>
     *
     * @param text Der zu formatierende Eingabetext
     * @param mask Die Formatierungsmaske
     * @return Linksbündig formatierter Text.
     */
    public static String formatLeft(String text, String mask) {
        // Nehme gesamten Eingabetext:
        String ret = (text != null) ? text : "";
        if (mask != null) {
            int textLen = ret.length();
            int maskLen = mask.length();
            if (textLen < maskLen) {
                // Rest aus Maske auffüllen:
                ret += mask.substring(textLen, maskLen);
            }
        }
        return ret;
    }

    /**
     * Schneidet Text zuerst auf maximal Maskenlänge und formatiert den
     * Rest LINKSBÜNDIG in der Maske.
     *
     * @param text Zu behandelnder Text
     * @param mask Maske
     * @return Ergebnis des linksbuendigen Einfuegens vom Text in die Maske
     */
    public static String cutFormatLeft(String text, String mask) {
        text = cutToMask(text, mask);
        return formatLeft(text, mask);
    }

    /**
     * Formatiert einen Text, indem dieser RECHTSBÜNDIG in die Maske geschrieben wird.
     * Ist der Eingabetext länger als die Maske, wird er unverändert zurückgegeben.
     * Es gehen also keine Informationen verloren, aber dafür wird durch die Länge der
     * Maske nur die minimale Länge des Rückgabetextes definiert.<br><br>
     *
     * Beispiele:<br>formatRight("abc", "123456") -> "123abc"<br>
     *               formatRight("abcdef", "123") -> "abcdef"<br>
     *
     * @param text Der zu formatierende Eingabetext
     * @param mask Die Formatierungsmaske
     * @return Rechtsbündig formatierter Text.
     */
    public static String formatRight(String text, String mask) {
        // Nehme gesamten Eingabetext:
        String ret = (text != null) ? text : "";
        if (mask != null) {
            int textLen = ret.length();
            int maskLen = mask.length();
            if (textLen < maskLen) {
                // Anfang aus Maske nehmen:
                ret = mask.substring(0, maskLen - textLen) + ret;
            }
        }
        return ret;
    }

    /**
     * Schneidet Text zuerst auf maximal Maskenlänge und formatiert den
     * Rest RECHTSBÜNDIG in der Maske.
     *
     * @param text Zu behandelnder Text
     * @param mask Maske
     * @return Ergebnis des rechtsbuendigen Einfuegens vom Text in die Maske
     */
    public static String cutFormatRight(String text, String mask) {
        text = cutToMask(text, mask);
        return formatLeft(text, mask);
    }

}
