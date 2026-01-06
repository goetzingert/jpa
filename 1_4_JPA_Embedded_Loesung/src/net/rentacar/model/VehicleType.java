package net.rentacar.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_VehicleType")
public class VehicleType {

	@Id
	private String id;
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "series", column = @Column(name = "model_series"))})
	private VehicleModel model;
	@Column(precision = 4)
	private long hp = 100;
	@Column(precision = 3)
	private long maxKpH;

	public VehicleType() {
	}

	public VehicleType(String id, VehicleModel model, long hp, long maxKpH) {
		this.id = id;
		this.hp = hp;
		this.maxKpH = maxKpH;
		this.model = model;
	}

	public String getId() {
		return id;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (maxKpH ^ (maxKpH >>> 32));
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + (int) (hp ^ (hp >>> 32));
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
		VehicleType other = (VehicleType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maxKpH != other.maxKpH)
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (hp != other.hp)
			return false;
		return true;
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

	public void setModel(VehicleModel model) {
		this.model = model;
	}

	public VehicleModel getModel() {
		return model;
	}

}
