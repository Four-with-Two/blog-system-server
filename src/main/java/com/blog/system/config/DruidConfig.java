package com.blog.system.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GGXian
 * @project MiniBlog
 * @createTime 2020/10/26 15:42
 * @description 数据库连接池配置
 **/
@EnableTransactionManagement
@EnableConfigurationProperties
@Configuration
public class DruidConfig {
    /**
     * 当前主机的处理器核心数
     */
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    /**
     * 根据PostgreSQL提供的公式，连接数 = ((核心数 * 2) + 有效磁盘数)，在大多数情况下有效磁盘数都为1
     */
    private static final int CONNECTIONS = PROCESSORS * 2 + 1;

    @ConfigurationProperties("spring.datasource")
    @Bean("druidDataSource")
    public DataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setMaxActive(CONNECTIONS);
        druidDataSource.setMinIdle(CONNECTIONS / 2);
        return druidDataSource;
    }

    @Bean("statViewServlet")
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 这些参数可以在 com.alibaba.druid.support.http.StatViewServlet的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
        Map<String, String> initParams = new HashMap<>(10);
        //后台管理界面的登录账号
        initParams.put("loginUsername", "404");
        //后台管理界面的登录密码
        initParams.put("loginPassword", "123456");
        //后台允许谁可以访问
        //initParams.put("allow", "localhost")：表示只有本机可以访问
        //initParams.put("allow", "")：为空或者为null时，表示允许所有访问
        initParams.put("allow", "");
        //deny：Druid 后台拒绝谁访问
        //设置初始化参数
        bean.setInitParameters(initParams);
        return bean;
    }
}
