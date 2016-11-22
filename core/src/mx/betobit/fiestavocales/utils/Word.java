package mx.betobit.fiestavocales.utils;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class Word {

	private String label;
	private boolean isDiphthong;
	private boolean isHiatus;

	public Word(String label, boolean dip, boolean hi) {
		this.label = label;
		isDiphthong = dip;
		isHiatus = hi;
	}

	public String getLabel() {
		return label;
	}

	public boolean isDiphthong() {
		return isDiphthong;
	}

	public boolean isHiatus() {
		return isHiatus;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
