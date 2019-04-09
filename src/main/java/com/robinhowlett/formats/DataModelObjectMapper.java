package com.robinhowlett.formats;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.robinhowlett.mixins.LinkMixin;
import com.robinhowlett.ser.SimpleLocalDateDeserializer;
import com.robinhowlett.ser.SimpleLocalDateSerializer;

import org.springframework.hateoas.Link;

import java.time.LocalDate;

public class DataModelObjectMapper extends ObjectMapper {
    public DataModelObjectMapper() {
        super();

        // adds JDK 8 Parameter Name access for cleaner JSON-to-Object mapping
        registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        SimpleModule simpleLocalDateModule = new SimpleModule();
        simpleLocalDateModule.addSerializer(LocalDate.class, new SimpleLocalDateSerializer());
        simpleLocalDateModule.addDeserializer(LocalDate.class, new SimpleLocalDateDeserializer());
        registerModule(simpleLocalDateModule);

        // support HATEOS-style links
        addMixIn(Link.class, LinkMixin.class);
    }
}
