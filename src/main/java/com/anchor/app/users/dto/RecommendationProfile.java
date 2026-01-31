package com.anchor.app.users.dto;

import com.anchor.app.dto.BaseVo;
import com.anchor.app.users.model.UserRecommendation;
import lombok.*;

import java.util.List;

/**
 * DTO for recommended friends list, following the project's BaseVo pattern.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecommendationProfile extends BaseVo {
    private String id; // The user ID for whom recommendations are generated
    private List<UserRecommendation> recommendations;
}
