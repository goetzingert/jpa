package net.rentacar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tbl_VehicleType")
public class VehicleType extends AbstractBusinessObject {

	@Embedded
	private Model model;
	@Column(precision = 4)
	private long hp = 100;
	@Column(precision = 3)
	private long maxKpH;

	public VehicleType() {
	}

	public VehicleType(Model model, long hp, long maxKph) {
		super();
		this.model = model;
		this.hp = hp;
		this.maxKpH = maxKph;
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

	public void setModel(Model model) {
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

}
