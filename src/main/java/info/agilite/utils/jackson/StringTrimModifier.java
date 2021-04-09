package info.agilite.utils.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import info.agilite.utils.StringUtils;

public class StringTrimModifier {
	public static SimpleModule createTrimModule() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(String.class, new JsonSerializer<String>() {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				if (value != null) {
					value = value.trim();
				}
				if(value.length() == 0) {
					gen.writeString((String)null);
				}else {
					gen.writeString(value);
				}
			}
		});

		module.addDeserializer(String.class, new JsonDeserializer<String>() {
			@Override
			public String deserialize(JsonParser p, DeserializationContext ctxt)throws IOException, JsonProcessingException {
				String receivedValue = p.getText();
				if (StringUtils.isNullOrEmpty(receivedValue)) {
					return null;
				}else {
					return receivedValue.trim();
				}
			}
		});

		return module;
	}
}
