package org.ezlearn;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 定義允許 CORS 的路徑
				.allowedOrigins("http://127.0.0.1:5500", "http://192.168.31.170:5500", "http://10.0.104.199:5500","https://8fb5-118-163-218-100.ngrok-free.app") // 定義允許的來源
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 定義允許的 HTTP 方法
				.allowedHeaders("*") // 定義允許的 Header
				.allowCredentials(true); // 是否允許攜帶 Cookie
	}
}