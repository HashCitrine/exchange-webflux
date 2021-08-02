package com.exchange;

import com.exchange.redis.User;
//import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
//import io.r2dbc.postgresql.PostgresqlConnectionFactory;
//import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;


@SpringBootApplication
@EnableWebFlux
//@OpenAPIDefinition(info = @Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
public class ApiApplication /*extends AbstractR2dbcConfiguration*/ {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Value("${spring.r2dbc.url}")
	private String url;

	// 1. R2DBC 설정
//	@Bean
//	public ConnectionFactory connectionFactory() {
//		return new PostgresqlConnectionFactory(
//				PostgresqlConnectionConfiguration.builder()
//						.codecRegistrar(EnumCodec.builder().withEnum("order_type", Constants.ORDER_TYPE.class).build())
//						.build()
//		);
//	}

	@Bean
	public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
//		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("postgresql_script.sql"));
//		initializer.setDatabasePopulator(populator);
		return initializer;
	}

	// 2. Redis 설정
	@Autowired
	RedisConnectionFactory factory;

	@Bean
	public ReactiveRedisTemplate<String, User> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

		Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<>(User.class);

		RedisSerializationContext.RedisSerializationContextBuilder<String, User> builder =
				RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

		RedisSerializationContext<String, User> context = builder.value(serializer)
				.build();

		return new ReactiveRedisTemplate<>(factory, context);
	}

	/*@Override
	public ConnectionFactory connectionFactory() {
		return ConnectionFactories.get(url);
	}

	@Override
	protected List<Object> getCustomConverters() {
		return List.of(new RoleReadingConverter(), new RoleWritingConverter());
	}*/
}
