package com.example.smartpantrymanager;

public class RecipeMatcher {


    // Makes ingredient names easier to compare
    public static String normalizeIngredientName(
            String name
    ) {

        if (name == null) {
            return "";
        }

        String normalized =
                name.trim().toLowerCase();


        // Handle some common plural forms
        if (normalized.equals("eggs")) {
            return "egg";
        }

        if (normalized.equals("tomatoes")) {
            return "tomato";
        }

        if (normalized.equals("potatoes")) {
            return "potato";
        }

        if (normalized.equals("bananas")) {
            return "banana";
        }

        if (normalized.equals("apples")) {
            return "apple";
        }


        return normalized;
    }


    // Makes unit names consistent
    public static String normalizeUnit(
            String unit
    ) {

        if (unit == null) {
            return "";
        }

        String normalized =
                unit.trim().toLowerCase();


        // Weight
        if (normalized.equals("grams")
                || normalized.equals("gram")) {

            return "g";
        }


        if (normalized.equals("kilograms")
                || normalized.equals("kilogram")
                || normalized.equals("kgs")) {

            return "kg";
        }


        // Liquid
        if (normalized.equals("millilitres")
                || normalized.equals("millilitre")
                || normalized.equals("milliliters")
                || normalized.equals("milliliter")) {

            return "ml";
        }


        if (normalized.equals("litres")
                || normalized.equals("litre")
                || normalized.equals("liters")
                || normalized.equals("liter")) {

            return "l";
        }


        // Individual items
        if (normalized.equals("piece")
                || normalized.equals("pieces")
                || normalized.equals("item")
                || normalized.equals("items")
                || normalized.equals("whole")) {

            return "piece";
        }


        // Bread slices
        if (normalized.equals("slice")
                || normalized.equals("slices")) {

            return "slice";
        }


        return normalized;
    }


    // Check whether two units can be compared
    public static boolean areUnitsCompatible(
            String firstUnit,
            String secondUnit
    ) {

        String first =
                normalizeUnit(firstUnit);

        String second =
                normalizeUnit(secondUnit);


        // Exact same unit
        if (first.equals(second)) {
            return true;
        }


        // Grams and kilograms
        if ((first.equals("g")
                || first.equals("kg"))
                &&
                (second.equals("g")
                        || second.equals("kg"))) {

            return true;
        }


        // Millilitres and litres
        if ((first.equals("ml")
                || first.equals("l"))
                &&
                (second.equals("ml")
                        || second.equals("l"))) {

            return true;
        }


        return false;
    }


    // Convert quantity to a common base unit
    public static double convertToBaseQuantity(
            double quantity,
            String unit
    ) {

        String normalizedUnit =
                normalizeUnit(unit);


        // kg becomes grams
        if (normalizedUnit.equals("kg")) {

            return quantity * 1000;
        }


        // litre becomes millilitres
        if (normalizedUnit.equals("l")) {

            return quantity * 1000;
        }


        // g, ml, piece and slice stay unchanged
        return quantity;
    }


    // Compare pantry quantity with recipe quantity
    public static boolean hasEnoughQuantity(
            double pantryQuantity,
            String pantryUnit,
            double requiredQuantity,
            String requiredUnit
    ) {

        if (!areUnitsCompatible(
                pantryUnit,
                requiredUnit
        )) {

            return false;
        }


        double available =
                convertToBaseQuantity(
                        pantryQuantity,
                        pantryUnit
                );


        double required =
                convertToBaseQuantity(
                        requiredQuantity,
                        requiredUnit
                );


        return available >= required;
    }
}