/**
 * 
 */
package com.imooc.security.core.social;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import com.imooc.security.core.properties.SecurityProperties;

/**
 * @author zhailiang
 *
 */
@Configuration
@EnableSocial//开启关于Social相关的一些配置
public class SocialConfig extends SocialConfigurerAdapter {

    /**
     * 这是我们自己配置的数据库
     */
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired(required = false)
	private ConnectionSignUp connectionSignUp;

    /**
     *
     * @param connectionFactoryLocator  作用就是为了获取ConnectionFactory 使用的是spring-social提供的默认的
	 *                                  Encryptors.noOpText() 加密和解密的工具
	 *
	 *        这里是创建了一个表 用来存放链接信息
     * @return
     */
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		repository.setTablePrefix("imooc_");
		if(connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}

	@Bean
	public SpringSocialConfigurer imoocSocialSecurityConfig() {
		String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
		ImoocSpringSocialConfigurer configurer = new ImoocSpringSocialConfigurer(filterProcessesUrl);
		//这里配置的是注册页面
        /**
         * 当QQ授权登录的时候，登录成功去数据库中获取对应的userid为null的时候，会默认将用户重定向到/signUp的请求页中
         */
		configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
		return configurer;
	}

	/**
	 * 该类的作用有两个：
	 * 		1.用户在注册时候如何拿到spring-social的信息（也就是authentication的信息）
	 * 		2.注册完成了 如何把业务系统的userId传给spring-social 和authentication的信息绑定到一起
	 * @param connectionFactoryLocator
	 * @return
	 */
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
		return new ProviderSignInUtils(connectionFactoryLocator,
				getUsersConnectionRepository(connectionFactoryLocator)) {
		};
	}
}
