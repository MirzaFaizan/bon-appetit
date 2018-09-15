package com.apps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "fundrive.db";
    private SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;
    String outFileName = "";
    SharedPreferences.Editor spEdit;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        //------------------------------------------------------------
        PackageInfo pinfo = null;
        if (!dbExist) {
            getReadableDatabase();
            copyDataBase();
        }

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public Cursor getData(String Query) {
        String myPath = DB_PATH + DB_NAME;
        Cursor c = null;
        try {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            c = db.rawQuery(Query, null);
        } catch (Exception e) {
            Log.e("Err", e.toString());
        }
        return c;
    }

    //UPDATE temp_dquot SET age='20',name1='--',rdt='11/08/2014',basic_sa='100000',plno='814',pterm='20',mterm='20',mat_date='11/08/2034',mode='YLY',dab_sa='100000',tr_sa='0',cir_sa='',bonus_rate='42',prem='5276',basic_prem='5118',dab_prem='100.0',step_rate='for Life',loyal_rate='0',bonus_rate='42',act_mat='1,88,000',mly_b_pr='448',qly_b_pr='1345',hly_b_pr='2664',yly_b_pr='5276'  WHERE uniqid=1
    public void dml(String Query) {
        String myPath = DB_PATH + DB_NAME;
        if (db == null)
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            db.execSQL(Query);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    public Boolean isFav(String id, String type) {
        String query = "";
        if(type.equals("wall")) {
            query = "select * from wallpaper where wid = '" + id + "'";
        } else if(type.equals("ring")) {
            query = "select * from ringtone where rid = '" + id + "'";
        } else if(type.equals("video")) {
            query = "select * from video where vid = '" + id + "'";
        }
        Cursor cursor = getData(query);
        if(cursor != null && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

//    public void addFavWall(ItemWallpaper itemWallpaper) {
//        if(!isFav(itemWallpaper.getId(),"wall")) {
//            dml("insert into wallpaper (wid, cid, username, cname, imageb, images, tags, rate, tot_views, tot_downloads) values ('" + itemWallpaper.getId() + "' ," +
//                    "'" + itemWallpaper.getCId() + "' , '" + itemWallpaper.getUserName() + "' , '" + itemWallpaper.getCName() + "' , '" + itemWallpaper.getImageBig() + "' ," +
//                    "'" + itemWallpaper.getImageSmall() + "' , '" + itemWallpaper.getTags() + "' ,'" + itemWallpaper.getRateAvg() + "' , '" + itemWallpaper.getTotalViews() + "' ," +
//                    "'" + itemWallpaper.getTotalDownload() + "')");
//        } else {
//            dml("update wallpaper set wid = '" + itemWallpaper.getId() + "' , cid = '" + itemWallpaper.getCId() + "', username = '" + itemWallpaper.getUserName() + "'" +
//                    ", cname = '" + itemWallpaper.getCName() + "', imageb = '" + itemWallpaper.getImageBig() + "', images = '" + itemWallpaper.getImageSmall() + "', " +
//                    "tags = '" + itemWallpaper.getTags() + "', rate = '" + itemWallpaper.getRateAvg() + "', tot_views = '" + itemWallpaper.getTotalViews() + "', " +
//                    "tot_downloads = '" + itemWallpaper.getTotalDownload() + "')");
//        }
//    }
//
//    public void addFavRing(ItemRingtone itemRingtone) {
//        if(!isFav(itemRingtone.getId(),"ring")) {
//            dml("insert into ringtone (rid, cid, username, cname, type, title, url, duration, imageb, images, tags, rate, tot_views, tot_downloads) values ('" + itemRingtone.getId() + "' ," +
//                    "'" + itemRingtone.getCId() + "' , '" + itemRingtone.getUserName() + "' , '" + itemRingtone.getCname() + "' , '" + itemRingtone.getType() + "' , " +
//                    "'" + itemRingtone.getTitle() + "' ,'" + itemRingtone.getUrl() + "' ,'" + itemRingtone.getDuration() + "' ,'" + itemRingtone.getImageBig() + "' ," +
//                    "'" + itemRingtone.getImageSmall() + "' , '" + itemRingtone.getTags() + "' ,'" + itemRingtone.getRateAvg() + "' , '" + itemRingtone.getTotalViews() + "' ," +
//                    "'" + itemRingtone.getTotalDownload() + "')");
//        } else {
//            dml("update ringtone set rid = '" + itemRingtone.getId() + "' , cid = '" + itemRingtone.getCId() + "', username = '" + itemRingtone.getUserName() + "'" +
//                    ", cname = '" + itemRingtone.getCname() + "', type = '" + itemRingtone.getType() + "', title = '" + itemRingtone.getTitle() + "', url = '" + itemRingtone.getUrl() + "'," +
//                    "duration = '" + itemRingtone.getDuration() + "', imageb = '" + itemRingtone.getImageBig() + "', images = '" + itemRingtone.getImageSmall() + "', " +
//                    "tags = '" + itemRingtone.getTags() + "', rate = '" + itemRingtone.getRateAvg() + "', tot_views = '" + itemRingtone.getTotalViews() + "', " +
//                    "tot_downloads = '" + itemRingtone.getTotalDownload() + "')");
//        }
//    }
//
//    public void addFavVideo(ItemVideos itemVideos) {
//        if(!isFav(itemVideos.getId(),"video")) {
//            dml("insert into video (vid, cid, username, cname, type, title, url, duration, video_id, imageb, images, tags, rate, tot_views) values ('" + itemVideos.getId() + "' ," +
//                    "'" + itemVideos.getCId() + "' , '" + itemVideos.getUserName() + "' , '" + itemVideos.getCName() + "' , '" + itemVideos.getType() + "' , " +
//                    "'" + itemVideos.getTitle() + "' ,'" + itemVideos.getUrl() + "' ,'" + itemVideos.getDuration() + "' , '" + itemVideos.getVideo_id() + "' ,'" + itemVideos.getImageBig() + "' ," +
//                    "'" + itemVideos.getImageSmall() + "' , '" + itemVideos.getTags() + "' ,'" + itemVideos.getRateAvg() + "' , '" + itemVideos.getTotalViews() + "')");
//        } else {
//            dml("update video set vid = '" + itemVideos.getId() + "' , cid = '" + itemVideos.getCId() + "', username = '" + itemVideos.getUserName() + "'" +
//                    ", cname = '" + itemVideos.getCName() + "', type = '" + itemVideos.getType() + "', title = '" + itemVideos.getTitle() + "', url = '" + itemVideos.getUrl() + "'," +
//                    "duration = '" + itemVideos.getDuration() + "', video_id = '" + itemVideos.getVideo_id() + "', imageb = '" + itemVideos.getImageBig() + "', images = '" + itemVideos.getImageSmall() + "', " +
//                    "tags = '" + itemVideos.getTags() + "', rate = '" + itemVideos.getRateAvg() + "', tot_views = '" + itemVideos.getTotalViews() + "')");
//        }
//    }
//
//    public void removeFav(String id, String type) {
//        switch(type) {
//            case "wallpaper":
//                dml("delete from wallpaper where wid = '"+id+"'");
//                break;
//
//            case "ringtone":
//                dml("delete from ringtone where rid = '"+id+"'");
//                break;
//
//            case "video":
//                dml("delete from video where vid = '"+id+"'");
//                break;
//        }
//    }
//
//    public void updateViewWall(String id, String totview, String type) {
//        int views = Integer.parseInt(totview);
//        switch (type) {
//            case "views":
//                dml("update wallpaper set tot_views = '" + String.valueOf(views) + "' where wid = '" + id + "'");
//                break;
//            case "downloads":
//                dml("update wallpaper set tot_downloads = '" + String.valueOf(views) + "' where wid = '" + id + "'");
//                break;
//            default:
//                dml("update wallpaper set rate = '" + String.valueOf(views) + "' where wid = '" + id + "'");
//                break;
//        }
//    }
//
//    public void updateViewRing(String id, String totview, String type) {
//        int views = Integer.parseInt(totview);
//        switch (type) {
//            case "views":
//                dml("update ringtone set tot_views = '" + String.valueOf(views) + "' where rid = '" + id + "'");
//                break;
//            case "downloads":
//                dml("update ringtone set tot_downloads = '" + String.valueOf(views) + "' where rid = '" + id + "'");
//                break;
//            default:
//                dml("update ringtone set rate = '" + String.valueOf(views) + "' where rid = '" + id + "'");
//                break;
//        }
//    }
//
//    public void updateViewVideo(String id, String totview, String type) {
//        int views = Integer.parseInt(totview);
//        switch (type) {
//            case "views":
//                dml("update video set tot_views = '" + String.valueOf(views) + "' where vid = '" + id + "'");
//                break;
//            case "downloads":
//                dml("update video set tot_downloads = '" + String.valueOf(views) + "' where vid = '" + id + "'");
//                break;
//            default:
//                dml("update video set rate = '" + String.valueOf(views) + "' where vid = '" + id + "'");
//                break;
//        }
//    }
//
//    public ArrayList<ItemWallpaper> getFavWall() {
//        ArrayList<ItemWallpaper> arrayList = new ArrayList<>();
//
//        Cursor cursor = getData("select * from wallpaper");
//
//        if(cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            for(int i=0; i<cursor.getCount();i++) {
//                String wid = cursor.getString(cursor.getColumnIndex("wid"));
//                String cid = cursor.getString(cursor.getColumnIndex("cid"));
//                String username = cursor.getString(cursor.getColumnIndex("username"));
//                String cname = cursor.getString(cursor.getColumnIndex("cname"));
//                String imageb = cursor.getString(cursor.getColumnIndex("imageb"));
//                String images = cursor.getString(cursor.getColumnIndex("images"));
//                String tags = cursor.getString(cursor.getColumnIndex("tags"));
//                String rate = cursor.getString(cursor.getColumnIndex("rate"));
//                String tot_views = cursor.getString(cursor.getColumnIndex("tot_views"));
//                String tot_downloads = cursor.getString(cursor.getColumnIndex("tot_downloads"));
//
//                ItemWallpaper itemWallpaper = new ItemWallpaper(wid,cid,username,cname,imageb,images,tags,rate,tot_views,tot_downloads);
//                arrayList.add(itemWallpaper);
//                cursor.moveToNext();
//            }
//
//        }
//
//        return arrayList;
//    }
//
//    public ArrayList<ItemVideos> getFavVideo() {
//        ArrayList<ItemVideos> arrayList = new ArrayList<>();
//
//        Cursor cursor = getData("select * from video");
//
//        if(cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            for(int i=0; i<cursor.getCount();i++) {
//                String vid = cursor.getString(cursor.getColumnIndex("vid"));
//                String cid = cursor.getString(cursor.getColumnIndex("cid"));
//                String username = cursor.getString(cursor.getColumnIndex("username"));
//                String cname = cursor.getString(cursor.getColumnIndex("cname"));
//                String type = cursor.getString(cursor.getColumnIndex("type"));
//                String title = cursor.getString(cursor.getColumnIndex("title"));
//                String url = cursor.getString(cursor.getColumnIndex("url"));
//                String duration = cursor.getString(cursor.getColumnIndex("duration"));
//                String video_id = cursor.getString(cursor.getColumnIndex("video_id"));
//                String imageb = cursor.getString(cursor.getColumnIndex("imageb"));
//                String images = cursor.getString(cursor.getColumnIndex("images"));
//                String tags = cursor.getString(cursor.getColumnIndex("tags"));
//                String rate = cursor.getString(cursor.getColumnIndex("rate"));
//                String tot_views = cursor.getString(cursor.getColumnIndex("tot_views"));
//
//                ItemVideos itemVideos = new ItemVideos(vid,cid,username,cname,type,title,url,video_id,duration,imageb,images,tags,rate,tot_views);
//                arrayList.add(itemVideos);
//                cursor.moveToNext();
//            }
//        }
//
//        return arrayList;
//    }
//
//    public ArrayList<ItemRingtone> getFavRingtone() {
//        ArrayList<ItemRingtone> arrayList = new ArrayList<>();
//
//        Cursor cursor = getData("select * from ringtone");
//
//        if(cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            for(int i=0; i<cursor.getCount();i++) {
//                String vid = cursor.getString(cursor.getColumnIndex("rid"));
//                String cid = cursor.getString(cursor.getColumnIndex("cid"));
//                String username = cursor.getString(cursor.getColumnIndex("username"));
//                String cname = cursor.getString(cursor.getColumnIndex("cname"));
//                String type = cursor.getString(cursor.getColumnIndex("type"));
//                String title = cursor.getString(cursor.getColumnIndex("title"));
//                String url = cursor.getString(cursor.getColumnIndex("url"));
//                String duration = cursor.getString(cursor.getColumnIndex("duration"));
//                String imageb = cursor.getString(cursor.getColumnIndex("imageb"));
//                String images = cursor.getString(cursor.getColumnIndex("images"));
//                String tags = cursor.getString(cursor.getColumnIndex("tags"));
//                String rate = cursor.getString(cursor.getColumnIndex("rate"));
//                String tot_views = cursor.getString(cursor.getColumnIndex("tot_views"));
//                String tot_downloads = cursor.getString(cursor.getColumnIndex("tot_downloads"));
//
//                ItemRingtone itemRingtone = new ItemRingtone(vid,cid,username,cname,type,title,url,duration,imageb,images,tags,rate,tot_views,tot_downloads);
//                arrayList.add(itemRingtone);
//                cursor.moveToNext();
//            }
//        }
//
//        return arrayList;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}  