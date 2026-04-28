package com.sprint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// NOTE ON PROJECTION:
// Spring Data REST keeps @ManyToOne relations (address, store) as HAL links by default.
// Projection flattens only DIRECT entity fields (firstName, lastName, email, active etc.)
// Address fields (address, phone) must be fetched separately via GET /customers/{id}/address
// Rental fields (rentalDate, returnDate) via GET /customers/{id}/rentals

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CustomerTesting {

    @Autowired
    private MockMvc mockMvc;

    // =====================================================
    // GROUP 1 — GET /customers/{id} [2 tests]
    // Projection: customerDetail
    // Only direct fields checked — address via separate call
    // =====================================================

    // Test 1
    @Test
    public void test01_getById_success() throws Exception {
        mockMvc.perform(get("/customers/1")
                .param("projection", "customerDetail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.active").exists());
    }

    // Test 2
    @Test
    public void test02_getById_notFound() throws Exception {
        mockMvc.perform(get("/customers/999999")
                .param("projection", "customerDetail"))
                .andExpect(status().isNotFound());
    }

    // =====================================================
    // GROUP 2 — GET /customers/search/ [3 tests]
    // findByFirstNameAndLastName
    // =====================================================

    // Test 3
    @Test
    public void test03_findByName_success() throws Exception {
        mockMvc.perform(get("/customers/search/findByFirstNameAndLastName")
                .param("firstName", "MARY")
                .param("lastName", "SMITH"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].firstName").value("MARY"))
                .andExpect(jsonPath("$.content[0].lastName").value("SMITH"));
    }

    // Test 4
    @Test
    public void test04_findByName_emptyResult() throws Exception {
        mockMvc.perform(get("/customers/search/findByFirstNameAndLastName")
                .param("firstName", "XYZ")
                .param("lastName", "ABC"))
                .andExpect(status().isOk());
    }

    // Test 5
    @Test
    public void test05_findByName_missingParams() throws Exception {
        mockMvc.perform(get("/customers/search/findByFirstNameAndLastName"))
                .andExpect(status().isBadRequest());
    }

    // =====================================================
    // GROUP 3 — GET /customers/search/findByEmail [3 tests]
    // =====================================================

    // Test 6
    @Test
    public void test06_findByEmail_success() throws Exception {
        mockMvc.perform(get("/customers/search/findByEmail")
                .param("email", "MARY.SMITH@sakilacustomer.org")
                .param("projection", "customerDetail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.email").value("MARY.SMITH@sakilacustomer.org"));
    }

    // Test 7
    @Test
    public void test07_findByEmail_notFound() throws Exception {
        mockMvc.perform(get("/customers/search/findByEmail")
                .param("email", "wrong@test.com"))
                .andExpect(status().isNotFound());
    }

    // Test 8
    @Test
    public void test08_findByEmail_empty() throws Exception {
        mockMvc.perform(get("/customers/search/findByEmail")
                .param("email", ""))
                .andExpect(status().isNotFound());
    }

    // =====================================================
    // GROUP 4 — GET /customers/search/findByActive [3 tests]
    // =====================================================

    // Test 9
    @Test
    public void test09_findByActive_true() throws Exception {
        mockMvc.perform(get("/customers/search/findByActive")
                .param("active", "true")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].active").value(true));
    }

    // Test 10
    @Test
    public void test10_findByActive_false() throws Exception {
        mockMvc.perform(get("/customers/search/findByActive")
                .param("active", "false")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    // Test 11
    @Test
    public void test11_findByActive_pagination() throws Exception {
        mockMvc.perform(get("/customers/search/findByActive")
                .param("active", "true")
                .param("page", "1")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(2));
    }

    // =====================================================
    // GROUP 4B — GET /customers/{id}/address [2 tests]
    // Address fields tested via the separate HAL link endpoint
    // This is how address data is actually accessed
    // =====================================================

    // Test — address fields exist at separate endpoint
    @Test
    public void test_getAddressByCustomerId_success() throws Exception {
        mockMvc.perform(get("/customers/1/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").exists()) // address field
                .andExpect(jsonPath("$.phone").exists()); // phone field
    }

    // Test — rental list via separate endpoint
    @Test
    public void test_getRentalsByCustomerId_success() throws Exception {
        mockMvc.perform(get("/customers/1/rentals"))
                .andExpect(status().isOk());
    }

    // =====================================================
    // GROUP 5 — POST /customers [3 tests]
    // =====================================================

    // Test 12
    @Test
    public void test12_post_validCustomer() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Test",
                          "lastName": "User",
                          "email": "test@test.com",
                          "active": true,
                          "store": "http://localhost/customers/../stores/1",
                          "address": "http://localhost/customers/../addresses/1"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    // Test 13
    @Test
    public void test13_post_missingFirstName() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "lastName": "User",
                          "active": true
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // Test 14
    @Test
    public void test14_post_missingLastName() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Test",
                          "active": true
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // =====================================================
    // GROUP 6 — PUT /customers/{id} [4 tests]
    // =====================================================

    // Test 15
    @Test
    public void test15_put_success() throws Exception {
        mockMvc.perform(put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Updated",
                          "lastName": "User",
                          "email": "updated@test.com",
                          "active": true,
                          "store": "/stores/1",
                          "address": "/addresses/1"
                        }
                        """))
                .andExpect(status().isNoContent());

        // verify
        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    // Test 16
    @Test
    public void test16_put_notFound() throws Exception {
        mockMvc.perform(put("/customers/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isConflict()); // Spring Data REST returns 409 Conflict for PUT on non-existent ID
    }

    // Test 17
    @Test
    public void test17_put_invalidBody() throws Exception {
        mockMvc.perform(put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json"))
                .andExpect(status().isBadRequest());
    }

    // Test 18
    @Test
    public void test18_patch_partialUpdate() throws Exception {
        mockMvc.perform(patch("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "PartialUpdate"
                        }
                        """))
                .andExpect(status().isNoContent()); // still 204

        mockMvc.perform(get("/customers/1"))
                .andExpect(jsonPath("$.firstName").value("PartialUpdate"));
    }

    // =====================================================
    // GROUP 7 — VALIDATION EDGE CASES [7 tests]
    // =====================================================

    // Test 19
    @Test
    public void test19_blankFirstName() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "",
                          "lastName": "User"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // Test 20
    @Test
    public void test20_blankLastName() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Test",
                          "lastName": ""
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // Test 21
    @Test
    public void test21_invalidEmail() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Test",
                          "lastName": "User",
                          "email": "bad-email"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // Test 22
    @Test
    public void test22_longFirstName() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"" + "A".repeat(46) + "\"}"))
                .andExpect(status().isBadRequest());
    }

    // Test 23
    @Test
    public void test23_missingStore() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Test",
                          "lastName": "User"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // Test 24
    @Test
    public void test24_missingActive() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Test",
                          "lastName": "User"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    // Test 25
    @Test
    public void test25_validCustomer_full() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "Valid",
                          "lastName": "User",
                          "email": "valid@test.com",
                          "active": true,
                          "store": "/stores/1",
                          "address": "/addresses/1"
                        }
                        """))
                .andExpect(status().isCreated());
    }
}