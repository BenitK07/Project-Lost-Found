package com.campus.lostfound.dto;

public class MatchResponseDTO {

    private Long matchId;
    private int score;
    private String status;
    private String lostTitle;
    private String foundTitle;

    public MatchResponseDTO(
            Long matchId,
            int score,
            String status,
            String lostTitle,
            String foundTitle
    ) {
        this.matchId = matchId;
        this.score = score;
        this.status = status;
        this.lostTitle = lostTitle;
        this.foundTitle = foundTitle;
    }

    public Long getMatchId() { return matchId; }
    public int getScore() { return score; }
    public String getStatus() { return status; }
    public String getLostTitle() { return lostTitle; }
    public String getFoundTitle() { return foundTitle; }
}
