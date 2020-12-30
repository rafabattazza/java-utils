package info.agilite.utils;


import java.util.function.Supplier;

public class NoNullPointer<R> {
	private final Supplier<R> supplier;
	private NoNullPointer(Supplier<R> supplier) {
		this.supplier = supplier;
	}
	public static<R> NoNullPointer<R> of(Supplier<R> supplier) {
		return new NoNullPointer<>(supplier);
	}
	public R get() {
		if(supplier == null)return null;
		try {
			return supplier.get();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
