package pro.sky.adsplatform.utility;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.datical.liquibase.ext.init.InitProjectUtil.getExtension;

public class ImageUtility {
    /**
     * Возвращает содержимое изображения.
     *
     * @param file файл изображения.
     * @return содержимое изображения.
     */
    public static byte[] getImageContent(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String fileNameOriginal = file.getOriginalFilename();
        String ext = getExtension(fileNameOriginal);

        byte[] imageByte = file.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(imageByte);

        BufferedImage imgIn = ImageIO.read(bais);
        if (imgIn == null) {
            return null;
        }

        double height = imgIn.getHeight() / (imgIn.getWidth() / 250d);
        BufferedImage imgOut = new BufferedImage(250, (int) height, imgIn.getType());
        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage((Image) imgIn, 0, 0, 250, (int) height, null);
        graphics.dispose();

        ImageIO.write(imgOut, ext, baos);

        return baos.toByteArray();
    }
}
