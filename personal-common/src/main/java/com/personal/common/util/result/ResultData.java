package com.personal.common.util.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyuzhu
 * @description: 返回信息类
 * @date 2018/1/20 1:38
 */
@Setter
@Getter
public class ResultData {
    /**
     * 标识，成功：true，失败：false
     */
    private boolean result = true;

    /**
     * 消息提示
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    public ResultData(){
    }

    public ResultData(Object data){
        this.data = data;
    }

    public ResultData(boolean result,String message){
        this.result = result;
        this.message = message;
    }
}
