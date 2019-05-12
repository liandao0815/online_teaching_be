package com.liandao.onlineteaching.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader(this.header);

        if (token != null && token.startsWith(this.tokenPrefix)) {
            String account = "";
            try {
                String jwtBody = Jwts.parser()
                        .setSigningKey(this.secret)
                        .parseClaimsJws(token.replace(this.tokenPrefix, ""))
                        .getBody()
                        .getSubject();
                Map<String, String> userInfo = this.getInfoFromJwtBody(jwtBody);
                String id = userInfo.get("id");
                String uid = request.getParameter("uid");

                if (!id.isEmpty() && id.equals(uid)) {
                    account = userInfo.get("account");
                }
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
            }

            if (!account.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(account);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            if (account.isEmpty()) SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(request, response);
    }

    private Map<String, String> getInfoFromJwtBody(String jwtBody) {
        Map<String, String> map = new HashMap<>();
        map.put("account", "");
        map.put("id", "0");

        if (jwtBody.contains(":")) {
            String account = jwtBody.substring(0, jwtBody.lastIndexOf(":"));
            String id = jwtBody.substring(jwtBody.lastIndexOf(":") + 1);

            map.put("account", account);
            map.put("id", id);
        }

        return map;
    }
}
