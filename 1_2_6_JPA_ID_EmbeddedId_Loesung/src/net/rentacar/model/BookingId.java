package net.rentacar.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class BookingId implements Serializable {

	private String contractNumber;
	private String branchCode;

	public BookingId() {
	}

	public BookingId(String contractNumber, String branchCode) {
		this.contractNumber = contractNumber;
		this.branchCode = branchCode;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookingId bookingId = (BookingId) o;
		return Objects.equals(contractNumber, bookingId.contractNumber) && Objects.equals(branchCode, bookingId.branchCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contractNumber, branchCode);
	}
}
