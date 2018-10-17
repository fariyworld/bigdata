package business.entity;

import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 6780205443093253322L;

	private String name;
	private Integer age;
	private String info;

	public Person() {
		name = "";
		age = 0;
		info = "";
	}

	public Person(String name, Integer age, String info) {
		super();
		this.name = name;
		this.age = age;
		this.info = info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(",");
		builder.append(age);
		builder.append(",");
		builder.append(info);
		return builder.toString();
	}

	public Person stringTo(String lineTxt){
		String[] paramArr = lineTxt.split("\\,",-1);
		this.name = paramArr[0];
		this.age = Integer.valueOf(paramArr[1]);
		this.info = paramArr[2];
		return this;
	}
	
}
