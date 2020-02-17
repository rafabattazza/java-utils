package info.agilite.utils.jackson;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSonMapperCreator {
	private static ObjectMapper jsonMapper;

	public static ObjectMapper create() {
		if(jsonMapper == null) {
			ObjectMapper objectMapper = new ObjectMapper();
			config(objectMapper);
			jsonMapper = objectMapper;
		}

		return jsonMapper;
	}

	private static void config(ObjectMapper objectMapper) {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	
}
