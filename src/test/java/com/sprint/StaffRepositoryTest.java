package com.sprint;

import com.sprint.Entities.Staff;
import com.sprint.Repository.StaffRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    // =====================================================
    // METHOD 1 — findByStore_StoreId
    // =====================================================

    // Test 1 — valid storeId returns data
    @Test
    public void test_findByStore_success() {
        Page<Staff> result =
                staffRepository.findByStore_StoreId(1L, PageRequest.of(0, 5));

        assertNotNull(result);
        assertFalse(result.isEmpty());

        result.getContent().forEach(staff ->
                assertEquals(1L, staff.getStore().getStoreId())
        );
    }

    // Test 2 — invalid storeId returns empty
    @Test
    public void test_findByStore_noResult() {
        Page<Staff> result =
                staffRepository.findByStore_StoreId(999999L, PageRequest.of(0, 5));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test 3 — pagination works correctly
    @Test
    public void test_findByStore_pagination() {
        Page<Staff> result =
                staffRepository.findByStore_StoreId(1L, PageRequest.of(0, 1));

        assertEquals(1, result.getSize());
    }

    // =====================================================
    // METHOD 2 — findByFirstNameContainingIgnoreCaseAndStore_StoreId
    // =====================================================

    // Test 4 — valid name + store returns data
    @Test
    public void test_searchByNameAndStore_success() {
        Page<Staff> result =
                staffRepository.findByFirstNameContainingIgnoreCaseAndStore_StoreId(
                        "Mike", 1L, PageRequest.of(0, 5)
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());

        result.getContent().forEach(staff -> {
            assertTrue(staff.getFirstName().toLowerCase().contains("mike"));
            assertEquals(1L, staff.getStore().getStoreId());
        });
    }

    // Test 5 — case-insensitive search
    @Test
    public void test_search_caseInsensitive() {
        Page<Staff> result =
                staffRepository.findByFirstNameContainingIgnoreCaseAndStore_StoreId(
                        "mIkE", 1L, PageRequest.of(0, 5)
                );

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // Test 6 — no matching name
    @Test
    public void test_search_noResult() {
        Page<Staff> result =
                staffRepository.findByFirstNameContainingIgnoreCaseAndStore_StoreId(
                        "XYZ", 1L, PageRequest.of(0, 5)
                );

        assertTrue(result.isEmpty());
    }

    // Test 7 — correct name but wrong store
    @Test
    public void test_search_wrongStore() {
        Page<Staff> result =
                staffRepository.findByFirstNameContainingIgnoreCaseAndStore_StoreId(
                        "Mike", 999999L, PageRequest.of(0, 5)
                );

        assertTrue(result.isEmpty());
    }

    // Test 8 — pagination works for search
    @Test
    public void test_search_pagination() {
        Page<Staff> result =
                staffRepository.findByFirstNameContainingIgnoreCaseAndStore_StoreId(
                        "Mike", 1L, PageRequest.of(0, 1)
                );

        assertEquals(1, result.getSize());
    }
}