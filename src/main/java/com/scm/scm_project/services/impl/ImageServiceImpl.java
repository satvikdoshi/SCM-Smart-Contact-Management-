package com.scm.scm_project.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.scm_project.helper.AppConstants;
import com.scm.scm_project.services.ImageService;


@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file, String fileName) {
        try {

            
            byte[] data = file.getBytes();
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id", fileName
            ));
            

        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the exception better, e.g., throw a custom exception
        }

        return getUrlFromPublicId(fileName);
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary
            .url()
            .transformation(
                new Transformation<>()
                    .width(AppConstants.CONTACT_IMAGE_WIDTH)
                    .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                    .crop(AppConstants.CONTACT_IMAGE_CROP)
            )
            .generate(publicId);
    }
}
