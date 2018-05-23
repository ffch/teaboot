package com.teaboot.security.configuration;

import com.teaboot.security.http.HttpSecurity;

public class CommonWebSecurityConfigurer extends WebSecurityConfigurer{
	public CommonWebSecurityConfigurer(){
		
	}
	
	@Override
	public void configure(HttpSecurity http){
		http
	  	.formLogin().loginPage("/login.html")
	  	.usernameParameter("userName").passwordParameter("userPwd")
	  	.loginProcessingUrl("/login")
	  	.and().logout().logoutUrl("/logout").loginOutPage("/index.html");

		http.authorizeRequests().antMatchers("/task.*").authenticated();
	}
	
	
	
}
