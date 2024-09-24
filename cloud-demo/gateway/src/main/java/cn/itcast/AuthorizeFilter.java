package cn.itcast;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 * @author Julring
 * @Order(-1) 值越小，优先级越高
 */
@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求中的查询参数
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        // 获取authorization参数的值
        String auth = queryParams.getFirst("authorization");
        // 如果authorization参数的值为'admin'，则允许请求继续
        if ("admin".equals(auth)) {
            return chain.filter(exchange);
        }
        // 如果authorization参数的值不是'admin'，则拒绝请求，设置状态码为未授权
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 返回一个完成信号，表示处理完成
        return exchange.getResponse().setComplete();
    }

}
