package info.agilite.utils.jackson;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;


public class FilteredSerializedModifier extends BeanSerializerModifier {
    private Predicate<String> noAttFilter;

	public FilteredSerializedModifier(Predicate<String> noAttFilter) {
		super();
		this.noAttFilter = noAttFilter;
	}

	@SuppressWarnings("serial")
	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
		return beanProperties.stream().map(bpw -> new BeanPropertyWriter(bpw) {
			@Override
			public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
				if(noAttFilter.test(super._name.toString())) {
					return;
				}else {
					super.serializeAsField(bean, gen, prov);
				}
			}
		}).collect(Collectors.toList());
	}
}