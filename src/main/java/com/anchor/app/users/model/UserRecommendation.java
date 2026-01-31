package com.anchor.app.users.model;

import com.anchor.app.oauth.enums.VisibilityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Stores recommended friends for a user.
 * This is a derived collection used for quick retrieval of suggestions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_recommendations")
public class UserRecommendation {

    @Id
    private String id;

    private String userId; // The user who receives the recommendation
    private String recommendedUserId; // The user being recommended

    private String firstName;
    private String lastName;
    private String face; // Avatar image ID/URL
    private String description; // Mutual info or signature

    private List<String> mutualFriendIds;
    private List<String> mutualChannelIds; // Represents "circles" or groups

    private VisibilityType profileType;

    private Double score; // Priority/Relevance score

    private Date createdAt;
}
