/* tslint:disable:max-line-length */
/**
 * VArchive API
 * undefined/varchive/api
 */

import * as __model from '../model';

export interface EventDto {
  /** format: int64 */
  id?: number;
  /** format: int64 */
  webcamId?: number;
  /** format: date-time */
  startDate?: string;
  /** format: date-time */
  endDate?: string;
  /** format: int32 */
  snapshotsSize?: number;
  snapshots?: __model.SnapshotDto[];
}
