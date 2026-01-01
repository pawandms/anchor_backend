package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
    private String question;
    private List<PollOption> options;
    private Boolean allowMultipleAnswers;
    private Integer totalVotes;
    private String createdBy;
    private Date expiresAt;
}
