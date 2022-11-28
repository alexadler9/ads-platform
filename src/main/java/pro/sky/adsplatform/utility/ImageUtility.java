package pro.sky.adsplatform.utility;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.datical.liquibase.ext.init.InitProjectUtil.getExtension;

/**
 * Вспомогательный класс для работы с файлами изображений.
 */
public class ImageUtility {
    private static byte[] getContent(MultipartFile file, boolean isThumbnail) throws IOException {
        String filename = file.getOriginalFilename();
        String ext = getExtension(filename);

        byte[] imageBytes = file.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);

        BufferedImage imgIn = ImageIO.read(bais);
        if (imgIn == null) {
            return new byte[0];
        }

        BufferedImage imgOut = imgIn;
        if (isThumbnail) {
            double height = imgIn.getHeight() / (imgIn.getWidth() / 250d);
            imgOut = new BufferedImage(250, (int) height, imgIn.getType());
            Graphics2D graphics = imgOut.createGraphics();
            graphics.drawImage(imgIn, 0, 0, 250, (int) height, null);
            graphics.dispose();
        }

        ImageIO.write(imgOut, ext, baos);

        return baos.toByteArray();
    }

    /**
     * Возвращает содержимое изображения.
     *
     * @param file файл изображения.
     * @return содержимое изображения.
     */
    public static byte[] getImageContent(MultipartFile file) throws IOException {
        return getContent(file, false);
    }

    /**
     * Возвращает содержимое превью изображения.
     *
     * @param file файл изображения.
     * @return содержимое превью изображения.
     */
    public static byte[] getThumbnailImageContent(MultipartFile file) throws IOException {
        return getContent(file, true);
    }
}