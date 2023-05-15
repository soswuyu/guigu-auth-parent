package com.atguigu.system.handlerexception;/**
 * 2  *                             _ooOoo_
 * 3  *                            o8888888o
 * 4  *                            88" . "88
 * 5  *                            (| -_- |)
 * 6  *                            O\  =  /O
 * 7  *                         ____/`---'\____
 * 8  *                       .'  \\|     |//  `.
 * 9  *                      /  \\|||  :  |||//  \
 * 10  *                     /  _||||| -:- |||||-  \
 * 11  *                     |   | \\\  -  /// |   |
 * 12  *                     | \_|  ''\---/''  |   |
 * 13  *                     \  .-\__  `-`  ___/-. /
 * 14  *                   ___`. .'  /--.--\  `. . __
 * 15  *                ."" '<  `.___\_<|>_/___.'  >'"".
 * 16  *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * 17  *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 * 18  *          ======`-.____`-.___\_____/___.-`____.-'======
 * 19  *                             `=---='
 * 20  *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 21  *                     佛祖保佑        永无BUG
 * 22  *            佛曰:
 * 23  *                   写字楼里写字间，写字间里程序员；
 * 24  *                   程序人员写程序，又拿程序换酒钱。
 * 25  *                   酒醒只在网上坐，酒醉还来网下眠；
 * 26  *                   酒醉酒醒日复日，网上网下年复年。
 * 27  *                   但愿老死电脑间，不愿鞠躬老板前；
 * 28  *                   奔驰宝马贵者趣，公交自行程序员。
 * 29  *                   别人笑我忒疯癫，我笑自己命太贱；
 * 30  *                   不见满街漂亮妹，哪个归得程序员？
 * 31
 */

import com.atguigu.common.result.Result;
import com.atguigu.system.exception.CustomException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * ClassName: GlobalExceptionHandler
 * Package: com.atguigu.system.exception
 * Description:
 * @Author 张 文 强
 * @Create 2023/5/7 12:50
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    //指定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("算数异常");
    }

    //自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e){
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody   //返回json格式数据
    public Result error(AccessDeniedException e) {
        e.printStackTrace();
        return Result.fail().code(403).message("没有访问权限");
    }

}
