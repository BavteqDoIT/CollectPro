package com.bavteqdoit.service;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.repository.BoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoxServiceTest {

    private BoxRepository boxRepository;
    private FundraisingEventService fundraisingEventService;
    private BoxService boxService;

    @BeforeEach
    void setUp() {
        boxRepository = mock(BoxRepository.class);
        fundraisingEventService = mock(FundraisingEventService.class);
        boxService = new BoxService(boxRepository, fundraisingEventService);
    }

    @Test
    void createBox_validBox_returnsSavedBox() {
        Box box = new Box();
        when(boxRepository.save(box)).thenReturn(box);

        Box result = boxService.createBox(box);

        assertEquals(box, result);
        verify(boxRepository).save(box);
    }

    @Test
    void createBox_null_throwsException() {
        assertThrows(NullPointerException.class, () -> boxService.createBox(null));
    }

    @Test
    void getAllBoxes_returnsAllBoxes() {
        List<Box> boxes = List.of(new Box(), new Box());
        when(boxRepository.findAll()).thenReturn(boxes);

        List<Box> result = boxService.getAllBoxes();

        assertEquals(2, result.size());
        verify(boxRepository).findAll();
    }

    @Test
    void getBoxById_validId_returnsBox() {
        Box box = new Box();
        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        Box result = boxService.getBoxById(1L);

        assertEquals(box, result);
    }

    @Test
    void getBoxById_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.getBoxById(0L));
    }

    @Test
    void getBoxById_notFound_throwsException() {
        when(boxRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> boxService.getBoxById(1L));
    }

    @Test
    void updateBox_valid_updatesBox() {
        Box existing = new Box();
        existing.setId(1L);
        Box updated = new Box();
        updated.setPrice(BigDecimal.TEN);
        updated.setRented(true);
        updated.setStartDate(LocalDate.now());
        updated.setEndDate(LocalDate.now().plusDays(2));
        updated.setSum(BigDecimal.ONE);

        when(boxRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(boxRepository.save(any())).thenReturn(existing);

        Box result = boxService.updateBox(1L, updated);

        assertEquals(BigDecimal.TEN, result.getPrice());
        assertTrue(result.isRented());
    }

    @Test
    void updateBox_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.updateBox(0L, new Box()));
    }

    @Test
    void updateBox_nullUpdated_throwsException() {
        assertThrows(NullPointerException.class, () -> boxService.updateBox(1L, null));
    }

    @Test
    void deleteBox_validEmptyNotRented_deletesBox() {
        Box box = new Box();
        box.setSum(BigDecimal.ZERO);
        box.setRented(false);

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        boxService.deleteBox(1L);

        verify(boxRepository).deleteById(1L);
    }

    @Test
    void deleteBox_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.deleteBox(0L));
    }

    @Test
    void deleteBox_rented_throwsException() {
        Box box = new Box();
        box.setSum(BigDecimal.ZERO);
        box.setRented(true);

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        assertThrows(ResponseStatusException.class, () -> boxService.deleteBox(1L));
    }

    @Test
    void deleteBox_notEmpty_throwsException() {
        Box box = new Box();
        box.setSum(BigDecimal.ONE);
        box.setRented(false);

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        assertThrows(ResponseStatusException.class, () -> boxService.deleteBox(1L));
    }

    @Test
    void emptyBox_zeroSum_returnsTrue() {
        Box box = new Box();
        box.setSum(BigDecimal.ZERO);
        assertTrue(boxService.emptyBox(box));
    }

    @Test
    void emptyBox_nonZeroSum_returnsFalse() {
        Box box = new Box();
        box.setSum(BigDecimal.TEN);
        assertFalse(boxService.emptyBox(box));
    }

    @Test
    void emptyBox_null_throwsException() {
        assertThrows(NullPointerException.class, () -> boxService.emptyBox(null));
    }

    @Test
    void rentBox_valid_returnsRentedBox() {
        Box box = new Box();
        box.setRented(false);

        FundraisingEvent event = new FundraisingEvent();

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(fundraisingEventService.getFundraisingEventById(2L)).thenReturn(event);
        when(boxRepository.save(any())).thenReturn(box);

        Box result = boxService.rentBox(1L, 5, 2L);

        assertTrue(result.isRented());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(LocalDate.now().plusDays(5), result.getEndDate());
        assertEquals(event, result.getFundraisingEvent());
    }

    @Test
    void rentBox_alreadyRented_throwsException() {
        Box box = new Box();
        box.setRented(true);

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        assertThrows(ResponseStatusException.class, () -> boxService.rentBox(1L, 3, 2L));
    }

    @Test
    void rentBox_invalidDays_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.rentBox(1L, 0, 2L));
    }

    @Test
    void rentBox_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.rentBox(0L, 3, 2L));
    }

    @Test
    void addToBox_validAmount_addsToSum() {
        Box box = new Box();
        box.setSum(BigDecimal.TEN);

        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        boxService.addToBox(1L, BigDecimal.ONE);

        assertEquals(new BigDecimal("11"), box.getSum());
        verify(boxRepository).save(box);
    }

    @Test
    void addToBox_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.addToBox(0L, BigDecimal.ONE));
    }

    @Test
    void addToBox_nullAmount_throwsException() {
        assertThrows(NullPointerException.class, () -> boxService.addToBox(1L, null));
    }

    @Test
    void addToBox_negativeAmount_throwsException() {
        assertThrows(ResponseStatusException.class, () -> boxService.addToBox(1L, new BigDecimal("-5")));
    }
}
