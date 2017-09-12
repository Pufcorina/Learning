import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.min;

public class Image {
    private int height;
    private int width;
    private String path;
    private int[][] r;
    private int[][] g;
    private int[][] b;
    private int[][] grayScale;
    private int region;
    private int[][][] blureMatrix;

    public Image(){}
    public Image(String path) {
        this.path = path;
        readImageFromFile();
        grayScaleImageFilter();
        gaussianBlurImageFilter();
    }

    private void readImageFromFile() {
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(path));
            height = img.getHeight();
            width = img.getWidth();
            r = new int[width][height];
            g = new int[width][height];
            b = new int[width][height];
            grayScale = new int[width][height];
            blureMatrix = new int[width][height][3];

            int[] aux;

            for(int i = 0; i < height; i++)
                for(int j = 0; j < width; j++)
                {
                    aux = img.getRaster().getPixel(j, i, new int[3]);
                    r[j][i] = aux[0];
                    g[j][i] = aux[1];
                    b[j][i] = aux[2];
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportImageToFile(String fileName)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                int[] aux = new int[3];
                aux[0] = r[j][i];
                aux[1] = g[j][i];
                aux[2] = b[j][i];
                img.getRaster().setPixel(j, i, aux);
            }
        File outputfile = new File(fileName);
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void grayScaleImageFilter()
    {
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++) {
                int sum = (int)(r[j][i] * 0.299 + g[j][i] * 0.587 + b[j][i] * 0.114);
                //int sum = (int)((r[j][i] + g[j][i] + b[j][i])/3);
                if(sum > 255 )
                    grayScale[j][i] = 255;
                else
                    grayScale[j][i] = sum;
            }
    }

    public void exportGrayScaleImageToFile(String fileName)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                int[] aux = new int[3];
                aux[0] = grayScale[j][i];
                aux[1] = grayScale[j][i];
                aux[2] = grayScale[j][i];
                img.getRaster().setPixel(j, i, aux);
            }
        File outputfile = new File(fileName);
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gaussianBlurImageFilter()
    {
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                blureMatrix[j][i] = getAveragePixel(i, j, 21);
    }

    private int[] getAveragePixel(int x, int y, int size)
    {
        int[] average = new int[3];
        average[0] = 0;
        average[1] = 0;
        average[2] = 0;
        int pix = 0;
        for(int i = x - (size/2); i < x + (size/2)+1; i++)
            if(i >= 0 &&  i < height)
                for(int j = y - 1; j < y + 2; j++)
                    if(j >= 0 && j < width)
                        if(x != i || y != j)
                        {
                            pix++;
                            average[0] += r[j][i];
                            average[1] += g[j][i];
                            average[2] += b[j][i];
                        }
        average[0] = average[0] / pix;
        average[1] = average[1] / pix;
        average[2] = average[2] / pix;

        return average;
    }

    public void exportGaussianBlurImageToFile(String fileName)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                img.getRaster().setPixel(j, i, blureMatrix[j][i]);
        File outputfile = new File(fileName);
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
