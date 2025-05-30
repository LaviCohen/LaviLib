package le.utils;

public class VarHolder<T> {

	T value;
	
	public VarHolder(T value) {
		this.value = value;
	}
	
	public void setValue(T vlaue) {
		this.value = vlaue;
	}
	
	public T getValue() {
		return value;
	}
}
