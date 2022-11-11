import com.kokoakuma.kotlinhttpserver.FileReader
import com.kokoakuma.kotlinhttpserver.exceptions.NotFoundException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FileReaderTest {
    @Test
    fun readSampleTxt() {
        val reader = FileReader("publicTest/FileReaderTestSample.txt")
        assertEquals(
            """First line
                |Second line
                |Third line
            """.trimMargin(),
            reader.content()
        )
    }

    @Test
    fun testFileNotExist() {
        val reader = FileReader("publicTest/FileNotExist.txt")
        val actual = assertFailsWith<NotFoundException> {
            reader.content()
        }

        assertEquals(404, actual.status)
        assertEquals("Not Found", actual.reason)
    }
}