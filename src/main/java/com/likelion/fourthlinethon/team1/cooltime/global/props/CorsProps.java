package com.likelion.fourthlinethon.team1.cooltime.global.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsProps(List<String> allowedOrigins) {}