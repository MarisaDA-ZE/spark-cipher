package top.kirisamemarisa.sparkcipher.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Marisa
 * @Description 通用返回类
 * @Date 2023/4/22
 */
@Data
@ToString
public class MrsResult<T> implements Serializable {
    private static final long serialVersion = 1L;

    private int code;
    private boolean status;
    private long timestamp;
    private String msg;
    private T data;

    public MrsResult(int code) {
        this.code = code;
        this.status = true;
        this.timestamp = System.currentTimeMillis();
        this.msg = "操作成功！";
        this.data = null;
    }

    public MrsResult(String msg) {
        this.code = 200;
        this.status = true;
        this.timestamp = System.currentTimeMillis();
        this.msg = msg;
        this.data = null;
    }

    public MrsResult(T data) {
        this.code = 200;
        this.status = true;
        this.timestamp = System.currentTimeMillis();
        this.msg = "操作成功！";
        this.data = data;
    }

    public MrsResult(int code, String msg) {
        this.code = code;
        this.status = true;
        this.timestamp = System.currentTimeMillis();
        this.msg = msg;
        this.data = null;
    }

    public MrsResult(int code, boolean status, String msg) {
        this.code = code;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
        this.msg = msg;
        this.data = null;
    }

    public MrsResult(int code, String msg, T data) {
        this.code = code;
        this.status = true;
        this.timestamp = System.currentTimeMillis();
        this.msg = msg;
        this.data = data;
    }

    public MrsResult(int code, boolean status, Date dateTime, String msg, T data) {
        this.code = code;
        this.status = status;
        this.timestamp = dateTime.getTime();
        this.msg = msg;
        this.data = data;
    }

    private static String format(Date dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return format.format(dateTime);
    }

    public static <T> MrsResult<T> ok() {
        return new MrsResult<>("操作成功！");
    }

    public static <T> MrsResult<T> ok(int code) {
        return new MrsResult<>(code);
    }

    public static <T> MrsResult<T> ok(String msg) {
        return new MrsResult<>(msg);
    }

    public static <T> MrsResult<T> ok(T data) {
        return new MrsResult<>(data);
    }

    public static <T> MrsResult<T> ok(int code, String msg) {
        return new MrsResult<>(code, msg);
    }

    public static <T> MrsResult<T> ok(String msg, T data) {
        return new MrsResult<>(200, msg, data);
    }

    public static <T> MrsResult<T> ok(int code, String msg, T data) {
        return new MrsResult<>(code, msg, data);
    }

    public static <T> MrsResult<T> failed() {
        return new MrsResult<>(500, false, "操作失败！");
    }

    public static <T> MrsResult<T> failed(int code) {
        return new MrsResult<>(code, false, "操作失败！");
    }

    public static <T> MrsResult<T> failed(String msg) {
        return new MrsResult<>(500, false, msg);
    }

    public static <T> MrsResult<T> failed(int code, String msg) {
        return new MrsResult<>(code, false, msg);
    }

    public static <T> MrsResult<T> failed(int code, String msg, T data) {
        return new MrsResult<>(code, false, new Date(), msg, data);
    }
}
