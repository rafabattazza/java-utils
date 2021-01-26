package info.agilite.utils.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class StringTrimModifier {
	public static SimpleModule createTrimModule() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(String.class, new JsonSerializer<String>() {
			@Override
			public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
				if (value != null) {
					value = value.trim();
				}
				gen.writeString(value);
			}
		});

		return module;
	}
}
