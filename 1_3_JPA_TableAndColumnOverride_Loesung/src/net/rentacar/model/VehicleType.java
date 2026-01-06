package net.rentacar.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_VehicleType")
public class VehicleType {

	@Id
	private String id;
	@Column(length = 30, nullable = false)
	private String brand;
	@Column(length = 50, nullable = false)
	private String modell;
	@Column(precision = 4)
	private long hp = 100;
	@Column(precision = 3)
	private long maxKpH;

	public VehicleType() {
	}

	public VehicleType(String id, String brand, String modell, long hp,
					   long maxKpH) {
		this.id = id;
		this.brand = brand;
		this.modell = modell;
		this.hp = hp;
		this.maxKpH = maxKpH;
	}

	public String getId() {
		return id;
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
		this.hp = HP;
	}

	public long getMaxKpH() {
		return maxKph;
	}

	public void setMaxKpH(long maxKph) {
		this.maxKpH = maxKph;
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
