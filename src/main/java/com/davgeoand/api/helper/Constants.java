package com.davgeoand.api.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String BASE_PATH_PARAM = "key";
    public static final String BASE_PATH_PARAM_ENDPOINT = "{" + BASE_PATH_PARAM + "}";
}
