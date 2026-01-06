package net.rentacar.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class VehicleType {

	@Id
	private Long id;
	private String brand;
	private String modell;
	private long hp = 100;
	private long maxKph;

	public VehicleType() {
	}

	public VehicleType(String brand, String modell, long hp, long maxKph) {
		this.brand = brand;
		this.modell = modell;
		this.hp = hp;
		this.maxKph = maxKph;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setModell(String modell) {
		this.modell = modell;
	}

	public String getBrand() {
		return brand;
	}

	public String getModell() {
		return modell;
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

	public Long getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof VehicleType) {
			VehicleType other = (VehicleType) obj;
			if (this.brand.equals(other.brand)
					&& this.modell.equals(other.modell))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = brand.hashCode();
		hashCode *= modell.hashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		return this.brand + " : " + this.modell;
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

}
