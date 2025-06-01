package lb.utils;

public class VarHolder<T> {

	T value;
	
	public VarHolder(T value) {
		this.value = value;
	}
	
	public synchronized void setValue(T vlaue) {
		this.value = vlaue;
	}
	
	public synchronized T getValue() {
		return value;
	}
}
