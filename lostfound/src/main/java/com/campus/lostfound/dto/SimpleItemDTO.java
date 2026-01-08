package com.campus.lostfound.dto;

public class SimpleItemDTO {

    private Long id;
    private String title;
    private String category;
    private String location;

    public SimpleItemDTO(Long id, String title, String category, String location) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.location = location;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
}