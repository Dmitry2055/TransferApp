package com.astakhov.util;

import com.astakhov.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterUtils.class);
    private static final ObjectMapper mapper = new JsonMapper();

    public static <T> T toObject(final String json, final Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOG.error("Cannot deserialize JSON provided", e);
            throw new ServiceException("Cannot deserialize JSON", e);
        }
    }

    public static String toJson(final Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("Cannot serialize to JSON", e);
            throw new ServiceException("Cannot serialize to JSON", e);
        }
    }
}
