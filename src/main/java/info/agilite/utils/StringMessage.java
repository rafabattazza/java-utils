package info.agilite.utils;

import java.util.function.Function;

public class StringMessage {
	private Function<Object, String> converter = StringUtils::formatByClass;
	private String text = "";
	private Object[] params;
	
	public static StringMessage create(String text) {
		return new StringMessage(text);
	}
	
	private StringMessage(String text) {
		super();
		this.text = text;
	}

	public StringMessage converter(Function<Object, String> converter) {
		this.converter = converter;
		return this;
	}
	
	public StringMessage text(String text) {
		this.text = text;
		return this;
	}
	
	public StringMessage params(Object ... params) {
		this.params = params;
		return this;
	}

	public String get() {
		if(params != null && params.length > 0) {
			StringBuilder build = new StringBuilder(text);
			for(int i=0; i < params.length; i++) {
				Object val = params[i];
				String format;
				try {
					format = val == null ? "" : converter.apply(val);
				} catch (Exception e) {
					throw new RuntimeException("Format JSon Objetc error", e);
				}

				build.replace(build.indexOf("{}"), build.indexOf("{}")+2, format);
			}

			return build.toString();
		}
		return text;
	}
}
