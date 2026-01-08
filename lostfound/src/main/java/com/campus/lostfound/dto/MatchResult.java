package com.campus.lostfound.dto;

public class MatchResult<T> {

    private Long matchId;
    private T item;
    private int score;
    private String status;

    public MatchResult(Long matchId, T item, int score, String status) {
        this.matchId = matchId;
        this.item = item;
        this.score = score;
        this.status = status;
    }

    public Long getMatchId() {
        return matchId;
    }

    public T getItem() {
        return item;
    }

    public int getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }
}
