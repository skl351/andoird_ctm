package com.ayi.utils;

import android.content.Context;

import com.ayi.entity.item_place_info;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by android on 2016/3/9.
 */
public class FileService {
    private Context context;
    public FileService(Context mcontext){
        context=mcontext;
    }

    /**
     16      * 把用户名和密码保存到手机ROM
     17      * @param password 输入要保存的密码
     18      * @param username 要保存的用户名
     19      * @param filename 保存到哪个文件
     20      * @return
     21      */
    public boolean saveToRom(String filename,String content) throws Exception{
        FileOutputStream outputStream=context.openFileOutput(filename,Context.MODE_PRIVATE);
        outputStream.write(content.getBytes("utf-8"));
        outputStream.close();
        System.out.println("写入内容"+content);
        return true;
    }
    public item_place_info read(String filename) throws Exception {

        FileInputStream instream=context.openFileInput(filename);
        item_place_info user_info=new item_place_info();
        if (instream==null){
            user_info.setName("");
            user_info.setPhone("");
            user_info.setNum_place("");
            user_info.setPlace("");
            user_info.setLatitude("");
            user_info.setLongitide("");
            user_info.setShiji_dizhi("");
        }else{
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int len=0;
            while((len=instream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            byte[] date=outputStream.toByteArray();
            String tt=new String(date,"utf-8");
            System.out.println(tt+"+++");
            if (tt!=null&&!tt.equals("")){
                String[] gg=tt.split("~");
                user_info.setName(gg[0]);
                user_info.setPhone(gg[1]);
                user_info.setPlace(gg[2]);
                user_info.setNum_place(gg[3]);
                user_info.setLatitude(gg[4]);
                user_info.setLongitide(gg[5]);
                user_info.setShiji_dizhi(gg[6]);
            }else {
                user_info.setName("");
                user_info.setPhone("");
                user_info.setNum_place("");
                user_info.setPlace("");
                user_info.setLatitude("");
                user_info.setLongitide("");
                user_info.setShiji_dizhi("");
            }

        }

        return  user_info;
    }

}
