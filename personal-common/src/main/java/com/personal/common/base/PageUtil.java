package com.personal.common.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author liuyuzhu
 * @description: 分页工具类
 * @date 2018/1/20 1:15
 */
@Setter
@Getter
@ToString
public class PageUtil {

    private Integer startRow;

    private Integer rowLength;

    private List data;

    private Integer total;

}
