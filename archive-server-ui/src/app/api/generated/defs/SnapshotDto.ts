/* tslint:disable:max-line-length */
/**
 * VArchive API
 * undefined/varchive/api
 */

export interface SnapshotDto {
  /** format: int64 */
  id?: number;
  /** format: int64 */
  webcamId?: number;
  /** format: int64 */
  eventId?: number;
  /** format: date-time */
  timestamp?: string;
  fileName?: string;
}
