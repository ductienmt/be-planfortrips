package com.be_planfortrips.mappers.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

@Component
public class JsonObjectMapper {
    public ResponseEntity<Map<String, Object>> getResponse(JsonObject jsonObject) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> map = gson.fromJson(jsonObject, type);

        return ResponseEntity.ok(map);
    }
}
