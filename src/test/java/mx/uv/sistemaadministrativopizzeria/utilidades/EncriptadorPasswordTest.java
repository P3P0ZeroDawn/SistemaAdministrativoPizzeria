package mx.uv.sistemaadministrativopizzeria.utilidades;

import mx.uv.sistemaadministrativopizzeria.utilidades.EncriptadorPassword;
import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class EncriptadorPasswordTest {

    @Test
    public void testSha256BytesProducesExpectedLength() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] b = EncriptadorPassword.sha256Bytes("hola");
        assertNotNull(b);
        assertEquals(32, b.length);
    }

    @Test
    public void testSha256DifferentInputsDifferentOutput() throws Exception {
        byte[] a = EncriptadorPassword.sha256Bytes("uno");
        byte[] b = EncriptadorPassword.sha256Bytes("dos");
        assertNotEquals(new String(a), new String(b));
    }
    
    @Test
    public void testSha256BytesDeHolaCoincideConHashConocido() throws Exception {
        byte[] bytes = EncriptadorPassword.sha256Bytes("hola");

        assertEquals(
                "b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79",
                toHex(bytes)
        );
    }
    
    private String toHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for(byte b : bytes){
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
