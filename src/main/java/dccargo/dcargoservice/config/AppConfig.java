package dccargo.dcargoservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration

@PropertySource("file:C:/YardConfig/config.properties")
public class AppConfig {

    @Value("${datasource}")
    private String datasource;

    @Value("${usrnm}")
    private String usrnm;

    @Value("${password}")
    private String password;

    @Value("${dbIpSource}")
    public String dbIpSource;

    @Value("${dbPortSource}")
    public String dbPortSource;

    @Value("${tableSettingPath}")
    private String tableSettingPath;

    public String getTableSettingPath(String tableName) {
        return tableSettingPath + "/" + tableName;
    }

    @Value("${ip}")
    private String ip;

    @Value("${gatewayport}")
    public String gatewayport;

    @Value("${dcargoPort}")
    private Integer qualityport;

}
