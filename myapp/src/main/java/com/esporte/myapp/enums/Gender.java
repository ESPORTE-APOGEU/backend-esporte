// src/main/java/com/esporte/myapp/enums/Gender.java
package com.esporte.myapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Gender {
    Male, Female, Other;

    @JsonCreator
    public static Gender fromJson(String v) {
        if (v == null) return null;
        String s = v.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "male": case "masculino": case "m": return Male;
            case "female": case "feminino": case "f": return Female;
            case "other": case "outro": case "o": return Other;
            default: throw new IllegalArgumentException("Invalid gender: " + v);
        }
    }

    @JsonValue
    public String toJson() {
        return name(); // "Male", "Female", "Other"
    }
}
