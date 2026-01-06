/**
 * 
 */
package net.fuhrparkservice.dto;

/**
 * @author goetzingert
 *
 */
public class VehicleDTO {
	
	public final String modell;
	public final long maxKph;
	
	public VehicleDTO(String modell, long maxKph) {
		this.modell = modell;
		this.maxKph = maxKph;
	}

}
