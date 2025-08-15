package com.example.The_Ca_Nhan.Service.UnitTest;

import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.Entity.Card;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.CardMapper;
import com.example.The_Ca_Nhan.Repository.CardRepository;
import com.example.The_Ca_Nhan.Service.Implemment.CardServiceImpl;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Util.Extract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private S3Interface s3Interface;

    @Mock
    private Extract extract;

    private CardRequest cardRequest;
    private Card card;
    private CardResponse cardResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cardRequest = CardRequest.builder()
                .name("CardTest")
                .description("Description")
                .price(100)
                .imageUrl(null)
                .build();

        card = Card.builder()
                .cardId(1)
                .name("CardTest")
                .description("Description")
                .price(100)
                .url("http://example.com/image.png")
                .build();

        cardResponse = CardResponse.builder()
                .cardId(1)
                .name("CardTest")
                .description("Description")
                .price(100)
                .url("http://example.com/image.png")
                .build();
    }

    @Test
    void insertCard_success() {
        cardRequest.setImageUrl(mock(org.springframework.web.multipart.MultipartFile.class));

        when(cardMapper.toEntity(any(CardRequest.class))).thenReturn(card);
        when(s3Interface.uploadFile(any())).thenReturn("http://example.com/image.png");
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toDTO(any(Card.class))).thenReturn(cardResponse);

        CardResponse response = cardService.insertCard(cardRequest);

        assertNotNull(response);
        assertEquals("CardTest", response.getName());
        verify(s3Interface, times(1)).uploadFile(any());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void insertCard_failure_noImage() {
        cardRequest.setImageUrl(null);

        AppException ex = assertThrows(AppException.class, () -> cardService.insertCard(cardRequest));
        assertEquals(ErrorCode.FILE_IMAGE_EMPTY, ex.getErrorCode());
    }

    @Test
    void updateCard_success() {
        cardRequest.setImageUrl(mock(org.springframework.web.multipart.MultipartFile.class));

        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(s3Interface.uploadFile(any())).thenReturn("http://example.com/new-image.png");
        when(cardMapper.toDTO(any(Card.class))).thenReturn(cardResponse);
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardRequest updateRequest = CardRequest.builder()
                .name("CardUpdate")
                .description("DescUpdate")
                .price(200)
                .imageUrl(mock(org.springframework.web.multipart.MultipartFile.class))
                .build();

        CardResponse response = cardService.updateCard(updateRequest, 1);

        assertNotNull(response);
        assertEquals("CardTest", response.getName()); // cardMapper.toDTO mock trả về cardResponse
        verify(s3Interface, times(1)).deleteImage(any());
        verify(s3Interface, times(1)).uploadFile(any());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void updateCard_failure_notFound() {
        when(cardRepository.findById(1)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> cardService.updateCard(cardRequest, 1));
        assertEquals(ErrorCode.CARD_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void deleteCard_success() {
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        doNothing().when(s3Interface).deleteImage(any());
        doNothing().when(cardRepository).deleteById(1);

        assertDoesNotThrow(() -> cardService.deleteCard(1));
        verify(s3Interface, times(1)).deleteImage(any());
        verify(cardRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteCard_failure_notFound() {
        when(cardRepository.findById(1)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> cardService.deleteCard(1));
        assertEquals(ErrorCode.CARD_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void findAll_success() {
        List<Card> list = new ArrayList<>();
        list.add(card);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> page = new PageImpl<>(list, pageable, list.size());

        when(cardRepository.findAll(pageable)).thenReturn(page);
        when(cardMapper.toDTO(card)).thenReturn(cardResponse);

        Page<CardResponse> result = cardService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("CardTest", result.getContent().get(0).getName());
    }

    @Test
    void findById_success() {
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(cardMapper.toDTO(card)).thenReturn(cardResponse);

        CardResponse response = cardService.findById(1);

        assertNotNull(response);
        assertEquals("CardTest", response.getName());
    }

    @Test
    void findById_failure_notFound() {
        when(cardRepository.findById(1)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> cardService.findById(1));
        assertEquals(ErrorCode.CARD_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void findByName_success() {
        List<Card> list = new ArrayList<>();
        list.add(card);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> page = new PageImpl<>(list, pageable, list.size());

        when(cardRepository.findByNameContainingIgnoreCase("Card", pageable)).thenReturn(page);
        when(cardMapper.toDTO(card)).thenReturn(cardResponse);

        Page<CardResponse> result = cardService.findByName("Card", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("CardTest", result.getContent().get(0).getName());
    }
}
