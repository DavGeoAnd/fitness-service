package com.davgeoand.api.model.step;

import com.davgeoand.api.model.Step;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RepExercise extends Step {
    private String mainMuscle;
    private String equipment;
    private String exerciseType;
    private String skillLevel;
    private String instructions;
}
