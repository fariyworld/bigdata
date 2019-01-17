package topology.entity;

public class Person {

	private String name;
	
	public static double L0;
	
	public Person(String name) {
		super();
		this.name = name;
	}
	
	public Person() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Person [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static double getL0() {
		return L0;
	}

	public static void setL0(double l0) {
		L0 = l0;
	}
	
}
