package org.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JasonObjectConverter {

    public static String convertJasonObjectToString(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder sb = new StringBuilder();
        try{
            sb.append(mapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            sb.append(e);
        }
        return sb.toString();
    }
}
