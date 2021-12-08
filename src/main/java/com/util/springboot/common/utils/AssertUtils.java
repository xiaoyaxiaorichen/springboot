package com.util.springboot.common.utils;

import com.util.springboot.enums.ResultEnums;
import com.util.springboot.global.exception.utils.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 *
 * Assertion utility class that assists in validating arguments.
 *
 * <p>Useful for identifying programmer errors early and clearly at runtime.
 *
 * <p>For example, if the contract of a public method states it does not
 * allow {@code null} arguments, {@code Assert} can be used to validate that
 * contract. Doing this clearly indicates a contract violation when it
 * occurs and protects the class's invariants.
 *
 * <p>Typically used to validate method arguments rather than configuration
 * properties, to check for cases that are usually programmer errors rather
 * than configuration errors. In contrast to configuration initialization
 * code, there is usually no point in falling back to defaults in such methods.
 *
 * <p>This class is similar to JUnit's assertion library. If an argument value is
 * deemed invalid, an {@link IllegalArgumentException} is thrown (typically).
 * For example:
 *
 * <pre class="code">
 * Assert.notNull(clazz, "The class must not be null");
 * Assert.isTrue(i > 0, "The value must be greater than zero");
 * </pre>
 *
 * @author util
 * @date 2016/11/24
 * @description Assert utils class
 */
public abstract class AssertUtils {

    /**
     * Assert args is null, if not null throw new {@link com.util.springboot.global.exception.ResponseException}
     * use resultEnums {@link com.util.springboot.enums.ResultEnums}
     *
     * @param resultEnums response info
     * @param args need Assert args value
     */
    public static void isNull(ResultEnums resultEnums, Object ... args) {
        if (null == args) {
            return;
        }
        for (Object arg : args) {
            if (null != arg) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
            if (arg instanceof String && StringUtils.isNotBlank(arg.toString())) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
            if (arg instanceof Collection && ((Collection<?>) arg).size() > 0) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
            if (arg instanceof Map && ((Map<?, ?>) arg).size() > 0) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
        }
    }

    /**
     * Assert args is not null,if null throw new {@link com.util.springboot.global.exception.ResponseException}
     * use resultEnums {@link com.util.springboot.enums.ResultEnums}
     *
     * @param resultEnums response info
     * @param args need Assert args value
     */
    public static void notNull(ResultEnums resultEnums, Object ... args) {
        if (null == args) {
            ExceptionUtils.throwResponseException(resultEnums);
        }
        for(Object arg : args){
            if (null == arg) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
            if (arg instanceof String && StringUtils.isBlank(arg.toString())) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
            if (arg instanceof Collection && ((Collection<?>) arg).size() == 0) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
            if (arg instanceof Map && ((Map<?,?>) arg).size() == 0) {
                ExceptionUtils.throwResponseException(resultEnums);
            }
        }
    }

    /**
     * Assert a boolean expression, throwing {@link com.util.springboot.global.exception.ResponseException}
     * if the test context is {@code false}.
     * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
     *
     * @param resultEnums response info
     * @param expression need Assert boolean expression
     */
    public static void isTrue(ResultEnums resultEnums, boolean expression) {
        if (!expression) {
            ExceptionUtils.throwResponseException(resultEnums);
        }
    }

    /**
     * Assert text must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isNumber(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isNumber(text))
            ExceptionUtils.throwResponseException(resultEnums);
    }

    /**
     * Assert mobile must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isMobile(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isMobileNumber(text))
            ExceptionUtils.throwResponseException(resultEnums);
    }

    /**
     * Assert id card must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isIdCard(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isIdCard(text))
            ExceptionUtils.throwResponseException(resultEnums);

    }

    /**
     * Assert tel phone must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isTelPhone(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isTelPhone(text))
            ExceptionUtils.throwResponseException(resultEnums);
    }

    /**
     * Assert zip code must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isZipCode(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isZipCode(text))
            ExceptionUtils.throwResponseException(resultEnums);
    }

    /**
     * Assert Chinese characters must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isChinese(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isChinese(text))
            ExceptionUtils.throwResponseException(resultEnums);
    }

    /**
     * Assert email must be match the patter
     * if the match the patter context false.
     * throw new {@link com.util.springboot.global.exception.ResponseException}
     *
     * @param resultEnums response info
     * @param text Assert text
     */
    public static void isEmail(ResultEnums resultEnums, String text) {
        if(StringUtils.isBlank(text))
            ExceptionUtils.throwResponseException(resultEnums);
        if(!RegexUtils.isEmail(text))
            ExceptionUtils.throwResponseException(resultEnums);
    }



}
