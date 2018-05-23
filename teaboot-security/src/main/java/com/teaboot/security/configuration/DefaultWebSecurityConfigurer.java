package com.teaboot.security.configuration;

import com.teaboot.security.http.HttpSecurity;

public class DefaultWebSecurityConfigurer extends WebSecurityConfigurer{
	public DefaultWebSecurityConfigurer(){
		changeLax();	
	}
	
	@Override
	public void configure(HttpSecurity http){
		http
	  	.authorizeRequests()
	  	.anyRequest()
	  	.permitAll();
	}
	
}
