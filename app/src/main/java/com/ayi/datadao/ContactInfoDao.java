package com.ayi.datadao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ayi.db.MyDBHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * 联系人数据库表的访问类
 */
public class ContactInfoDao {
    /**
     * 数据库打开的帮助类
     */
    private MyDBHelper helper;

    /**
     * 在构造方法里面完成 必须要用的类的初始化
     *
     * @param context
     */
    public ContactInfoDao(Context context) {
        helper = new MyDBHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("create table if not exists flag_read_state (id integer primary key autoincrement, flag_userid varchar(20) ,flag_id varchar(20) , state varchar(10))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条记录
     *
     * @param userid userid
     * @param id     消息id
     * @param state  消息状态
     * @return 返回的是添加后在数据库的行号  -1代表添加失败
     */
    public long add(String userid, String id, String state) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("insert into contactinfo (name,phone) values (?,?)", new Object[]{name,phone});
        ContentValues values = new ContentValues();
        values.put("flag_userid", userid);
        values.put("flag_id", id);
        values.put("state", state);
        //内部是组拼sql语句实现的.
        long rowid = db.insert("flag_read_state", null, values);
        //记得释放数据库资源
        db.close();
        return rowid;
    }

    /**
     * 根据姓名删除一条记录
     *
     * @param id 根据id来删除
     * @return 返回0代表的是没有删除任何的记录 返回整数int值代表删除了几条数据
     */
    public int delete(String id) {
        //判断这个数据是否存在.
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("delete from contactinfo where name=?", new Object[]{name});
        int rowcount = db.delete("flag_read_state", "flag_userid=?", new String[]{id});
        db.close();
        //再从数据库里面查询一遍,看name是否还在
        return rowcount;
    }

    /**
     * 修改联系人电话号码
     *
     * @param state 修改的状态
     * @param id    要修改的联系人id
     * @return 0代表一行也没有更新成功, >0 整数代表的是更新了多少行记录
     */
    public int update(String state, String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.execSQL("update contactinfo set phone =? where name=?", new Object[]{newphone,name});
        ContentValues values = new ContentValues();
        values.put("state", state);
        int rowcount = db.update("flag_read_state", values, "flag_id=?", new String[]{id});
        db.close();
        return rowcount;
    }

    /**
     * 查询联系人的电话号码
     *
     * @param id 要查询的联系人
     * @return 电话号码
     */
    public String getPhoneNumber(String id, String userid) {
        String phone = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        //Cursor  cursor = db.rawQuery("select phone from contactinfo where name=?", new String[]{name});
//		db.execSQL("select * from ,, where userid = '10' and status = '1'");

//		Cursor  cursor =  db.query("flag_read_state", new String[]{"state"}, "flag_id=? and flag_userid=?", new String[]{id,userid}, null, null, null);
        Cursor cursor = db.rawQuery("select state from flag_read_state where flag_userid=?and flag_id=?", new String[]{id, userid});

        while (cursor.moveToNext()) {
            phone = cursor.getString(0);
        }
//		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
//			phone = cursor.getString(0);
//			System.out.println("搜2索+"+phone);
//		}
        cursor.close();//关闭掉游标,释放资源
        db.close();//关闭数据库,释放资源
        return phone;
    }

    public int getPhoneNumbersize(String id) {
        int phone = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from flag_read_state where flag_id=?", new String[]{id});
        phone = cursor.getCount();

//		while (cursor.moveToNext()){
//			phone++;
//		}
        System.out.println("phonenumber" + phone);
//		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
//			phone = cursor.getString(0);
//			System.out.println("搜2索+"+phone);
//		}
        cursor.close();//关闭掉游标,释放资源
        db.close();//关闭数据库,释放资源
        return phone;
    }

    public List<String> getPhoneNumberby_userid(String userid) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        //Cursor  cursor = db.rawQuery("select phone from contactinfo where name=?", new String[]{name});
//		db.execSQL("select * from ,, where userid = '10' and status = '1'");

//		Cursor  cursor =  db.query("flag_read_state", new String[]{"state"}, "flag_id=? and flag_userid=?", new String[]{id,userid}, null, null, null);
        Cursor cursor = db.rawQuery("select flag_userid from flag_read_state where flag_id=?", new String[]{userid});

        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
//		if(cursor.moveToNext()){//如果光标可以移动到下一位,代表就是查询到了数据
//			phone = cursor.getString(0);
//			System.out.println("搜2索+"+phone);
//		}
        cursor.close();//关闭掉游标,释放资源
        db.close();//关闭数据库,释放资源
        return list;
    }
}
