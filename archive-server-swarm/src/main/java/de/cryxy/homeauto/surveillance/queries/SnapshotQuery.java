package de.cryxy.homeauto.surveillance.queries;

import javax.swing.SortOrder;

public class SnapshotQuery {

	private Long webcamId;
	private String fileName;
	private Long snapshotId; 
	private SortOrder sortOrder;
	private Integer maxResults;
	private Long eventId;

	private SnapshotQuery() {
		
	}

	public Long getWebcamId() {
		return webcamId;
	}

	public void setWebcamId(Long webcamId) {
		this.webcamId = webcamId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(Long snapshotId) {
		this.snapshotId = snapshotId;
	}
	
	
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public static SnapshotQueryBuilder create() {
		return new SnapshotQueryBuilder();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SnapshotQuery [webcamId=" + webcamId + ", fileName=" + fileName + ", snapshotId=" + snapshotId
				+ ", sortOrder=" + sortOrder + ", maxResults=" + maxResults + ", eventId=" + eventId + "]";
	}
	
	public static class SnapshotQueryBuilder {
		private Long webcamId;
		private String fileName;
		private Long snapshotId;
		private Long eventId;
		
		private SortOrder sortOrder;
		private Integer maxResults;
		
		public SnapshotQueryBuilder webcamId(Long webcamId) {
			this.webcamId = webcamId;
			return this;
		}
		public SnapshotQueryBuilder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}
		public SnapshotQueryBuilder sortOrder(SortOrder sortOrder) {
			this.sortOrder = sortOrder;
			return this;
		}
		public SnapshotQueryBuilder maxResults(Integer maxResults) {
			this.maxResults = maxResults;
			return this;
		}
		public SnapshotQueryBuilder eventId(Long eventId) {
			this.eventId = eventId;
			return this;
		}
		
		public SnapshotQuery build() {
			SnapshotQuery sq = new SnapshotQuery();
			sq.setWebcamId(this.webcamId);
			sq.setFileName(this.fileName);
			sq.setSnapshotId(this.snapshotId);
			sq.setSortOrder(this.sortOrder);
			sq.setMaxResults(this.maxResults);
			sq.setEventId(this.eventId);
			return sq;
		}
		
		
		
		
//		public SnapshotQueryBuilder snapshotId(Long snapshotId) {
//			this.snapshotId = snapshotId;
//		} 
		
		
	}

}
