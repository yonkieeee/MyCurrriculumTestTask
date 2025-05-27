package org.example.mycurrriculumtesttask.dto;

import lombok.Data;

@Data
public class LessonRequestDTO {
    private String name;
    private String description;
    private Long moduletId;
}
