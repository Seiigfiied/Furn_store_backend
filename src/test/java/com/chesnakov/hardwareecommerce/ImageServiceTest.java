package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.entity.ImageModel;
import com.chesnakov.hardwareecommerce.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ImageServiceTest {

    private ImageService imageService;

    @Mock
    private MultipartFile mockMultipartFile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageService();
    }

    @Test
    public void testUploadImage() throws IOException {
        // create a mock MultipartFile
        byte[] content = {1, 2, 3};
        when(mockMultipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockMultipartFile.getContentType()).thenReturn("image/jpeg");
        when(mockMultipartFile.getBytes()).thenReturn(content);

        // upload the mock file using the image service
        MultipartFile[] multipartFiles = {mockMultipartFile};
        Set<ImageModel> imageModels = imageService.uploadImage(multipartFiles);

        // assert that the image model was created correctly
        assertEquals(1, imageModels.size());
        ImageModel imageModel = imageModels.iterator().next();
        assertEquals("test.jpg", imageModel.getName());
        assertEquals("image/jpeg", imageModel.getType());
        assertEquals(content, imageModel.getPicByte());
    }
}