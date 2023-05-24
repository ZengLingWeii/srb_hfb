package com.atguigu.srb.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
* Swagger2 接口文档 查询地址
* http://localhost:8110/swagger-ui.html
* http://localhost:8120/swagger-ui.html
* http://localhost:8130/swagger-ui.html
* Swagger bootstrap ui
* http://localhost:8110/doc.html
* */

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                //组别名称
                .groupName("管理者API")
                //调用方法 ---> 文档描述
                .apiInfo(adminApiInfo())
                .select()
                //过滤器的路径设置 ---> 路由地址查找
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    private ApiInfo adminApiInfo(){
        return  new ApiInfoBuilder()
                //文档描述的标题
                .title("尚融宝后台管理系统-API文档")
                //文档的具体描述
                .description("本文档描述了尚融宝后台管理接口")
                //版本号
                .version("1.0")
                //作者 + 地址 + 邮箱
                .contact(new Contact("zlw","http://atguigu.com","1358383290@qq.com"))
                .build();
    }

    @Bean
    public Docket WebApipiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("WebApi")
                .apiInfo(WebApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();

    }

    private ApiInfo WebApiInfo(){

        return new ApiInfoBuilder()
                .title("尚融宝后台网站API文档")
                .description("本文档描述了尚融宝网站各个模块接口的调用方式")
                .version("1.0")
                .contact(new Contact("zlw","http://atguigu.com","1358383290@qq.com"))
                .build();
    }

}
