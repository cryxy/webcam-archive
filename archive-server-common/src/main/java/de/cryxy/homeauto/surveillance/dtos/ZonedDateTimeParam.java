package de.cryxy.homeauto.surveillance.dtos;

import java.time.ZonedDateTime;

public class ZonedDateTimeParam {

	private ZonedDateTime zonedDateTime;

	public ZonedDateTimeParam(ZonedDateTime zonedDateTime) {

		this.zonedDateTime = zonedDateTime;

	}

	public ZonedDateTimeParam(String dateString) {

		zonedDateTime = ZonedDateTime.parse(dateString);

	}

	@Override
	public String toString() {
		return zonedDateTime.toString();
	}

	public ZonedDateTime getZonedDateTime() {
		return zonedDateTime;
	}

	public void setZonedDateTime(ZonedDateTime zonedDateTime) {
		this.zonedDateTime = zonedDateTime;
	}

}
