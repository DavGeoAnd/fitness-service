package com.davgeoand.api.model;

import com.arangodb.serde.jackson.Id;
import com.arangodb.serde.jackson.Key;
import com.arangodb.serde.jackson.Rev;
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
@JsonSubTypes({})
public class User {
    @Id
    protected String id;
    @Key
    protected String key;
    @Rev
    protected String rev;
    protected String name;
}
