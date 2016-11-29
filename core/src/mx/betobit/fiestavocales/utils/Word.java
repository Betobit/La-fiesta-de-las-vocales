package mx.betobit.fiestavocales.utils;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class Word {

	private int id;
	private String label;
	private boolean isDiphthong;
	private boolean isHiatus;


	public String getLabel() {
		return label;
	}

	public boolean isDiphthong() {
		return isDiphthong;
	}

	public boolean isHiatus() {
		return isHiatus;
	}

	public int getId() {
		return id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
