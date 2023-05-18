package com.ruoyi.common.exception.api;

import com.ruoyi.common.exception.base.BaseException;
import lombok.Data;

/**
 * 用户信息异常类
 *
 * @author ruoyi
 */
@Data
public class SuccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SuccessException(String message) {
        super(message);
    }
}
