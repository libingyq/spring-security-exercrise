/**
 * 
 */
package com.imooc.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import com.imooc.security.core.authentication.AbstractChannelSecurityConfig;
import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.imooc.security.core.properties.SecurityConstants;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeSecurityConfig;

/**
 * @author zhailiang
 *
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
	
	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;
	
	@Autowired
	private SpringSocialConfigurer imoocSocialSecurityConfig;
	
	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
	
	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;

	//这里就是退出登陆的处理器 写接口  会自动注入自己的实现类
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		applyPasswordAuthenticationConfig(http);
		
		http.apply(validateCodeSecurityConfig)
				.and()
			.apply(smsCodeAuthenticationSecurityConfig)
				.and()
			.apply(imoocSocialSecurityConfig)
				.and()
                /**
                 * 下面就是配置登录的时候“记住我remember-me”的功能
                 */
			.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsService)//这里在校验token后 根据得到的用户名进行登录操作
				.and()
				/**
				 * 下面配置的是session的失效和session并发问题
				 */
			.sessionManagement()
				//这里配置的是session失效后的跳转后的请求
//				.invalidSessionUrl("/session/invalid")
				//配置session策略
				.invalidSessionStrategy(invalidSessionStrategy)
				//同一个用户所拥有的session的数量（可以控制并发登陆）
				.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
				//当session数量达到最大以后 会阻止后面用户的session登陆
				.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
				.expiredSessionStrategy(sessionInformationExpiredStrategy)
				.and()
				.and()
				//这里配置的推出登陆的相关配置
			.logout()
				//这里配置的defineUrl 表示的意思的推出登陆的时候的请求链接地址
				.logoutUrl("/defineUrl")
				//这里表示的意思是 退出成功拦截器会做一些自定义的处理
				.logoutSuccessHandler(logoutSuccessHandler)
				//将浏览器中的cookie进行删除操作
				.deleteCookies("JSESSIONID")
				.and()
			.authorizeRequests()
				.antMatchers(
					SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
					SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
					securityProperties.getBrowser().getLoginPage(),
					SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
					securityProperties.getBrowser().getSignUpUrl(),
					securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".json",
					securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".html",
					"/user/regist")
					.permitAll()
				.anyRequest()
				.authenticated()
				.and()
			.csrf().disable();
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		//若你的系统不想使用这个实现类例如你使用的是MD5加密的方式
		/**
		 * 那么你就要使用这个你的实现类去实现passwordEncoder
		 */
		return new BCryptPasswordEncoder();
	}


    /**
     * 这里创建TokenRepository 来配置操作token
     * @return
     */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}
	
}
