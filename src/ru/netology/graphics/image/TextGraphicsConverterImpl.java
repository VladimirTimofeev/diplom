package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;


public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    public TextGraphicsConverterImpl() {
        this.schema = new TextColorSchemaImpl();
    }

    public TextGraphicsConverterImpl(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));
        int height = img.getHeight();
        int width = img.getWidth();
        double newRatio = 0;

        int minSideImg = Math.min(height, width);
        int maxSideImg = Math.max(height, width);
        double ratio = (double) maxSideImg / minSideImg;
        if (ratio > this.maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        double newRatioHeight = 1;
        if(height > maxHeight) {
            newRatioHeight = (double) height / maxHeight;
        }

        double newRatioWidth = 1;
        if(width > maxWidth) {
            newRatioWidth = (double) width / maxWidth;
        }
         if(newRatioHeight > 1 && newRatioWidth > 1) {
             newRatio = Math.max(newRatioHeight, newRatioWidth);
         }

         if (newRatioHeight > 1 && newRatioWidth < 1) {
             newRatio = newRatioHeight;
         }

         if (newRatioHeight < 1 && newRatioWidth > 1) {
             newRatio = newRatioWidth;
         }

        if (newRatioHeight == newRatioWidth) {
            newRatio = newRatioWidth;
        }

        int newWidth = (int)Math.ceil(width / newRatio);
        int newHeight = (int)Math.ceil(height / newRatio);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder sb = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                sb.append(c).append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
