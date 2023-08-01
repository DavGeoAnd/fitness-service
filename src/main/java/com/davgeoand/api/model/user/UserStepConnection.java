package com.davgeoand.api.model.user;

import com.davgeoand.api.model.Step;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStepConnection {
    private String type;
    private String key;
    private Step step;
}
