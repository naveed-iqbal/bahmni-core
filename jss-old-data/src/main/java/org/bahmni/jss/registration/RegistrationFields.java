package org.bahmni.jss.registration;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bahmni.datamigration.request.patient.Name;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.StringTokenizer;

public class RegistrationFields {
    private static final String patternWhenYearSpecifiedAs4Digits = "dd/MM/yyyy";
    private static final String patternWhenYearSpecifiedAs2Digits = "dd/MM/yy";

    public static String getDate(String s) {
        StringTokenizer stringTokenizer = new StringTokenizer(s.trim(), " ");
        if (!stringTokenizer.hasMoreTokens()) return null;
        String datePart = stringTokenizer.nextToken();
        String pattern = datePart.length() == 8 ? patternWhenYearSpecifiedAs2Digits : patternWhenYearSpecifiedAs4Digits;
        LocalDate localDate = LocalDateTime.parse(datePart, DateTimeFormat.forPattern(pattern)).toLocalDate();
        if (localDate.getYear() <= 1900) {
            localDate = new LocalDate(1900 + localDate.getYearOfCentury(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
        }
        return localDate.toString("dd-MM-yyyy");
    }

    public static String sentenceCase(String s) {
        return WordUtils.capitalizeFully(s);
    }

    public static RegistrationNumber parseRegistrationNumber(String registrationNumber) {
        StringTokenizer stringTokenizer = new StringTokenizer(registrationNumber, "/");
        String id = stringTokenizer.nextToken();
        String centerCode = stringTokenizer.nextToken();
        return new RegistrationNumber(centerCode, id);
    }

    public static Name name(String firstName, String lastName) {
        String[] splitFirstNames = StringUtils.split(firstName, " ");

        Name name = new Name();
        if (StringUtils.isEmpty(lastName) && splitFirstNames.length > 1) {
            Object[] splitFirstNamesExceptLastWord = ArrayUtils.remove(splitFirstNames, splitFirstNames.length - 1);
            name.setGivenName(StringUtils.join(splitFirstNamesExceptLastWord, " "));
            name.setFamilyName(splitFirstNames[splitFirstNames.length - 1]);
        } else {
            name.setGivenName(firstName);
            name.setFamilyName(StringUtils.isEmpty(lastName) ? "." : lastName);
        }
        return name;
    }

    public static int getAge(String fieldValue) {
        double doubleValue;
        try {
            doubleValue = Double.parseDouble(fieldValue);
        } catch (NumberFormatException e) {
            return 0;
        }
        return (int) Math.round(doubleValue);
    }
}