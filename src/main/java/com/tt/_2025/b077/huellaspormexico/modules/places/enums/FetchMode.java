package com.tt._2025.b077.huellaspormexico.modules.places.enums;

public enum FetchMode {

    LIGHT("place_id,name,rating,formatted_address,geometry,types,photos"),
    FULL("name,rating,formatted_address,geometry,url,website,formatted_phone_number,types,editorial_summary,opening_hours,reviews,photos");

    private final String fields;

    FetchMode(String fields) {
        this.fields = fields;
    }

    public String getFields() {
        return fields;
    }
}