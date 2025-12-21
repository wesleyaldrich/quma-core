package com.quma.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class QrCodeService {

    @Value("${ticket.storage-path}")
    private String storagePath;

    public String generate(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height
        );

        BufferedImage image = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(
                        x,
                        y,
                        matrix.get(x, y) ? 0x000000 : 0xFFFFFF
                );
            }
        }

        return save(image);
    }

    public String save(BufferedImage image) throws IOException {
        String fileName = UUID.randomUUID() + ".png";

        Path directory = Paths.get(storagePath);
        Files.createDirectories(directory);

        Path filePath = directory.resolve(fileName);
        ImageIO.write(image, "png", filePath.toFile());

        return "/tickets/images/" + fileName;
    }
}
