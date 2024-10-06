package com.javamongo.moviebooktickets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.javamongo.moviebooktickets.dto.MyResponse;
import com.javamongo.moviebooktickets.entity.Image;
import com.javamongo.moviebooktickets.repository.ImageRepository;
import java.util.List;
import java.util.ArrayList;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    // Đường dẫn đến thư mục static/images trong project
    private static String UPLOAD_DIR = "src/main/resources/static/images/";

    public MyResponse<List<Image>> getAllImages() {
        MyResponse<List<Image>> response = new MyResponse<>();
        List<Image> images = imageRepository.findAll();
        response.setStatus(200);
        response.setMessage("Danh sách hình ảnh");
        response.setData(images);
        return response;
    }

    public MyResponse<List<Image>> addImage(MultipartFile[] files) {
        MyResponse<List<Image>> response = new MyResponse<>();
        List<Image> savedImages = new ArrayList<>();

        try {
            // Duyệt qua từng file trong mảng files
            for (MultipartFile file : files) {
                // Kiểm tra định dạng file
                List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/gif");
                if (!allowedTypes.contains(file.getContentType())) {
                    response.setStatus(400);
                    response.setMessage("Lỗi: Định dạng file " + file.getOriginalFilename() + " không hợp lệ");
                    return response;
                }

                // File không được lớn hơn 2MB
                if (file.getSize() > 2 * 1024 * 1024) {
                    response.setStatus(400);
                    response.setMessage(
                            "Lỗi: File " + file.getOriginalFilename() + " quá lớn, vui lòng chọn file dưới 2MB");
                    return response;
                }

                // Xử lý và lưu từng file
                Image image = new Image();
                String id = this.generateImageId();
                String fileName = id + "_" + file.getOriginalFilename();

                // Kiểm file có kí tự khoảng trắng không
                if (fileName.contains(" ")) {
                    fileName = fileName.replace(" ", "_");
                }

                long size = file.getSize();
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + fileName);

                // Lưu file vào thư mục
                Files.write(path, bytes);

                // Tạo url để truy cập file
                String url = "http://localhost:8080/images/" + fileName;

                // Tạo đối tượng Image
                image.setId(id);
                image.setName(fileName);
                image.setSize(size);
                image.setUrl(url);

                // Lưu Image vào database
                Image savedImage = imageRepository.save(image);
                if (savedImage == null) {
                    response.setStatus(500);
                    response.setMessage("Lỗi: Không thể lưu hình ảnh vào database cho file " + fileName);
                    return response;
                } else {
                    savedImages.add(savedImage);
                }
            }

            // Nếu lưu thành công tất cả các file
            response.setStatus(200);
            response.setMessage("Tất cả hình ảnh đã được lưu thành công");
            response.setData(savedImages);

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Lỗi: " + e.getMessage());
        }

        return response;
    }

    public MyResponse<?> deleteImage(String id) {
        MyResponse<?> response = new MyResponse<>();
        try {
            // Tìm Image theo id
            Image image = imageRepository.findById(id).orElse(null);
            if (image == null) {
                response.setStatus(404);
                response.setMessage("Lỗi: Không tìm thấy hình ảnh với id " + id);
                return response;
            }

            // Xóa file trong thư mục
            Path path = Paths.get(UPLOAD_DIR + image.getName());
            Files.delete(path);

            // Xóa Image trong database
            imageRepository.deleteById(id);

            response.setStatus(200);
            response.setMessage("Xóa hình ảnh thành công");

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Lỗi: " + e.getMessage());
        }

        return response;
    }

    public String generateImageId() {
        // Tạo id ngẫu nhiên có 8 kí tự số chữ
        String id = "";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * characters.length());
            id += characters.charAt(index);
        }
        return id;
    }

}
