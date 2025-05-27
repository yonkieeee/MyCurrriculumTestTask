package org.example.mycurrriculumtesttask.dto;

import lombok.Data;

@Data
public class ModuleRequestDTO {
    private String name;
    private String description;
    private Long subjectId;
}