package aifaas.cloud.outsourcing.common.config;

import aifaas.cloud.outsourcing.common.utils.StringUtils;
import aifaas.cloud.outsourcing.common.utils.YamlUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 */
@Slf4j
public class Global
{
    private static String NAME = "application.yml";

    /**
     * 当前对象实例
     */
    private static Global global;

    public static final long ROOT_DEPT_ID=100L;
    /**
     * 顶级管理员id
     */
    public static final long TOP_ADMIN_ID=1L;

    public static final long ROLE_ID_DEFAULT=2L;

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<String, String>();

    private Global()
    {
    }

    /**
     * 静态工厂方法
     */
    public static synchronized Global getInstance()
    {
        if (global == null)
        {
            global = new Global();
        }
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key)
    {
        String value = map.get(key);
        if (value == null)
        {
            Map<?, ?> yamlMap = null;
            try
            {
                yamlMap = YamlUtil.loadYaml(NAME);
                value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                map.put(key, value != null ? value : StringUtils.EMPTY);
            }
            catch (FileNotFoundException e)
            {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }

    /**
     * 获取项目名称
     */
    public static String getName()
    {
        return StringUtils.nvl(getConfig("ruoyi.name"), "RuoYi");
    }

    /**
     * 获取项目版本
     */
    public static String getVersion()
    {
        return StringUtils.nvl(getConfig("ruoyi.version"), "3.3.0");
    }

    /**
     * 获取版权年份
     */
    public static String getCopyrightYear()
    {
        return StringUtils.nvl(getConfig("ruoyi.copyrightYear"), "2018");
    }

    /**
     * 获取ip地址开关
     */
    public static Boolean isAddressEnabled()
    {
        return Boolean.valueOf(getConfig("ruoyi.addressEnabled"));
    }

    /**
     * 获取文件上传路径
     */
    public static String getProfile()
    {
        return getConfig("ruoyi.profile");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getConfig("ruoyi.profile") + "avatar/";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getConfig("ruoyi.profile") + "download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getConfig("ruoyi.profile") + "upload/";
    }


    public static Integer getTcpPort(){
        return Integer.parseInt(StringUtils.nvl(getConfig("client.tcpPort"), "8885"));
    }
    public static String getCharacterSet(){
        return StringUtils.nvl(getConfig("client.characterSet"), "GBK");
    }
    public static Integer getMaxConns(){
        return Integer.parseInt(StringUtils.nvl(getConfig("client.maxConns"), "2000"));
    }
}
