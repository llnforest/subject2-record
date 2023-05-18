package com.ruoyi.framework.aspectj;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Sign;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.api.FailException;
import com.ruoyi.common.filter.RepeatedlyRequestWrapper;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 防止重复提交(参考美团GTIS防重系统)
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class SignAspect {

    @Before("@annotation(sign)")
    public void doBefore(JoinPoint point, Sign sign) throws Throwable {
        String sign1 = ServletUtils.getRequest().getParameter("sign");
        if (sign.verifyTime()) {
            try{
                Long time = Long.valueOf(ServletUtils.getRequest().getParameter("time"));
                if(time + 10 < new Date().getTime()) throw new FailException("时间戳已过期");

            }catch (Exception exception){
                throw new FailException("时间戳异常");
            }
        }
        if(ServletUtils.getRequest() instanceof RepeatedlyRequestWrapper){
            BufferedReader reader = ServletUtils.getRequest().getReader();
            String read = IoUtil.read(reader);
            StringBuilder sb = new StringBuilder();
            JsonUtils.parseMap(read).forEach((key,value) -> {
                sb.append(value);
            });
            sb.append(sign.key());
            log.info(sb.toString());
//            String a = DigestUtil.md5Hex(sb.toString());
//            String b = SecureUtil.md5(sb.toString());
//            String sign2 = Md5Crypt.md5Crypt(sb.toString().getBytes(StandardCharsets.UTF_8));
//            log.info(sign2);
        }


    }

}
