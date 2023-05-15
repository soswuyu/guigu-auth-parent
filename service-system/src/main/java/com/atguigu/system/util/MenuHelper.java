package com.atguigu.system.util;/**
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

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: menuHelper
 * Package: com.atguigu.system.util
 * Description:
 * @Author 张 文 强
 * @Create 2023/5/10 18:27
 * @Version 1.0
 */
public class MenuHelper {
    //构建树形数据
    public static List<SysMenu> buildTree(List<SysMenu> menuList) {
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            if (sysMenu.getParentId() == 0){
                //递归添加子节点
                trees.add(findChildRen(sysMenu,menuList));
            }
        }
        return trees;
    }
    //递归添加子节点
    private static SysMenu findChildRen(SysMenu sysMenu, List<SysMenu> menuList) {
        sysMenu.setChildren(new ArrayList<>());
        menuList.forEach(item ->{
            if (sysMenu.getId().longValue() == item.getParentId().longValue()){
                /*if (sysMenu.getChildren() == null){
                    sysMenu.setChildren(new ArrayList<>());
                }*/
                sysMenu.getChildren().add(findChildRen(item,menuList));
            }
        });
        return sysMenu;
    }

}
