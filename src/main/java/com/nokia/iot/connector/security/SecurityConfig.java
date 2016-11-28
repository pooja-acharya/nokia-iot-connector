package com.nokia.iot.connector.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import com.nokia.iot.connector.security.Util.AuthenticationUtil;
import com.nokia.iot.connector.security.filter.RegistrationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public CustomUserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationUtil authenticationUtil;
	
	protected void configure(HttpSecurity http) throws Exception {

		http.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		.antMatchers("/*").permitAll()
		.antMatchers("/**").permitAll()
		.anyRequest().authenticated();
		
		http.addFilterBefore(new RegistrationFilter(authenticationUtil), BasicAuthenticationFilter.class);
		http.csrf().disable();
		
		/*http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/subscribe/**").authenticated()
				.and().exceptionHandling()
				// this entry point handles when you request a protected page
				// and
				// you are not yet authenticated
				.authenticationEntryPoint(digestEntryPoint());
		http.csrf().disable();
		http.addFilter(digestAuthenticationFilter(digestEntryPoint()));*/
	}
	
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public DigestAuthenticationEntryPoint digestEntryPoint() {
		DigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new DigestAuthenticationEntryPoint();
		digestAuthenticationEntryPoint.setKey("acegi");
		digestAuthenticationEntryPoint.setRealmName("Digest MSM");
		return digestAuthenticationEntryPoint;
	}

	public DigestAuthenticationFilter digestAuthenticationFilter(
			DigestAuthenticationEntryPoint digestAuthenticationEntryPoint)
					throws Exception {
		DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
		digestAuthenticationFilter
		.setAuthenticationEntryPoint(digestEntryPoint());
		digestAuthenticationFilter.setUserDetailsService(userDetailsService);
		return digestAuthenticationFilter;
	}
	
	/*@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .httpBasic()
	    .and()
	      .authorizeRequests()
	        .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
	        .anyRequest().authenticated();
	  }
	}*/
}



/*@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username)
					throws UsernameNotFoundException {
				String password = "test";
				if ("test".equalsIgnoreCase(username)) {
					return new org.springframework.security.core.userdetails.User(
							username, password, true, true, true, true,
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException(
							"could not find the user '" + username + "'");
				}
			}
		};
	}*/

