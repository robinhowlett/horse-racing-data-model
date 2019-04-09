package com.robinhowlett.mixins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public abstract class LinkMixin {
    LinkMixin(@JsonProperty("href") String href, @JsonProperty("rel") String rel) {
    }

    @JsonProperty("href")
    abstract int getHref();

    @JsonProperty("rel")
    abstract int getRel();
}
