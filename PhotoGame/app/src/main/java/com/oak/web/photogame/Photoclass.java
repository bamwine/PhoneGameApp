package com.oak.web.photogame;

public class Photoclass {
	 String uname;
	 String title,id;
	 String category;
	byte[] filename;
 String descption,location,datetaken,upvotes,downvotes;

	public Photoclass(String id,String uname, String title, String category, String descption, String location, byte[]  filename, String datetaken, String upvotes, String downvotes) {
		this.id = id;
		this.uname = uname;
		this.title = title;
		this.category = category;
		this.descption = descption;
		this.location = location;
		this.filename = filename;
		this.datetaken = datetaken;
		this.upvotes = upvotes;
		this.downvotes = downvotes;
	}

	public Photoclass() {

	}

	public String getId() {
		return uname;
	}

	public void setId(String Id) {
		this.uname = uname;
	}


	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescption() {
		return descption;
	}

	public void setDescption(String descption) {
		this.descption = descption;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public byte[]  getFilename() {
		return filename;
	}

	public void setFilename(byte[]  filename) {
		this.filename = filename;
	}

	public String getDatetaken() {
		return datetaken;
	}

	public void setDatetaken(String datetaken) {
		this.datetaken = datetaken;
	}

	public String getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(String upvotes) {
		this.upvotes = upvotes;
	}

	public String getDownvotes() {
		return downvotes;
	}

	public void setDownvotes(String downvotes) {
		this.downvotes = downvotes;
	}
}
