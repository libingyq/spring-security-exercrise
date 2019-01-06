/**
 * 
 */
package com.imooc.security.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.imooc.security.core.properties.SecurityProperties;

/**
 * @author zhailiang
 *  它的组作用就是讲配置中的路径生效imooc.security.browser.loginPage = /demo-signIn.html
 *
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

}
