package de.cryxy.homeauto.surveillance.enums;
/**
 * Calculator: http://andrew.hedges.name/experiments/aspect_ratio/
 * @author Fabian
 *
 */
public enum ImageSize {
	// 180 x 101
	XTRA_SMALL,
	// 320 x 180
	SMALL,
	// 720 x 405
	MEDIUM,
	// 1200 x 720
	LARGE,
	// 1920 x 1080
	ORIGINAL;
	
	public Integer[] getWidthAndHeigth() {
		
		switch (this) {
		case LARGE:
			return new Integer[]{1200,720};
		case MEDIUM:
			return new Integer[]{720,405};
		case SMALL:
			return new Integer[]{320,180};
		case XTRA_SMALL:
			return new Integer[]{180,101};
		default:
			return new Integer[]{1920,1080};
		}
	}
	
	

}
