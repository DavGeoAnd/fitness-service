package com.davgeoand.api.model;

import com.arangodb.serde.jackson.Id;
import com.arangodb.serde.jackson.Key;
import com.arangodb.serde.jackson.Rev;
import com.davgeoand.api.model.step.RepExercise;
import com.davgeoand.api.model.step.Rest;
import com.davgeoand.api.model.step.TimeExercise;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = RepExercise.class, name = "RepExercise"),
        @JsonSubTypes.Type(value = TimeExercise.class, name = "TimeExercise"),
        @JsonSubTypes.Type(value = Rest.class, name = "Rest")})
public class Step {
    @Id
    protected String id;
    @Key
    protected String key;
    @Rev
    protected String rev;
    protected String name;
}
