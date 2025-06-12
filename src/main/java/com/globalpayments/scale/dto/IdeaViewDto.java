package com.globalpayments.scale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdeaViewDto {
    private String ideaViewsId;
    private String userId;
    private String ideaId;
    private LocalDateTime viewedAt;
}
