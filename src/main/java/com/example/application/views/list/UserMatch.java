package com.example.application.views.list;

import jakarta.persistence.*;

@Entity
@Table(name = "user_matches")
public class UserMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserForm user; // The user who is getting the recommendation

    @ManyToOne
    @JoinColumn(name = "recommended_user_id")
    private UserForm recommendedUser; // The recommended user

    private Integer score;

    public UserMatch() {}

    // Updated constructor to include recommendedUser
    public UserMatch(UserForm user, UserForm recommendedUser, Integer score) {
        this.user = user;
        this.recommendedUser = recommendedUser;
        this.score = score;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserForm getUser() {
        return user;
    }

    public void setUser(UserForm user) {
        this.user = user;
    }

    public UserForm getRecommendedUser() {
        return recommendedUser; // Getter for the recommendedUser
    }

    public void setRecommendedUser(UserForm recommendedUser) {
        this.recommendedUser = recommendedUser; // Setter for the recommendedUser
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
