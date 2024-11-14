package com.hikmetsuicmez.komsu_connect.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hikmetsuicmez.komsu_connect.request.BusinessOwnerRegisterRequest;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;

import java.io.IOException;

public class RegisterRequestDeserializer extends JsonDeserializer<RegisterRequest> {


    @Override
    public RegisterRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        String role = root.get("role").asText();

        if ("ROLE_BUSINESS_OWNER".equals(role)) {
            return mapper.treeToValue(root, BusinessOwnerRegisterRequest.class);
        } else {
            return mapper.treeToValue(root, RegisterRequest.class);
        }
    }
}
