package com.davgeoand.api.model.step;

import com.davgeoand.api.model.Step;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Rest extends Step {
    private int seconds;
}
