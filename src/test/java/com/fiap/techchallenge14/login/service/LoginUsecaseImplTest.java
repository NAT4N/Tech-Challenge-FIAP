package com.fiap.techchallenge14.login.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginUsecaseImplTest {

   /* @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private LoginUseCaseImpl loginAdapter;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Guilherme");
        user.setLogin("guilherme");
        user.setPassword("123");
        user.setActive(true);
    }

    @Test
    void login_ShouldAuthenticateAndReturnToken() {
        // Arrange
        when(userRepository.findByLoginAndPassword("guilherme", "123"))
                .thenReturn(Optional.of(user));
        when(timeProvider.now()).thenReturn(LocalDateTime.now());

        // Act
        LoginResponseDTO result = loginAdapter.login(new LoginRequestDTO("guilherme", "123"));

        // Assert
        assertNotNull(result);
        assertNotNull(result.token());
        assertDoesNotThrow(() -> UUID.fromString(result.token()));

        verify(userRepository).findByLoginAndPassword("guilherme", "123");
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser.getLastLoginAt());
        assertTrue(savedUser.getLastLoginAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        verify(tokenService).saveToken(anyString(), eq(1L));
    }

    @Test
    void login_ShouldThrowException_WhenCredentialsInvalid() {
        // Arrange
        when(userRepository.findByLoginAndPassword("wrong", "invalid"))
                .thenReturn(Optional.empty());

        // Act & Assert
        LoginException ex = assertThrows(LoginException.class,
                () -> loginAdapter.login(new LoginRequestDTO("wrong", "invalid")));

        assertEquals("Login ou senha inválidos", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldThrowException_WhenUserInactive() {
        // Arrange
        user.setActive(false);
        when(userRepository.findByLoginAndPassword("guilherme", "123"))
                .thenReturn(Optional.of(user));

        // Act & Assert
        LoginException ex = assertThrows(LoginException.class,
                () -> loginAdapter.login(new LoginRequestDTO("guilherme", "123")));

        assertEquals("Login ou senha inválidos", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateLastLogin_ShouldSetTimestampAndSave() {
        // Arrange
        when(userRepository.findByLoginAndPassword("guilherme", "123"))
                .thenReturn(Optional.of(user));
        LocalDateTime fixed = LocalDateTime.now();
        when(timeProvider.now()).thenReturn(fixed);

        // Act
        loginAdapter.login(new LoginRequestDTO("guilherme", "123"));

        // Assert
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser.getLastLoginAt());
        assertEquals(fixed, savedUser.getLastLoginAt());
    }*/
}
