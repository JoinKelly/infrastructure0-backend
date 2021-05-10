package com.infrastructure.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

public class ObjectMapperUtils {

    public static <T> T applyPatch(ObjectMapper objectMapper, JsonPatch patch, T targetItem, Class<T> tClass) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetItem, JsonNode.class));
        return objectMapper.treeToValue(patched, tClass);
    }
}
