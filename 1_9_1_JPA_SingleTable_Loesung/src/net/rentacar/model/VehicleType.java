package net.rentacar.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Vehicle_Hierarchy")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Vehicle")
public class VehicleType {

	@Id
	private String id;
	@Embedded
	private Model model;
	@Column(precision = 4)
	private long hp = 100;
	@Column(precision = 3)
	private long maxKph;

	public VehicleType() {
	}

	public VehicleType(String id, Model model, long hp, long maxKph) {
		this.id = id;
		this.hp = hp;
		this.maxKph = maxKph;
		this.model = model;
	}

	public String getId() {
		return id;
	}

	public long getHp() {
		return hp;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}

	public long getMaxKph() {
		return maxKph;
	}

	public void setMaxKph(long maxKph) {
		this.maxKph = maxKph;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof VehicleType) {
			VehicleType other = (VehicleType) obj;
			return this.getModel().equals(other.getModel());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getModel().hashCode();
	}

	@Override
	public String toString() {
		return getModel().toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objout = new ObjectOutputStream(out);
			objout.writeObject(this);
			objout.flush();
			ByteArrayInputStream in = new ByteArrayInputStream(out
					.toByteArray());
			ObjectInputStream obIn = new ObjectInputStream(in);
			Object toReturn = obIn.readObject();
			obIn.close();
			in.close();
			out.close();
			objout.close();
			return toReturn;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}

	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

}
