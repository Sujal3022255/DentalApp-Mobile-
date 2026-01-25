package com.example.dental

import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun login_success_test() {
        // Arrange - Create mock repository and viewModel
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        // Configure mock behavior - when login is called, invoke callback with success
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        // Variables to capture callback results
        var successResult = false
        var messageResult = ""

        // Act - Call login method
        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert - Verify the results
        assertTrue("Login should be successful", successResult)
        assertEquals("Login success", messageResult)

        // Verify - Ensure repository method was called with correct parameters
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }
}

// Mock interfaces for the test
interface UserRepo {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
}

class UserViewModel(private val repo: UserRepo) {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        repo.login(email, password, callback)
    }
}