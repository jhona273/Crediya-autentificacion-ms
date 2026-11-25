package co.com.crediya.api;

public record LoginResponse(String token, String tipo, long expiraEnSegundos) {
}
