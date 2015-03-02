package com.petcare.demo1;
import java.io.Serializable;   

public class User implements Serializable {
	
	private String id;
	private String tel;
	private String name;
	private String type;
	private String tenement;
	private String information;
	private String village;
	private String buildingNumber;
	private String houseNumber;
	private String carNumber1;
	private String carNumber2;
	private String complainType;
	private String complainContent;
	private String complainHandleState;
	private String complainTime;
	private String complainhandleResult;
	private String fixConfirmTime;
	private String fixOrderTime;
	private String fixContent;
	private String fixStateExplain;
	private String fixResult;
	private String senderName;
	private String content;
	private String sendusername;
	private String sendertel;
	private String senderaddress;
	private String handleid;
	private String workername;
	private String workertel;
	private String workerid;
	private String fixsubmittime;
	public User(String name){
		this.id=name;
	}
	
	public String getId() {
		return id;
		}
	
	public void setName(String name) {
	this. name = name;
	}
	
	
	public String getName() {
	return name;
	}
	
	public void setType(String type){
		this.type=type;
	}
	
	public String getType(){
		return type;
	}
	public void setTenement(String tenement){
		this.tenement=tenement;
	}
	public String getTenement(){
		return tenement;
	}
	public void setInformation(String information){
		this.information=information;
	}
	public String getInformation(){
		return information;
	}
	public void setVillage(String village){
		this.village=village;
	}
	public String getVillage(){
		return village;
	}
	public void setBulidingNumber(String buildingNumber){
		this.buildingNumber=buildingNumber;
	}
	public String getBuildingNumber(){
		return buildingNumber;
	}
	public void setHouseNumber(String houseNumber){
		this.houseNumber=houseNumber;
	}
	public String getHouseNumber(){
		return houseNumber;
	}
	public void setCarNumber1(String carNumber1){
		this.carNumber1=carNumber1;
	}
	public String getCarNumber1(){
		return carNumber1;
	}
	public void setCarNumber2(String carNumber2){
		this.carNumber2=carNumber2;
	}
	public String getCarNumber2(){
		return carNumber2;
	}
	public void setTel(String tel){
		this.tel=tel;
	}
	public String getTel(){
		return tel;
	}
	public void setComplainTime(String time){
		this.complainTime=time;
	}
	public String getComplainTime(){
		return complainTime;
	}
	public void setComplainType(String type){
		this.complainType=type;
	}
	public String getComplainType(){
		return complainType;
	}
	public void setComplainContent(String content){
		this.complainContent=content;
	}
	public String getComplainContent(){
		return complainContent;
	}
	public void setComplainHandleState(String state){
		this.complainHandleState=state;
	}
	public String getComplainHandleState(){
		return complainHandleState;
	}
	public void setComplainHanleResult(String result){
		this.complainhandleResult=result;
	}
	public String getComplainHandleResult(){
		return complainhandleResult;
	}
	public void setFixConfirmTime(String s){
		this.fixConfirmTime=s;
	}
	public String getFixConfirmTime(){
		return fixConfirmTime;
	}
	public void setFixOrederTime(String s){
		this.fixOrderTime=s;
	}
	public String getFixOrderTime(){
		return fixOrderTime;
	}
	public void setFixContent(String s){
		this.fixContent=s;
	}
	public String getFixContent(){
		return fixContent;
	}
	public void setFixStateExplain(String s){
		this.fixStateExplain=s;
	}
	public String getFixStateExplain(){
		return fixStateExplain;
	}
	public void setFixResult(String s){
		this.fixResult=s;
	}
	public String getFixResult(){
		return fixResult;
	}
	public void setSenderName(String s){
		this.senderName=s;
	}
	public String getSenderName(){
		return senderName;
	}
	public void setContent(String s){
		this.content=s;
	}
	public String getContent(){
		return content;
	}
	public void setSenderusername(String s){
		this.sendusername=s;
	}
	public String getSenderusername(){
		return sendusername;
	}
	public void setSendertel(String s){
		this.sendertel=s;
	}
	public String getSendertel(){
		return sendertel;
	}
	public void setSenderaddress(String s){
		this.senderaddress=s;
	}
	public String getSenderaddress(){
		return senderaddress;
	}
	public void setHandleid(String s){
		this.handleid=s;
	}
	public String getHandleid(){
		return handleid;
	}
	public void setWorkername(String s){
		this.workername=s;
	}
	public String getWorkername(){
		return workername;
	}
	public void setWorkertel(String s){
		this.workertel=s;
	}
	public String getWorkertel(){
		return workertel;
	}
	public void setWorkerid(String s){
		this.workerid=s;
		
	}
	public String getWorkerid(){
		return workerid;
	}
	public void setFixsubmittime(String s){
		this.fixsubmittime=s;
	}
	public String getFixsubmittime(){
		return fixsubmittime;
	}
}
