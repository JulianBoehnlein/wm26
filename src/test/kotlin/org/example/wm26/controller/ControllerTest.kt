package org.example.wm26.controller

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ControllerTest {
    @Autowired
    private lateinit var controller: MatchController

//    @Test
//    fun testClient() {
//        val result = controller.testClient()
//        assertNotNull(result)
//        println(result)
//    }

}
