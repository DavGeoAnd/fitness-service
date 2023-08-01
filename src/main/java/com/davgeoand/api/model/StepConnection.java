package com.davgeoand.api.model;

import com.arangodb.serde.jackson.*;
import com.davgeoand.api.model.stepConnection.OwnStep;
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
@JsonSubTypes({@JsonSubTypes.Type(value = OwnStep.class, name = "OwnStep")})
public class StepConnection {
    @Id
    protected String id;
    @Key
    protected String key;
    @Rev
    protected String rev;
    @From
    protected String user;
    @To
    protected String step;
}
