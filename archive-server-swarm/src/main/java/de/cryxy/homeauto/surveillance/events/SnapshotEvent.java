/**
 * 
 */
package de.cryxy.homeauto.surveillance.events;

import javax.validation.constraints.NotNull;

import de.cryxy.homeauto.surveillance.entities.Snapshot;

/**
 * @author fabian
 *
 */
public class SnapshotEvent {

	public SnapshotEvent(Snapshot value) {
		super();
		this.value = value;
	}

	@NotNull
	private Snapshot value;

	/**
	 * @return the value
	 */
	public Snapshot getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Snapshot value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SnapshotEvent [value=" + value + "]";
	}

}
