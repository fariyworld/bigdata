package dataprocess.utils;

import miscellaneous.DataChecker;

/**
 * 点
 * @author 15257
 *
 */
public class Point {
	
	/**
	 * 经纬度(纬度,经度) 
	 */
	class CoordinateLB{
		
		private double latitude;
		private double longitude;

		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		
		public CoordinateLB(double longitude, double latitude) {
			super();
			this.longitude = longitude;
			this.latitude = latitude;
		}
		public CoordinateLB() {
			this.longitude = Double.POSITIVE_INFINITY;
			this.latitude = Double.POSITIVE_INFINITY;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(DataChecker.isEffective(latitude) ? latitude : "");
			builder.append(",");
			builder.append(DataChecker.isEffective(longitude) ? longitude : "");
			return builder.toString();
		}
	}
	
	/**
	 * 	坐标(经纬度转换)
	 */
	class CoordinateXYZ{
		
	}
}
