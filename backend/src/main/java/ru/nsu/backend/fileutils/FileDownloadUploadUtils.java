package ru.nsu.backend.fileutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.logging.log4j.message.MapMessage;
import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import ru.nsu.backend.person.People;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FileDownloadUploadUtils {
    private static final ObjectMapper XML_MAPPER = new XmlMapper();
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        XML_MAPPER.setDateFormat(simpleDateFormat);
        JSON_MAPPER.setDateFormat(simpleDateFormat);
    }

    public static String serialize(People people, MapMessage.MapFormat format) {
        return format.equals(JSON) ? serialize(people, JSON_MAPPER) : serialize(people, XML_MAPPER);
    }

    private static String serialize(People people, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(people);
        } catch (IOException e) {
            return null;
        }
    }

    public static People deserialize(String people, MapMessage.MapFormat format){
        return format.equals(JSON) ? deserialize(people, JSON_MAPPER) : deserialize(people, XML_MAPPER);
    }

    private static People deserialize(String people, ObjectMapper objectMapper){
        try {
            return objectMapper.readValue(people, People.class);
        } catch (JsonProcessingException e){
            return null;
        }
    }
}
