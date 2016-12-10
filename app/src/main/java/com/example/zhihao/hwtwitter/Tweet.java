package com.example.zhihao.hwtwitter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhihao on 12/9/2016.
 */

public class Tweet {
    String content;
    String user;
    String createTime;

    private Tweet() { }

    public Tweet(String content, String user) {
        this.content = content;
        this.user = user;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        this.createTime = sdf.format(new Date());
    }

    @Override
    public String toString() {
        return "[" + user + "][" + createTime + "]: " + content;
    }
}
