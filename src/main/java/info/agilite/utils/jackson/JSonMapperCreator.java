package info.agilite.utils.jackson;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

public class JSonMapperCreator {
	private static ObjectMapper jsonMapper;

	public static ObjectMapper create() {
		if(jsonMapper == null) {
			ObjectMapper objectMapper = new ObjectMapper();
			config(objectMapper, true);
			jsonMapper = objectMapper;
		}

		return jsonMapper;
	}
	
	public static ObjectMapper createNoHibernate() {
		if(jsonMapper == null) {
			ObjectMapper objectMapper = new ObjectMapper();
			config(objectMapper, false);
			jsonMapper = objectMapper;
		}

		return jsonMapper;
	}
	
	public static ObjectMapper createWithNoAttFilter(Predicate<String> noAttFilter) {
		ObjectMapper objectMapper = new ObjectMapper();
		config(objectMapper, true);

		SimpleModule noIdModule = new SimpleModule();
		noIdModule.setSerializerModifier(new FilteredSerializedModifier(noAttFilter));
		objectMapper.registerModule(noIdModule);
		return objectMapper;
	}

	private static void config(ObjectMapper objectMapper, boolean includeHibernate) {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
		objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		if(includeHibernate) {
			Hibernate5Module hibernateModule = new Hibernate5Module();
			hibernateModule.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
			objectMapper.registerModule(hibernateModule);
		}
		
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(StringTrimModifier.createTrimModule());
		objectMapper.registerModule(createModuleToORM("dd/MM/yyyy", "dd/MM/yyyy HH:mm", "HH:mm"));
	}
	
	private static SimpleModule createModuleToORM(String dateFmt, String dateTimeFmt, String timeFmt) {
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFmt)));
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFmt)));

		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFmt)));
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFmt)));

		module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFmt)));
		module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFmt)));
		
		return module;
	}
}
