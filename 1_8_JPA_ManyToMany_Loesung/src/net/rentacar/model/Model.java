/**
 * 
 */
package net.rentacar.model;

import jakarta.persistence.Embeddable;

/**
 * @author goetzingert
 * 
 */
@Embeddable
public class Model {

	private String brand;
	private String modell;

	public Model() {
	}

	public Model(String brand, String modell) {
		super();
		this.brand = brand;
		this.modell = modell;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModell() {
		return modell;
	}

	public void setModell(String modell) {
		this.modell = modell;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Model) {
			Model other = (Model) obj;
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
}
