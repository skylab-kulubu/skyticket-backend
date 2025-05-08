package com.skylab.skyticket.entities;

import lombok.Builder;
import java.text.Normalizer;

public enum Option {

    DEADPOOL("Deadpool"),
    YODA("Yoda"),
    ADVENTURE_TIME("Adventure Time (Finn, Köpek Jake)"),
    FINN("Finn"),
    JAKE("Köpek Jake"),
    SPIDERMAN("Spiderman"),
    RICK("Rick"),
    HARLEY_QUINN("Harley Quinn"),
    GROOT("Groot (Guardians of Galaxy)"),
    KIM_POSSIBLE("Kim Possible"),
    SUNGER_BOB("Sünger Bob"),
    RIGBY_VE_MORDECAI("Rigby ve Mordecai"),
    WONDER_WOMAN("Wonder Woman"),
    SCOOBY_DOO("Scooby Doo"),
    HARRY_POTTER("Harry Potter"),
    BATMAN("Batman"),
    IRON_MAN("Iron Man"),
    JOKER("Joker"),
    PRENSES_CIKLET("Prenses Ciklet"),
    WOODY("Woody"),
    THANOS("Thanos"),
    GWEN_STACY("Gwen Stacy"),
    SUPERMAN("Superman"),
    WOLVERINE("Wolverine"),
    HULK("Hulk"),
    ROGUE("Rogue"),

    YESIL("Yağmur ormanlarının içinde yemyeşil ağaçları"),
    MAVI("Sonsuz bir okyanus mavisini"),
    KIRMIZI("Bir yanardağının patladığı anı"),
    MOR("Lavanta ve mor lalelerden bir çiçek bahçesini"),

    GECENIN_YILDIZI("Gecenin Yıldızı"),
    SKYDAYS("Skydays"),
    YILDIZJAM("Yıldız Jam");

    private final String description;

    Option(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Option fromDescription(String description) {
        String normalizedDescription = normalizeString(description).trim();
        for (Option option : Option.values()) {
            String normalizedOptionDescription = normalizeString(option.getDescription()).trim();
            if (normalizedOptionDescription.equalsIgnoreCase(normalizedDescription)) {
                return option;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }

    private static String normalizeString(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFKD);
        normalized = normalized.replaceAll("\\p{M}", "");
        normalized = normalized.replaceAll("\\s+", " ");
        return normalized;
    }

}
