package com.gamevault.model;

import jakarta.persistence.*;

/**
 * Represents a game stored in the application database.
 * Contains both external data (from RAWG) and user-specific library data.
 */
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Game Data
    private String title;
    private String genre;
    private String platform;
    private String coverImageUrl;
    private String releaseDate;
    private Double communityRating;
    private String externalId;

    // Personal / library fields
    @Enumerated(EnumType.STRING)
    private PlayStatus playStatus;

    private Integer personalRating;   // e.g. 0–100

    @Column(length = 2000)
    private String notes;

    public enum PlayStatus {
        WISHLIST,
        BACKLOG,
        PLAYING,
        COMPLETED,
        DROPPED
    }

    /**
     * Gets the unique ID of the game.
     *
     * @return the game ID
     */
    public Long getId() { return id; }

    /**
     * Gets the title of the game.
     *
     * @return the game title
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the game.
     *
     * @param title the game title
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets the genre of the game.
     *
     * @return the genre
     */
    public String getGenre() { return genre; }

    /**
     * Sets the genre of the game.
     *
     * @param genre the genre
     */
    public void setGenre(String genre) { this.genre = genre; }

    /**
     * Gets the platform of the game.
     *
     * @return the platform
     */
    public String getPlatform() { return platform; }

    /**
     * Sets the platform of the game.
     *
     * @param platform the platform
     */
    public void setPlatform(String platform) { this.platform = platform; }

    /**
     * Gets the cover image URL.
     *
     * @return the cover image URL
     */
    public String getCoverImageUrl() { return coverImageUrl; }

    /**
     * Sets the cover image URL.
     *
     * @param coverImageUrl the cover image URL
     */
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    /**
     * Gets the release date of the game.
     *
     * @return the release date
     */
    public String getReleaseDate() { return releaseDate; }

    /**
     * Sets the release date of the game.
     *
     * @param releaseDate the release date
     */
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    /**
     * Gets the community rating.
     *
     * @return the community rating
     */
    public Double getCommunityRating() { return communityRating; }

    /**
     * Sets the community rating.
     *
     * @param communityRating the community rating
     */
    public void setCommunityRating(Double communityRating) { this.communityRating = communityRating; }

    /**
     * Gets the external ID.
     *
     * @return the external ID
     */
    public String getExternalId() { return externalId; }

    /**
     * Sets the external ID.
     *
     * @param externalId the external ID
     */
    public void setExternalId(String externalId) { this.externalId = externalId; }

    /**
     * Gets the play status.
     *
     * @return the play status
     */
    public PlayStatus getPlayStatus() { return playStatus; }

    /**
     * Sets the play status.
     *
     * @param playStatus the play status
     */
    public void setPlayStatus(PlayStatus playStatus) { this.playStatus = playStatus; }

    /**
     * Gets the personal rating.
     *
     * @return the personal rating
     */
    public Integer getPersonalRating() { return personalRating; }

    /**
     * Sets the personal rating.
     *
     * @param personalRating the personal rating
     */
    public void setPersonalRating(Integer personalRating) { this.personalRating = personalRating; }

    /**
     * Gets the notes for the game.
     *
     * @return the notes
     */
    public String getNotes() { return notes; }

    /**
     * Sets the notes for the game.
     *
     * @param notes the notes
     */
    public void setNotes(String notes) { this.notes = notes; }
}