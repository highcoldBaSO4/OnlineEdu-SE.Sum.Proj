package com.se231.onlineedu;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.google.common.base.Predicate;

/**
 * Swagger2 Configuration Class
 *
 * configure swagger2 to generate api doc
 *
 * @author Zhe Li
 *
 * @date 2019/07/09
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     *  构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("online-edu api")
                .description("在线学习平台的swagger2 api文档")
                //创建人
                .termsOfServiceUrl("http://202.120.40.8:30382/online-edu")
                .contact(new Contact("Zhe Li",
                        "http://202.120.40.8:30382/online-edu",
                        "lz18064638846@sjtu.edu.cn"))
                //版本号
                .version("1.0")
                //描述
                .build();
    }
}
