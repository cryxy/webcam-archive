package de.cryxy.homeauto.surveillance.queries;

import java.time.ZonedDateTime;

import javax.swing.SortOrder;

public class EventQuery {
	
	private String userId;
	private Long webcamId; 
	private ZonedDateTime beginPeriod; 
	private ZonedDateTime endPeriod;
	private Integer limit; 
	private Integer offset;
	
	private Integer minSnapshots;
	private SortOrder sortOrder;
	
	private boolean withSnapshots;
	
	private EventQuery() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getWebcamId() {
		return webcamId;
	}

	public void setWebcamId(Long webcamId) {
		this.webcamId = webcamId;
	}

	public ZonedDateTime getBeginPeriod() {
		return beginPeriod;
	}

	public void setBeginPeriod(ZonedDateTime beginPeriod) {
		this.beginPeriod = beginPeriod;
	}

	public ZonedDateTime getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(ZonedDateTime endPeriod) {
		this.endPeriod = endPeriod;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMinSnapshots() {
		return minSnapshots;
	}

	public void setMinSnapshots(Integer minSnapshots) {
		this.minSnapshots = minSnapshots;
	}

	public boolean isWithSnapshots() {
		return withSnapshots;
	}

	public void setWithSnapshots(boolean withSnapshots) {
		this.withSnapshots = withSnapshots;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "EventQuery [userId=" + userId + ", webcamId=" + webcamId + ", beginPeriod=" + beginPeriod
				+ ", endPeriod=" + endPeriod + ", limit=" + limit + ", offset=" + offset + ", minSnapshots="
				+ minSnapshots + ", sortOrder=" + sortOrder + ", withSnapshots=" + withSnapshots + "]";
	}
	
	public static EventQueryBuilder create() {
		return new EventQueryBuilder();
	}
	
	public static class EventQueryBuilder {
		
		private String userId;
		private Long webcamId; 
		private ZonedDateTime beginPeriod; 
		private ZonedDateTime endPeriod;
		private Integer limit; 
		private Integer offset;
		
		private Integer minSnapshots;
		private SortOrder sortOrder;
		
		private boolean withSnapshots;
		
		public EventQueryBuilder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public EventQueryBuilder webcamId(Long webcamId) {
			this.webcamId = webcamId;
			return this;
		}

		public EventQueryBuilder beginPeriod(ZonedDateTime beginPeriod) {
			this.beginPeriod = beginPeriod;
			return this;
		}

		public EventQueryBuilder endPeriod(ZonedDateTime endPeriod) {
			this.endPeriod = endPeriod;
			return this;
		}

		public EventQueryBuilder limit(Integer limit) {
			this.limit = limit;
			return this;
		}

		public EventQueryBuilder offset(Integer offset) {
			this.offset = offset;
			return this;
		}

		public EventQueryBuilder minSnapshots(Integer minSnapshots) {
			this.minSnapshots = minSnapshots;
			return this;
			
		}

		public EventQueryBuilder sortOrder(SortOrder sortOrder) {
			this.sortOrder = sortOrder;
			return this;
		}
		
		public EventQueryBuilder withSnapshots(boolean withSnapshots) {
			this.withSnapshots = withSnapshots;
			return this;
		}
		
		public EventQuery build() {
			EventQuery eq = new EventQuery();
			eq.userId = this.userId;
			eq.webcamId = this.webcamId;
			eq.beginPeriod = this.beginPeriod;
			eq.endPeriod = this.endPeriod;
			eq.limit = this.limit;
			eq.offset = this.offset;
			eq.minSnapshots = this.minSnapshots;
			eq.sortOrder = this.sortOrder;
			eq.withSnapshots = this.withSnapshots;
			
			return eq;
			
		}
		
		
		
		
	}
	
	
	

}
