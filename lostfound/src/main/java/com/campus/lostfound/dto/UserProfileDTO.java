package com.campus.lostfound.dto;

public class UserProfileDTO {

    private String name;
    private String email; // null for public profile

    private int lostItemsCount;
    private int foundItemsCount;
    private int confirmedMatches;

    public UserProfileDTO(
            String name,
            String email,
            int lostItemsCount,
            int foundItemsCount,
            int confirmedMatches
    ) {
        this.name = name;
        this.email = email;
        this.lostItemsCount = lostItemsCount;
        this.foundItemsCount = foundItemsCount;
        this.confirmedMatches = confirmedMatches;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getLostItemsCount() { return lostItemsCount; }
    public int getFoundItemsCount() { return foundItemsCount; }
    public int getConfirmedMatches() { return confirmedMatches; }
}
