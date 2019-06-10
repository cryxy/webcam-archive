/* tslint:disable:max-line-length */
/**
 * VArchive API
 * undefined/varchive/api
 */

export interface WebcamDto {
  /** format: int64 */
  id?: number;
  name?: string;
  location?: string;
  snapshotUri?: string;
  snapshotDir?: string;
  clientId?: string;
  /** format: int32 */
  minSnapshotsThreshold?: number;
  /** format: int32 */
  alertThreshold?: number;
  alertMail?: string;
}
