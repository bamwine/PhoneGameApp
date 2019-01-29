package com.oak.web.photogame;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "phonegameapp";

	// Contacts table name
	private static final String TABLE_UPLOADS = "filesup";

	// Contacts Table Columns names
	private static final String KEY_id = "id";
	private static final String KEY_uname = "uname";
	private static final String KEY_title = "title";	
	private static final String KEY_category = "category";
	private static final String KEY_descption = "descption";
	private static final String KEY_location= "location";
	private static final String KEY_filename = "filename";
	private static final String KEY_datetaken = "datetaken";
	private static final String KEY_upvotes = "upvotes";
	private static final String KEY_downvotes = "downvotes";
	private static final String KEY_datesup = "datesup";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_UPLOADS_TABLE = "CREATE TABLE " + TABLE_UPLOADS + "("
				+ KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_uname + " TEXT,"
				+ KEY_title + " TEXT,"
				+ KEY_category + " TEXT,"
				+ KEY_descption + " TEXT,"
				+ KEY_location + " TEXT,"
				+ KEY_datetaken + " TEXT,"
				+ KEY_upvotes + " TEXT,"
				+ KEY_downvotes + " TEXT,"
				+ KEY_filename + " BLOB" + ")";
		db.execSQL(CREATE_UPLOADS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPLOADS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public// Adding new photoclass
	void addphoto(Photoclass photoclass) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_uname, photoclass.uname);
		values.put(KEY_title, photoclass.title);
		values.put(KEY_category, photoclass.category);
		values.put(KEY_descption, photoclass.descption);
		values.put(KEY_location, photoclass.location);
		values.put(KEY_datetaken, photoclass.datetaken);
		values.put(KEY_upvotes, photoclass.upvotes);
		values.put(KEY_downvotes, photoclass.downvotes);
		values.put(KEY_filename, photoclass.filename);

		// Inserting Row
		db.insert(TABLE_UPLOADS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Photoclass getbycategory(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_UPLOADS, new String[] {
						KEY_id,
						KEY_uname,
						KEY_title,
						KEY_category,
						KEY_descption,
						KEY_location,
						KEY_datetaken,
						KEY_upvotes,
						KEY_downvotes,
						KEY_filename }, KEY_category + "=?",
				new String[] { id }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();


		// return photoclass
		Photoclass photoclass = new Photoclass(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getBlob(9),cursor.getString(6),cursor.getString(7),cursor.getString(8));



		return photoclass;

	}

	// Getting All Contacts
	public List<Photoclass> getAllcollectionPhotos() {
		List<Photoclass> photoclassList = new ArrayList<Photoclass>();
		// Select All Query
		String selectQuery = "SELECT * FROM filesup ORDER BY id ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
					// Adding photoclass to list
photoclassList.add(new Photoclass(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getBlob(9),cursor.getString(6),cursor.getString(7),cursor.getString(8)));
			} while (cursor.moveToNext());
		}
		// close inserting data from database
		db.close();
		// return contact list
		return photoclassList;

	}

	// Updating single photoclass
	public int updateContact(Photoclass photoclass) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_uname, photoclass.getUname());
		values.put(KEY_title, photoclass.getTitle());
		values.put(KEY_category, photoclass.getCategory());
		values.put(KEY_descption, photoclass.descption);
		values.put(KEY_location, photoclass.getLocation());
		values.put(KEY_datetaken, photoclass.getDatetaken());
		values.put(KEY_upvotes, photoclass.getUpvotes());
		values.put(KEY_downvotes, photoclass.getDownvotes());
		values.put(KEY_filename, photoclass.getFilename());

		// updating row
		return db.update(TABLE_UPLOADS, values, KEY_id + " = ?",
				new String[] { String.valueOf(photoclass.getId()) });

	}

	// Deleting single photoclass
	public void deleteContact(Photoclass photoclass) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_UPLOADS, KEY_id + " = ?",
				new String[] {photoclass.getId() });
		db.close();
	}

	// Getting contacts Count
	public int getFilesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_UPLOADS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
