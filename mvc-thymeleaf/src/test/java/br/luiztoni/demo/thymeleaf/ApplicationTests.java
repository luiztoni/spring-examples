package br.luiztoni.demo.thymeleaf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {


	@Autowired
	private MockMvc mvc;


	@Test
	void shouldAllowAccessHomePageWhenAnonimus() throws Exception {
		//https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/form-login.html
		this.mvc.perform(get("/home"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	public void loginWithValidUserThenAuthenticated() throws Exception {

		SecurityMockMvcRequestBuilders.FormLoginRequestBuilder login = formLogin()
				.user("email","admin@admin.com")
				.password("pass");

		mvc.perform(login)
				.andExpect(status().is3xxRedirection())
				.andExpect(authenticated());

	}

	@Test
	public void loginWithInvalidUserThenUnauthenticated() throws Exception {
		SecurityMockMvcRequestBuilders.FormLoginRequestBuilder login = formLogin()
				.user("email","invalid@email.com")
				.password("invalidpassword");

		mvc.perform(login)
				.andExpect(unauthenticated());
	}

	@Test
	public void accessUnsecuredResourceThenOk() throws Exception {
		mvc.perform(get("/js/form.js"))
				.andExpect(status().isOk());
	}
}
