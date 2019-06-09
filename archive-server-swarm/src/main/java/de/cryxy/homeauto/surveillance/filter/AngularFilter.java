/**
 * 
 */
package de.cryxy.homeauto.surveillance.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class AngularFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String contextPath = httpRequest.getContextPath();
		String requestURI = httpRequest.getRequestURI();
		String angularPath = requestURI.substring(contextPath.length());
		if (isAngularRoute(angularPath)) {
			request.getRequestDispatcher("/index.html").forward(request, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isAngularRoute(String angularPath) {
		return
		/*
		 * Do not reroute files in the Angular root, such as index.html, main.js,
		 * polyfills.js, runtime.js, styles.css and favicon.ico
		 */
		((angularPath.indexOf('/', 1) != -1) || (angularPath.indexOf('.') == -1))
				/* Do not reroute static files */
				&& !angularPath.startsWith("/assets/")
				/*
				 * Do not reroute the api.
				 */
				&& !angularPath.contains("api");
	}

	@Override
	public void destroy() {
	}

}
