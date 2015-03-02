package com.petcare.demo1;
import java.io.Serializable;
public class cameraParam {
	private String id;
	private String cameraName;
	
	public cameraParam() {
	}
	public void Setid(String id){
		this.id=id;
	}
	public String getid(){
		return id;
	}
	public void setName(String name){
		this.cameraName=name;
	}
	public String getName(){
		return cameraName;
	}
}
