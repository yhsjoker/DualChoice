package cn.edu.bjfu.dualchoice.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE_PATH = "application.log"; // 日志文件路径

    // 记录日志的方法，接收 Object 类型的内容、操作类型和接口名称
    public static void log(String userIdentity, int userId, Object content, LogType logType, String apiName) {
        // 获取当前时间
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 将 Object 类型的 content 转换为字符串
        String logContent = String.format("[%s] [%s] API: %s User: %s (ID: %d) - %s%n",
                currentTime, logType, apiName, userIdentity, userId, String.valueOf(content));

        // 写入日志到文件
        writeLogToFile(logContent);
    }

    // 将日志写入文件
    private static void writeLogToFile(String logContent) {
        BufferedWriter writer = null;
        try {
            // 创建日志文件，如果文件不存在则创建
            File logFile = new File(LOG_FILE_PATH);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            // 以追加的方式写入日志
            writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(logContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭文件流
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 枚举类型：日志级别，区分合法操作与非法操作
    public enum LogType {
        INFO,    // 合法操作
        ERROR,    // 非法操作
        SUCCESS
    }
}
