import com.kokoakuma.kotlinhttpserver.RequestParser
import com.kokoakuma.kotlinhttpserver.exceptions.BadRequestException
import com.kokoakuma.kotlinhttpserver.models.HttpMethod
import com.kokoakuma.kotlinhttpserver.models.HttpVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequestParserTest {
    private val parser: RequestParser = RequestParser()
    private val crlf = "\r\n"

    @Test
    fun shouldReturnGETMethod() {
        val requestByteArray = "GET /index.html HTTP/1.1${crlf}Host: localhost:8000${crlf}${crlf}".encodeToByteArray()
        val context = parser.parse(requestByteArray)
        assertEquals(HttpMethod.GET, context.method, "should return GET method")
    }

    @Test
    fun shouldReturnPOSTMethod() {
        val requestByteArray = "POST /index.html HTTP/1.1${crlf}Host: localhost:8000${crlf}${crlf}".encodeToByteArray()
        val context = parser.parse(requestByteArray)
        assertEquals(HttpMethod.POST, context.method, "should return POST method")
    }

    @Test
    fun shouldReturnHTTP_0_9_ifRequestDoesNotHaveHttpVersion() {
        val requestByteArray = "POST /index.html${crlf}Host: localhost:8000${crlf}${crlf}".encodeToByteArray()
        val context = parser.parse(requestByteArray)
        assertEquals(HttpVersion.HTTP_0_9, context.httpVersion, "should return HTTP 0.9")
    }

    @Test
    fun shouldReturnHTTP_1_0() {
        val requestByteArray = "POST /index.html HTTP/1.0${crlf}Host: localhost:8000${crlf}${crlf}".encodeToByteArray()
        val context = parser.parse(requestByteArray)
        assertEquals(HttpVersion.HTTP_1_0, context.httpVersion, "should return HTTP 1.0")
    }

    @Test
    fun shouldReturnHTTP_1_1() {
        val requestByteArray = "POST /index.html HTTP/1.1${crlf}Host: localhost:8000${crlf}${crlf}".encodeToByteArray()
        val context = parser.parse(requestByteArray)
        assertEquals(HttpVersion.HTTP_1_1, context.httpVersion, "should return HTTP 1.1")
    }

    @Test
    fun shouldNotParseHttpResponse() {
        val responseByteArray = "HTTP/1.1 200 OK${crlf}${crlf}".encodeToByteArray()
        val actual = assertFailsWith<BadRequestException> {
            parser.parse(responseByteArray)
        }
        assertEquals(400, actual.status)
        assertEquals("Bad Request", actual.reason)
    }
}