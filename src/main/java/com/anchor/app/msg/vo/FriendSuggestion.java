package com.anchor.app.msg.vo;

public class FriendSuggestion {
    
    private Long userId;
    private int mutualConnectionCount;
    
    // Additional fields (populated from User)
    private String firstName;
    private String lastName;
    private String profileImage;
    private String userName;
    
    public FriendSuggestion() {}
    
    public FriendSuggestion(Long userId, int mutualConnectionCount) {
        this.userId = userId;
        this.mutualConnectionCount = mutualConnectionCount;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public int getMutualConnectionCount() {
        return mutualConnectionCount;
    }
    
    public void setMutualConnectionCount(int mutualConnectionCount) {
        this.mutualConnectionCount = mutualConnectionCount;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
