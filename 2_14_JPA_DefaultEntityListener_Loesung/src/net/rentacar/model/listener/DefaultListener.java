package net.rentacar.model.listener;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jakarta.persistence.PostPersist;

import net.rentacar.model.AbstractBusinessObject;

public class DefaultListener {

	public static final String DATEIENDUNG = "xml";
	public static final String VERZEICHNIS = "out";

	@PostPersist
	public void afterInsertion(Object inserted) {
		if (inserted instanceof AbstractBusinessObject) {
			AbstractBusinessObject businessOBject = (AbstractBusinessObject) inserted;
			
		BufferedOutputStream objOutStream = null;
		XMLEncoder encoder = null;
		try {
			FileOutputStream outStream = new FileOutputStream(VERZEICHNIS+File.separatorChar+businessOBject.getClass().getSimpleName()+ businessOBject.getId() + "."+DATEIENDUNG);
			objOutStream = new BufferedOutputStream(outStream);
			encoder = new XMLEncoder(objOutStream);
			encoder.writeObject(businessOBject);
			encoder.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			encoder.close();
		}}
	}
}
