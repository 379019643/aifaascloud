package aifaas.cloud.outsourcing.framework.config;

import aifaas.cloud.outsourcing.common.enums.DataSourceType;
import aifaas.cloud.outsourcing.framework.config.properties.DruidProperties;
import aifaas.cloud.outsourcing.framework.datasource.DynamicDataSource;
import aifaas.cloud.outsourcing.framework.datasource.SysDatasource;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * druid 配置多数据源
 */
@Configuration
public class DruidConfig
{
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource, DataSource slaveDataSource)
    {
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
//        targetDataSources.put(DataSourceType.SLAVE.name(), slaveDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        // 设置数据源列表
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
        // 从数据库中直接读取数据库
        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource);
        List<SysDatasource> dsList = jdbcTemplate.query("select * from sys_datasource", new Object[] {},
                new BeanPropertyRowMapper<SysDatasource>(SysDatasource.class));
        if (dsList != null) {
            for (SysDatasource ds : dsList) {
                DruidDataSource dds = new DruidDataSource();
                dds.setUrl(ds.getUrl());
                dds.setUsername(ds.getUser());
                dds.setPassword(ds.getPwd());
                dds.setDriverClassName("com.mysql.cj.jdbc.Driver");
                targetDataSources.put(ds.getName(), dds);
            }
        }
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
}
