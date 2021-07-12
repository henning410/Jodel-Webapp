package de.hse.swa.jodel.util;


public class Position{
	private double lat;
	private double lon;
	public Position(){
		this.lat = 0;
		this.lon = 0;
	}
	public Position(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
}
