package com.sugon.wwb.test;

import com.google.common.collect.ImmutableMap;

public class Constant  {

		public static  String JSON_AUTH_PROJECT = "/identity/v3/authv3_project.json";
	    public static  String JSON_AUTH_DOMAIN = "/identity/v3/authv3_domain.json";
	    public static  String JSON_AUTH_TOKEN = "/identity/v3/authv3_token.json";
	    public static  String JSON_AUTH_TOKEN_UNSCOPED = "/identity/v3/authv3_token_unscoped.json";
	    public static  String JSON_AUTH_UNSCOPED = "/identity/v3/authv3_unscoped.json";
	    public static  String JSON_AUTH_ERROR_401 = "/identity/v3/authv3_authorizationerror.json";
	    public static  String JSON_USERS = "/identity/v3/users.json";
	    public static  ImmutableMap<String, String> HEADER_AUTH_PROJECT_RESPONSE = ImmutableMap.of("X-Subject-Token", "763fd7e197ab4e00b2e6e0a8d22a8e87", "Content-Type", "application/json");
	    public static  ImmutableMap<String, String> HEADER_AUTH_TOKEN_RESPONSE = ImmutableMap.of("X-Subject-Token", "3ecb5c2063904566be4b10406c0f7568", "Content-Type", "application/json");
	    public static  ImmutableMap<String, String> HEADER_REAUTH_TOKEN_RESPONSE = ImmutableMap.of("X-Subject-Token", "3e3f7ec1180e4f1b8ca884d32e04ccfb", "Content-Type", "application/json");
	    public static  ImmutableMap<String, String> HEADER_REAUTH_PROJECT_RESPONSE = ImmutableMap.of("X-Subject-Token", "8f57cce49fd04b3cb72afdf8c0445b87", "Content-Type", "application/json");

	    public static  String USER_NAME = "admin";
	    public static  String USER_ID = "aa9f25defa6d4cafb48466df83106065";
	    public static  String DOMAIN_ID = "default";
	    public static  String DOMAIN_NAME = "Default";
	    public static  String PROJECT_ID = "123ac695d4db400a9001b91bb3b8aa46";
	    public static  String PROJECT_NAME = "admin";
	    public static  String PROJECT_DOMAIN_ID = "default";
	    public static  String PASSWORD = "test";
	    public static  String REGION_EUROPE = "europe";
	    public static  String TOKEN_UNSCOPED_ID = "3ecb5c2063904566be4b10406c0f7568";

}
