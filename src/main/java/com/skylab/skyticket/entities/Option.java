package com.skylab.skyticket.entities;

import lombok.Builder;

public enum Option {

    DEADPOOL("Deadpool"),
    YODA("Yoda"),
    ADVENTURE_TIME("Adventure Time (Finn, Köpek Jake)"),
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

    YESIL("Yağmur ormanlarının içinde yemyeşil ağaçları"),
    MAVI("Sonsuz bir okyanus mavisini"),
    KIRMIZI("Bir yanardağının patladığı anı"),
    MOR("Lavanta ve mor lalelerden bir çiçek bahçesini");

    private final String description;

    Option(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Option fromDescription(String description) {
        for (Option option : Option.values()) {
            if (option.getDescription().equalsIgnoreCase(description.trim())) {
                return option;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }

}
